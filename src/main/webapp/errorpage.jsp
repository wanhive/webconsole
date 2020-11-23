<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><jsp:include page="include/title.jsp">
		<jsp:param name="pageTitle" value="Error" />
	</jsp:include></title>
<link rel="shortcut icon" href="favicon.ico">
<style type="text/css">
body {
	margin: 5px;
	padding: 5px;
	background-color: #e0e0e0;
	color: #222;
	font-family: "HelveticaNeue-Light", "Helvetica Neue Light",
		"Helvetica Neue", Helvetica, Arial, "Lucida Grande", sans-serif;
	font-size: 15px;
}
</style>
</head>
<body>
	<h1>Something went wrong. Please try again after a while.</h1>
	<br>
	<a href="index.jsp"><strong>Back to the login page.</strong></a>
</body>
</html>