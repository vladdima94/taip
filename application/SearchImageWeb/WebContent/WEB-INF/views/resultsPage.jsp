<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page import="org.json.simple.JSONArray" %>
    <%@ page import="org.json.simple.JSONObject" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Image Search</title>
<style>
	.results{
		width: 88%;
		display: inline-block;
		padding: 25px 0px 0px 0px;
	}
	img{
		float: left;
		margin: 0px 0px 25px 25px;
	}
</style>
</head>
<body>
	<h1>Results</h1>
	<form action="<%=request.getContextPath()%>/main/" method="POST">
		<input type="submit" value= "Search"/>
	</form>
	
	<center>
		<div class="results">
	<%
	JSONArray images = (JSONArray) request.getAttribute("resultsList");
	int size = images.size();
	for(int i = 0; i < size; ++i)
	{
		out.append("<img src=\"").append((String)((JSONObject)images.get(i)).get("link")).append("\" width=\"304\" height=\"236\"/>");
	}
	%>
			</div>
	</center>
</body>
</html>