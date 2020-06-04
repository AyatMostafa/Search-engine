
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

<style>
.column {
  float: left;
  width: 25%;
  padding: 2%;
}

/* Clearfix (clear floats) */
.row::after {
  content: "";
  clear: both;
  display: table;
}
@media screen and (max-width: 500px) {
  .column {
    width: 100%;
  }
</style>

</head>

<body>
<jsp:include page="/Home.jsp"/>
<%  
String spageid = request.getParameter("page"); 
int pageid = Integer.parseInt(spageid);
double Size = cacheFormer.GetNumberOfURL();
double totalSize = Math.ceil(Size/12.0);
String  str = cacheFormer.getCacheImage(pageid); 
%>
<%= str %>
<div>
<%
for(int i = 1 ; i <= totalSize; i++)
{  	
    out.print("<a style='margin: 10px' href=imageResults.jsp?page=" + i + "> "+ i +" </a>");  
}
%>
</div>

	

<script type = "text/javascript">
    function doSomething() {
    	var url =event.srcElement.getAttribute('href');
    	console.log(url);
    	var serverUrl ="http://localhost:8081/SearchEngine/userRanks";
    	var params = "url= " + url;
    	var http = new XMLHttpRequest();
    	console.log(serverUrl+"?"+params);
    	http.open("GET", serverUrl+"?"+params, true);
    	http.send( null );
    }
</script>	

</body>
</html>