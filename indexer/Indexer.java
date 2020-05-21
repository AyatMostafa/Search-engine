package jdbc_demo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
		Vector<String>vec=new Vector<String>();
		
		Map< String,Integer> hm =  new HashMap< String,Integer>();
		Vector<String>header=new Vector<String>();
		Vector<String>images_URLs=new Vector<String>();
		String URL=null;
		String date=null;
		wordStopper ws= new wordStopper();
		Stemmer st = new Stemmer();
		Driver dr= new Driver();
		
		
		//dummy inputs
		
		vec.add("I");
		vec.add("love");
		vec.add("football");
		vec.add("very");
		vec.add("much");
		vec.add("so");
		vec.add("I'm");
		vec.add("a");
		vec.add("footballer");
		
		header.add("loving");
		header.add("football");
		
		URL="mkmk";
		
		String document="";
		//open connection with DB
		Driver.DB.make_connection();
		wordStopper.Stopwords();
		//loop at each word
		for(int i=0;i<vec.size();++i) {
			if(!wordStopper.isStopword(vec.get(i))) {
				
				//if the word new, add it to the map. else, increment its value
				
				String ret= st.stem_input_word(vec.get(i));
				if(hm.containsKey(ret)) {
					hm.put(ret, hm.get(ret) + 1);
				}
				else {
					hm.put(ret, 1);
				}
				document+=ret;
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
		   String get_query= "select `id` from `indexer` where word = \""+entry.getKey()+"\" and url = \""+URL+"\";"; 
		   ResultSet ret_query= Driver.DB.execute_select_query(get_query);
		   if(!ret_query.isBeforeFirst()) {
			   //insert
			   String insert_query= "insert into `indexer` (`word`, `url`, `header`, `freq`, `date_of_creation`) values ( \""+entry.getKey()+"\" , \""+
			   URL+"\" , "+is_header+" , "+(double)entry.getValue()/vec.size()+" , "+date+" );";
			   Driver.DB.execute_insert_quere(insert_query);
		   }
		   else {
			   String update_query = "update `indexer` set `word`= \""+entry.getKey()+"\", `url` = \""+URL+"\", `header` = "+is_header+","
			   		+ " `freq` = "+(double)entry.getValue()/vec.size()+", `date_of_creation` = \""+date+"\" where `id` = "+ret_query.getString("id")+" ;" ;
			   Driver.DB.execute_update_quere(update_query);
		   }
		   is_header=false;
	   }
	   
	 //----------------------------------------------------------------------------------------------------------------//
		
	//phrase searching table manipulation
	// make query
	   //1- get query for the same URL
	   //2- if the URL is already exits at the same URL, then just update it (get then put)
	   //3- if not insert it as a new record
	   String get_query= "select `id` from `phrase_searching` where url = \""+URL+"\";"; 
	   ResultSet ret_query= Driver.DB.execute_select_query(get_query);
	   if(!ret_query.isBeforeFirst()) {
		   //insert
		   String insert_query= "insert into `phrase_searching` (`url`, `document`) values ( \""+
		   URL+"\" , \""+document+"\" );";
		   Driver.DB.execute_insert_quere(insert_query);
	   }
	   else {
		   String update_query = "update `phrase_searching` set `url` = \""+URL+"\", `document` = \""+document+"\"  where `id` = "+ret_query.getString("id")+" ;" ;
		   Driver.DB.execute_update_quere(update_query);
	   }
		
		//----------------------------------------------------------------------------------------------------------------//
		
	   //images_url table manipulation
	   
//	   read images urls from the file where page url is pointing to, (if the images_urls are in the same file as above then make
//			  this read above)
//	   BufferedReader Reader = null;
//	    try {
//	        String row;
//	        Reader = new BufferedReader(new FileReader("images_URLS.txt"));
//	        if ((row = Reader.readLine()) != null){
//	            String[] data = row.split("--");
//	            //String insert_query= "insert into RankTable (from, to) values ("+data[0]+", "+data[1]+");";
//	            //Driver.DB.execute_insert_quere(insert_query);
//	   			images_URLs.add(data);
//	        }
//	
//	    } catch (FileNotFoundException e1) {
//	        e1.printStackTrace();
//	    }
//	    catch (IOException e) {
//	        e.printStackTrace();
//	    } catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	   
	// make query
	   //1- get query for the same URL and image URL
	   //2- if the image URL is already exits at the same URL, then just update it (get then put)
	   //3- if not insert it as a new record
	   for(int i=0;i<images_URLs.size();++i) {
		   String get_query1= "select `id` from `images_urls` where page_url = \""+URL+"\" and image_url= \""+images_URLs.get(i)+"\";"; 
		   ResultSet ret_query1= Driver.DB.execute_select_query(get_query1);
		   if(!ret_query1.isBeforeFirst()) {
			   //insert
			   String insert_query= "insert into `images_urls` (`page_url`, `image_url`) values ( \""+
			   URL+"\" , \""+images_URLs.get(i)+"\" );";
			   Driver.DB.execute_insert_quere(insert_query);
		   }
	   }
	   
	   //-----------------------------------------------------------------------------------------------------------------//
		
		//adding indegree and outdegree table
		
//		BufferedReader Reader = null;
//		    try {
//		        String row;
//		        Reader = new BufferedReader(new FileReader("myUrls.txt"));
//		        if ((row = Reader.readLine()) != null){
//		            String[] data = row.split("--");
//		            String insert_query= "insert into RankTable (from, to) values ("+data[0]+", "+data[1]+");";
//		            Driver.DB.execute_insert_quere(insert_query);
//		        }
//		
//		    } catch (FileNotFoundException e1) {
//		        e1.printStackTrace();
//		    }
//		    catch (IOException e) {
//		        e.printStackTrace();
//		    } catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		// adding images see new table or at the same table
		// store freq/length of doc
		
	}

}
