
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"
     import="java.util.*,jdbc_demo.URL_Data, jdbc_demo.cacheFormer" 
     import= "java.lang.Math"%>
<!DOCTYPE html>
<html>

<head>
<meta charset="ISO-8859-1">
<title>Search Engine</title>
<link rel= "stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.5.0/css/bootstrap-grid.css"/>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">



</head>

<body>
<jsp:include page="/Home.jsp"/>
<%  
String spageid = request.getParameter("page"); 
int pageid = Integer.parseInt(spageid);
double Size = cacheFormer.GetNumberOfURL();
double totalSize = Math.ceil(Size/10.0);

String  str = cacheFormer.getCacheData(pageid); 
%>
<%= str %>
<% 
for(int i = 1 ; i <= totalSize; i++)
{  	
    out.print("<a style='margin: 10px' href=SearchResults.jsp?page=" + i + "> "+ i +" </a>");  
}
 
%>

<script type = "text/javascript">
    function doSomething() {
   // 	var url = event.srcElement.getAttribute('href');
    	var doc = document.getElementById("0");
    	doc.action = "userRanks";
    	doc.method = "GET";
    	doc.submit();
    	//var url = 
   // 	var serverUrl ="http://localhost:8081/SearchEngine/userRanks";
  //  	var params = "url= " + url;
//    	var http = new XMLHttpRequest();
    	//http.open("GET", serverUrl+"?"+params, true);
    	//http.send( null );
    }
</script>	

</body>
</html>


