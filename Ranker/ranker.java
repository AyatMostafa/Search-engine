package jdbc_demo;

import java.util.*;
import java.sql.ResultSet;
import java.sql.SQLException; 
import static java.util.stream.Collectors.*;
import static java.util.Map.Entry.*;
import java.time.LocalDate;
import java.time.Period;
import java.io.*; 
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ranker {
	
	public static double IDF(double docsSize, double numberOfDocs)
	{
		return Math.log(docsSize / numberOfDocs);
	}
	
	public static Map<String,Double> pageRank() throws SQLException
	{
		Map <String,Double> PR =  new HashMap<String,Double>();
		String rank_query = "select RankTable ;" ;
        ResultSet rank_res = Driver.DB.execute_select_query(rank_query);
        
        Map<String,HashSet<String>> links_to_page = new HashMap<String, HashSet<String>>();
        Map<String,Integer> outDegrees = new HashMap<String, Integer>();
        
        while(rank_res.next())
        {
        	String URL_from = rank_res.getString("URLfrom");
        	String URL_to = rank_res.getString("URLto");
        	PR.put(URL_from, null);
        	PR.put(URL_to, null);
        	
        	if(!links_to_page.containsKey(URL_to))
        		links_to_page.put(URL_to, new HashSet<String>());
        	else
        	{
        		HashSet<String>temp = links_to_page.get(URL_to);
                temp.add(URL_from);
                links_to_page.replace(URL_to,temp);
        	}
        	if(!outDegrees.containsKey(URL_from))
        		outDegrees.put(URL_from, 1);
            else
            	outDegrees.replace(URL_from, outDegrees.get(URL_from) + 1);
        }
        
        PR.replaceAll((k,v) -> (1.0/PR.size()));
        
        int iterations = 10;
        for(int i = 0 ; i < iterations ; i++)
        {
            for(Map.Entry<String, Double> entry: PR.entrySet())
            {
            	double rank = 0;
                String URL = (String) entry.getKey(); 
                HashSet<String> in_links = links_to_page.get(URL);
                Iterator<String> it = in_links.iterator();
                while(it.hasNext())
                {
                    String in = it.next();
                    if(outDegrees.containsKey(in) && !in.contentEquals(URL))
                        rank += (PR.get(in)/outDegrees.get(in));
                }
                if(rank > 0)
                    entry.setValue(rank);
            }
        }
        return PR;
	}
	
	public static Map <String,Double> relevanceScore (List<ResultSet> Querywords, double total_number_Doc) throws SQLException
	{
		Map <String,Double> URLS =  new HashMap<String,Double>();
		ResultSet word;
		for(int i = 0; i < Querywords.size(); i++)
		{
			word = Querywords.get(i);
			if(word.first())
			{
				URLS.put(word.getString("url"), 0.0);
			}
			while(word.next())
			{
				URLS.put(word.getString("url"), 0.0);	
			}
		}
				
		for(int i = 0; i < Querywords.size(); i++)
		{
			word = Querywords.get(i);
			int docs_contain_word = 0;
			if(word.last())
			{
				docs_contain_word = word.getRow();
				word.beforeFirst(); 
			}
			while(word.next())
			{
				double tf = word.getInt("freq");
				boolean header = word.getBoolean("header");
				String url = word.getString("url");
				double idf = ranker.IDF(total_number_Doc, docs_contain_word);
				if(header)
					tf = 2 * tf;
				double newValue = URLS.get(url)+ (tf*idf);
				URLS.replace(url, newValue);
				
			}
		}
		return URLS;
	}
	public static Map<String,Double> dateScore(Map <String,Double> scores) throws SQLException
	{
		Set<String> URL =  scores.keySet();
		Iterator<String> it = URL.iterator();
		String query = null;
		if(it.hasNext())
			query = "SELECT date_of_creation, URL FROM phrase_searching WHERE URL = " + it.next();
        while(it.hasNext())
        {
        	String page = it.next();
        	query += " OR URL = " + page ;
        	
        }
        query += " ;" ;
		ResultSet result = Driver.DB.execute_select_query(query);
		
		LocalDate now = LocalDate.now();
		while(result.next())
		{
			String page = result.getString("url");
			LocalDate dateOfURL = result.getDate("date_of_creation").toLocalDate();
			Period diff = Period.between(dateOfURL, now);
			
			double newScore = scores.get(page);
			newScore += 1.0/( diff.getYears()+ (diff.getMonths()/12) + (diff.getDays()/365) );
			
        	scores.replace(page, newScore);
		}
		return scores;
	}
	public static void geographicScore(Map<String, Double> scores, String UserCode) throws IOException, ParseException
	{
//		Map<String, Double> extensions = new HashMap<String, Double>();
//		File exts = new File("extensions.txt");
//		Scanner myReader = new Scanner(exts);
//		while (myReader.hasNextLine()) {
//	        String data = myReader.nextLine();
//	        extensions.put(data.substring(0, 3), Double.parseDouble(data.split(" ")[1]));
//	    }
//	    myReader.close();
	    Map<String, Pair > extensions = new HashMap<String, Pair > ();
		JSONParser parser = new JSONParser();
		JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("countrycode-latlong.json"));
		Iterator<String> keys = jsonObject.keySet().iterator();
		while(keys.hasNext()) {
		    String key = keys.next();
		    if (jsonObject.get(key) instanceof JSONObject) {
		    	JSONObject Lon_Lat = (JSONObject) jsonObject.get(key);
		    	Pair p = new Pair(Double.parseDouble((String) Lon_Lat.get("lat")), Double.parseDouble((String) Lon_Lat.get("long")));
		    	extensions.put('.'+key, p);
		    }
		}
		if(extensions.containsKey(UserCode))
		{
			double lat1 = extensions.get(UserCode).first;
			double lon1 = extensions.get(UserCode).second;
			for(Map.Entry<String, Double> entry: scores.entrySet())
	        {
	            String URL = (String) entry.getKey(); 
	            String ext = URL.substring(URL.lastIndexOf('.'));
	            if(extensions.containsKey(ext))
	            { 
	                double lat2 = extensions.get(ext).first;
	                double lon2 = extensions.get(ext).second;
	                lon1 = Math.toRadians(lon1); 
	                lon2 = Math.toRadians(lon2); 
	                lat1 = Math.toRadians(lat1); 
	                lat2 = Math.toRadians(lat2); 
	                double dlon = lon2 - lon1;  
	                double dlat = lat2 - lat1; 
	                double a = Math.pow(Math.sin(dlat / 2), 2) 
	                         + Math.cos(lat1) * Math.cos(lat2) 
	                         * Math.pow(Math.sin(dlon / 2),2); 
	                      
	                double c = 2 * Math.asin(Math.sqrt(a)); 
	                // Radius of earth in kilometers. Use 3956  // for miles 
	                double r = 6371; 
	                double distance = (c * r);
	                double addScore = 1 - (distance/13.573);
	                if(addScore > 0)
	                {
	                	double oldScore = (double) entry.getValue();
	                    entry.setValue((addScore+oldScore));
	                }
	            }
	            
	        }
		}
	}
	
	public static void main(String[] args) 
	{	
//		double total_number_Doc = 5000;
//		Map<String, Double> sorted = URLS
//        .entrySet()
//        .stream()
//        .sorted(comparingByValue())
//        .collect(
//            toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2,
//                LinkedHashMap::new));
		
		
	}
}