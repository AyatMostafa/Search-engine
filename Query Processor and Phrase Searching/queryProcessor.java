package jdbc_demo;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import jdbc_demo.Driver;
import javax.servlet.*;
import javax.servlet.http.*;

public class queryProcessor extends HttpServlet{

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String searchQuery = (request.getParameter("query"));
        try {
            doWork(searchQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void doWork(String searchQuery) throws SQLException{
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
}
