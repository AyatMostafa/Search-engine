import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrawlingThread implements Runnable {
    private Crawler.CsvWriter writer;
    Set<String> visitedSet;
    private boolean firstCrawl;
    private String firstSeed;

    public CrawlingThread(Crawler.CsvWriter writer, String firstSeed, Set<String> visitedSet) {
        this.writer = writer;
        firstCrawl = true;
        this.firstSeed = firstSeed;
        this.visitedSet = visitedSet;
    }

    @Override
    public void run() {
        doWork();
    }

    public void doWork() {
        int currentCount=0;
        while(true) {
            String url = "";
            if (firstCrawl) {
                url = firstSeed;
                firstCrawl = false;
                synchronized (writer){currentCount = writer.getCount(); writer.incrementCount();}
            }
            else {
                int next = writer.next();
                if (next == -1 || next == writer.getStore().size()) continue;
                synchronized (writer) {
                    currentCount = writer.getCount();
                    writer.incrementCount();
                    if (writer.getCount() <= 50) {
                        if ((url = writer.getStore().get(next)) != null) {
                            while (visitedSet.contains(url) && (url != null)) {
                                url = writer.getStore().get(next);
                                writer.getStore().remove(next);
                            }
                            //write = true;
                        }
                    } else {
                        try {
                            writer.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    visitedSet.add(url);
                }
            }
            //System.out.println(url);
            String  html = parsePage(url);
            //String lastMode = data[0];
            String data = getPageDetails(html);
            String id = ((Integer)currentCount).toString();
            String details = id+", "+url+", "+data;
            Set<String> urls = getUrls(html, url);
            synchronized (writer) {
                writer.writeData(details, false);
                System.out.println(details + " Thread Name "+ Thread.currentThread().getName());
            }
            synchronized (writer) {
                Iterator<String> itr = urls.iterator();
                while (itr.hasNext())
                    writer.writeData(itr.next(), true);
                }
            String stripped = Jsoup.clean(html, Whitelist.none());
            String  bold = getPageHeaders(url);
            PrintWriter contentWriter = null, boldWriter = null;
            String current = id + ".txt";
            try {
                contentWriter = new PrintWriter("content"+"/"+current, "UTF-8");
                boldWriter = new PrintWriter("bold"+"/"+current, "UTF-8");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            contentWriter.println(stripped);
            boldWriter.println(bold);
            contentWriter.close();
            boldWriter.close();
        }
    }

    public String parsePage(String urlToVisit) {
        String html = "";
        try {
            URL url = new URL(urlToVisit);
            URLConnection urlcon = url.openConnection();
            InputStream stream = urlcon.getInputStream();
            int i;
            while ((i = stream.read()) != -1) {
                html += ((char) i);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return html;
    }

    public String getPageDetails(String html) {
        Pattern p1 = Pattern.compile("<title>(.+?)</title>");
        Matcher title = p1.matcher(html);
        Pattern p2 = Pattern.compile("datetime=(.+?) ");
        Matcher date = p2.matcher(html);
        String details = "";
        if (date.find()) {
            details += date.group(1) ;
            details=details.substring(0, 12)+", ";
        }

        else
            details += "0000-00-00, ";
        if (title.find())
            details += title.group(1) + ", ";
        else
            details += "No Title";
        return details;
    }

    public String getPageHeaders(String html){
        Pattern p2 = Pattern.compile("<h2>(.+?)</h2>");
        Matcher bold = p2.matcher(html);
        String headerText = "";
        while (bold.find())
            headerText += (bold.group(1)) + ",";
        return headerText;
    }

    public Set<String> getUrls(String html, String baseURL) {
        Set<String> urls = new HashSet<>();
        Pattern p1 = Pattern.compile("href=\"(.*?)\"");
        Matcher links = p1.matcher(html);
        while (links.find()) {
            String url = links.group(1);
            if(!url.startsWith("http"))
                url = baseURL +'/'+ url;
            urls.add(url); // this variable should contain the URL details to be stored
        }
        return urls;
    }
}