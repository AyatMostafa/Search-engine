package jdbc_demo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*; 

public class testing{
    public static class Pairc {
    	public String first,second;
        public Pairc()
        {
            first = "";
            second = "";
        }
        public Pairc(String a,String b)
        {
            first = a;
            second = b;
        }
    }
    
    public static Map <String,Pair> pageRank(ArrayList<Pairc> connections) 
	{
		Map <String,Pair> PR =  new HashMap<String,Pair>();
		
        Map<String,HashSet<String>> links_to_page = new HashMap<String, HashSet<String>>();
        Map<String,Integer> outDegree = new HashMap<String, Integer>();
        
        for(int i = 0 ; i < connections.size() ; i++)
        {
            if(!links_to_page.containsKey(connections.get(i).second))
                links_to_page.put(connections.get(i).second, new HashSet<String>(Arrays.asList(connections.get(i).first)));
            else
            {
                HashSet<String>temp = links_to_page.get(connections.get(i).second);
                temp.add(connections.get(i).first);
                links_to_page.put(connections.get(i).second, temp);
            }
            PR.put(connections.get(i).first, new Pair(0.0,0.0));
            PR.put(connections.get(i).second, new Pair(0.0,0.0));
            if(outDegree.containsKey(connections.get(i).first))
                outDegree.put(connections.get(i).first, outDegree.get(connections.get(i).first)+1);
            else
                outDegree.put(connections.get(i).first,1);
        }
        
        PR.replaceAll((k,v) ->   new Pair((1.0/PR.size()) , 1.0/PR.size()) );
        
        int iterations = 1;
        for(int i = 0 ; i < iterations ; i++)
        {
            for(Map.Entry<String, Pair> entry: PR.entrySet())
            {
            	double rank = 0;
                String URL = (String) entry.getKey(); 
                if(links_to_page.get(URL) != null)
                {
	                HashSet<String> in_links = links_to_page.get(URL);
	                Iterator<String> it = in_links.iterator();
	                while(it.hasNext())
	                {
	                    String in = it.next();
	                    if(outDegree.containsKey(in) && !in.contentEquals(URL))
	                        rank += (PR.get(in).first/outDegree.get(in));
	                }
	                if(rank > 0)
	                    entry.setValue(new Pair(entry.getValue().first, rank));
                }
            }
            if(i == iterations-1)
            	PR.replaceAll((k,v) -> new Pair(v.second, v.second));
        }
        return PR;
	}
    public static void queryProcessor(String searchQuery,String Country ,String searchType) throws SQLException
    {
    	boolean isPhrase = false;
        Character firstChar,lastChar;
        // Pre-Processing the search query

        firstChar = searchQuery.charAt(0);
        lastChar =  searchQuery.charAt(searchQuery.length()-1);
        if (firstChar.equals("\"")&&lastChar.equals("\"")) isPhrase = true;
        searchQuery.replace("\"", "");
        String [] words = searchQuery.split("\\s+");


        jdbc_demo.wordStopper ws= new jdbc_demo.wordStopper();
        jdbc_demo.Stemmer st = new jdbc_demo.Stemmer();

        //open connection with DB
        Driver.DB.make_connection();
        List<ResultSet> toRanker = new ArrayList<>();
        //loop at each word
        if(!isPhrase) {
            for (int i = 0; i < words.length; ++i) {
                if (!ws.isStopword(words[i])) {
                    String ret = st.stem_input_word(words[i]);
                    String get_query = "select * from indexer where word =" + ret + ";";
                    ResultSet ret_query = Driver.DB.execute_select_query(get_query);
                    ret_query.first();
                    toRanker.add(ret_query);
                }
            }
        }
        else {
            String phrase = "", firstWord = "";
            boolean gotFirstWord = false;
            for (int i = 0; i < words.length; ++i) {
                if (!ws.isStopword(words[i])) {
                    phrase += words[i];
                    if (!gotFirstWord) {
                        firstWord = words[i];
                        gotFirstWord = true;
                    }
                }
            }
            String phrase_query = "select * from indexer where word =" + firstWord + ";";
            ResultSet phrase_res = Driver.DB.execute_select_query(phrase_query);
            if (!phrase_res.next()) {                        // No Results
                System.out.println("No records found");    //==> to be modified to resemble the way data returns from the ranker
            } else {
                do {
                    String phraseURL = phrase_res.getString("url");
                    String getURLContent = "select * from urlTable where url =" + phraseURL + " and content like '%" + phrase + "%';";
                    ResultSet urlContents = Driver.DB.execute_select_query(getURLContent);
                    if (!urlContents.next())
                        phrase_res.deleteRow();
                    // Get data from the current row and use it
                } while (phrase_res.next());
                phrase_res.first();
                toRanker.add(phrase_res);
            }
        }
    }

    public static void main(String []args){
        List<Pairc> connections = new ArrayList<Pairc>();
        
        connections.add( new Pairc("B", "A"));
        connections.add(new Pairc("B", "C"));
        connections.add( new Pairc("D", "B"));
        connections.add( new Pairc("D", "A"));
        connections.add( new Pairc("D", "C"));
        connections.add( new Pairc("C", "A"));
        
//        System.out.println(connect.get(1).first );
//        System.out.println( connect.get(1).second);
//        System.out.println( connect.size());
        Map<String, Pair> pp = pageRank((ArrayList<Pairc>) connections);
        
        for(Map.Entry<String, Pair> m : pp.entrySet())
        	System.out.println(m.getValue().second);
        
    }
}
