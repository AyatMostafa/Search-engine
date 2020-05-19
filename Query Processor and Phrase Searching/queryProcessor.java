package controller;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
//import jdbc_demo.Driver;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class queryProcessor
 */
@WebServlet("/queryProcessor")
public class queryProcessor extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		
		String searchQuery = (request.getParameter("query"));
		String Country = (request.getParameter("country")); //string format ( ['country name','logo'] )
		String SearchType = (request.getParameter("searchType")); // options sent (Text,Image)
		
		System.out.println(searchQuery);
		System.out.println(Country);
		System.out.println(SearchType);
	   /*try {
            doWork(searchQuery , Country, SearchType);
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
	   
	   response.sendRedirect("searchPage.jsp");
	}
	
	
/*
    public void doWork(String searchQuery,String Country ,String searchType) throws SQLException{
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
   */
    public queryProcessor() {
       
    }


}
