package jdbc_demo;

import java.sql.ResultSet;
import java.sql.SQLException;

public class URL_Data {

	private String URL;  
	private  String title;  
	private  String partOfContent;  
	
	public URL_Data(String url,String data,String content){  
		URL=url;  
		title =data;  
		partOfContent=content;  
		
    } 
    public static URL_Data getRecords(String url){    
    	URL_Data urlObj=null;				
    	try {
    		//Driver.DB.make_connection();
			String getURLContent = "SELECT `glance` FROM `phrase_searching` WHERE url = \"" + url + "\";";
            ResultSet urlContents = Driver.DB.execute_select_query(getURLContent);
            
            if (!urlContents.next()) {
                System.out.println("No record found");
            }
            else{
                String glance = urlContents.getString("glance");
         //       System.out.println(glance);
                String [] data = glance.split("\\|");
                
                String title = data[0];
                String partOfContent = "";
                for(int i = 1; i < data.length ; i++)
                {
                	partOfContent +=  data[i] + " ";
                }
                urlObj=new URL_Data(url,title,partOfContent);  
               
            }  		
	        } catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
		return urlObj;  
    } 
    
    public String getURL(){  
        
        return URL;  
    } 
    
    public  String getTitle(){  
        return title;  
    } 
    
    public  String getpartOfContent(){  
        return partOfContent;  
    } 
    
}
