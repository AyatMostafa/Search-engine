package jdbc_demo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import jdbc_demo.Driver;

public class Indexer {
	
	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		
		/*
		 * read from file into vector of strings
		 */
		Vector<String>vec=new Vector();
		
		Map< String,Integer> hm =  new HashMap< String,Integer>();
		Vector<String>header=new Vector();
		String URL=null;
		String date=null;
		wordStopper ws= new wordStopper();
		Stemmer st = new Stemmer();
		Driver dr= new Driver();
		
		//open connection with DB
		Driver.DB.make_connection();
		
		//loop at each word
		for(int i=0;i<vec.size();++i) {
			if(!ws.isStopword(vec.get(i))) {
				
				//if the word new, add it to the map. else, increment its value
				
				String ret= st.stem_input_word(vec.get(i));
				if(hm.containsKey(ret)) {
					hm.put(ret, hm.get(ret) + 1);
				}
				else {
					hm.put(ret, 1);
				}
			}
	   }
	   
	   
	   //make stem and (not necessary stop, will take much time for nothing help) also for header that will make comparing below right
	   
	   for(int i=0;i<header.size();++i) {
		   header.set(i, st.stem_input_word(header.get(i)));
	   }
	   
	   
	   boolean is_header=false;
	   for(Entry<String, Integer> entry : hm.entrySet()) {
		   
		   // is it header or not
		   for(int j=0;j<header.size();++j) {
			   if(header.contains(entry.getKey())) {
				   is_header=true;
				   break;
			   }
		   }
		   
		   // make query
		   //1- get query for the same word and URL
		   //2- if the word is already exits at the same URL, then just update it (get then put)
		   //3- if not insert it as a new record
		   String get_query= "select id from indexer where word ="+entry.getKey()+" and url ="+URL+";"; 
		   ResultSet ret_query= Driver.DB.execute_select_query(get_query);
		   if(ret_query.toString() == null) {
			   //insert
			   String insert_query= "insert into indexer (word, url, header, freq, date_of_creation) values ("+entry.getKey()+", "+
			   URL+", "+is_header+", "+entry.getValue()+", "+date+");";
			   Driver.DB.execute_insert_quere(insert_query);
		   }
		   else {
			   String update_query = "update indexer set word= "+entry.getKey()+", url= "+URL+", header= "+is_header+","
			   		+ " freq= "+entry.getValue()+", date_of_creation= "+date+" where id= "+ret_query.toString()+" ;" ;
			   Driver.DB.execute_update_quere(update_query);
		   }
		   is_header=false;
	   }
		
	}

}
