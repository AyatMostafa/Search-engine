import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.concurrent.ThreadLocalRandom;

public class Crawler {
    private float frequency;
    private float threshold;

    public Crawler(int seed){
        this.frequency = 50;
        this.threshold = 5000;
    }
    public static void main(String[] args) throws InterruptedException {

        int seed = 3; // to be read as an argument
        Set<String> visitedSet = new HashSet<>();
        Set<String> syncVisitedSet = Collections.synchronizedSet(visitedSet);
        CsvWriter dB = new CsvWriter("myUrls.txt", "indexer.txt");
        final String[] initialSeed = {"https://stackoverflow.com","https://en.wikipedia.org","https://www.javatpoint.com","https://www.nationalgeographic.com","https://www.quora.com","https://www.theguardian.com","https://www.finalthoughts.com","https://www.goodhousekeeping.com"};
        ArrayList<Thread> crawlerAssistants = new ArrayList<>();
        for (int i=0; i< seed; i++){
            crawlerAssistants.add(new Thread(new CrawlingThread(dB,initialSeed[i], syncVisitedSet)));
            crawlerAssistants.get(i).setName(((Integer)i).toString());
            crawlerAssistants.get(i).start();
        }
        for (int i=0; i< seed; i++)
            crawlerAssistants.get(i).join();
        System.out.println("50 pages reached");
    }

    public static class CsvWriter {
        private PrintWriter urlWriter, indexerWriter;
        private StringBuilder urlSb, indexerSb;
        private int count;
        private BufferedReader csvReader ;
        private List<String> urlStore;
        private Random rand = new Random();

        public CsvWriter(String urlFilePath, String indexerFilePath) {
            try  {
                urlWriter = new PrintWriter(new File(java.lang.String.valueOf(urlFilePath)));
                indexerWriter = new PrintWriter(new File(java.lang.String.valueOf(indexerFilePath)));
                csvReader = new BufferedReader(new FileReader(urlFilePath));
                urlStore = new ArrayList<>();
                count = 1;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        public int getCount(){
            return count;
        }
        public List<String> getStore(){ return urlStore; }
        public int next() {int s = getStore().size(); if (s>0) return ThreadLocalRandom.current().nextInt(s); else return -1;}
        public void incrementCount(){
            count ++;
        }
        public BufferedReader getCsvReader(){return  csvReader;}
        public void writeData(String row, boolean url) {
            if(url) {
                //urlSb = new StringBuilder();
                //urlSb.append(row);
                //urlSb.append("\r\n");
                //urlWriter.write(urlSb.toString());
                //urlWriter.flush();
                urlStore.add(row);
            }
            else{
                indexerSb = new StringBuilder();
                indexerSb.append(row);
                indexerSb.append("\r\n");
                //System.out.println(count);
                indexerWriter.write(indexerSb.toString());
                indexerWriter.flush();
            }
            //System.out.println("done!");
        }
    }
}
