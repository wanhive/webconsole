<%@page import="com.wanhive.iot.Constants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
if (!Constants.isSignUpAllowed()) {
	response.sendRedirect("index.jsp");
	return;
}
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><jsp:include page="include/title.jsp">
		<jsp:param name="pageTitle" value="Create account" />
	</jsp:include></title>
<link rel="shortcut icon" href="favicon.ico">
<link rel="stylesheet" type="text/css" href="css/newuserpage.css">
<link rel="stylesheet" type="text/css"
	href="widget/jquery-ui/jquery-ui.min.css">
<link rel="stylesheet" type="text/css"
	href="widget/fontawesome/css/all.min.css">
<script type="text/javascript" src="widget/jquery.min.js"></script>
<script type="text/javascript" src="widget/jquery.timeago.js"></script>
<script type="text/javascript" src="widget/jquery-ui/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/newuser.js"></script>
<jsp:include page="include/recaptcha3.jsp">
	<jsp:param name="reCaptchaAction" value="newuser" />
</jsp:include>
</head>
<body>
	<div id="wait-veil">
		<i class="fa fa-spinner fa-spin"
			style="font-size: 64px; color: #330000; margin-top: 100px"></i>
		<h2>Loading....</h2>
	</div>
	<!-- start header div -->
	<div class="header">
		<h3>Sign up</h3>
	</div>
	<!-- end header div -->

	<!-- start wrap div -->
	<div class="wrap">
		<form id="logonform">
			<fieldset>
				<legend>Create account</legend>
				<input type="email" name="email" id="email" required="required"
					autofocus="autofocus" placeholder="Email Address"><br>
				<input type="text" name="alias" id="alias" required="required"
					placeholder="Alias"> <br> <span><input
					type="checkbox" name="agreement" value="agree" id="agreement">I
					have read and <strong>agree</strong> to the <a href="license.jsp"
					target="_blank">Terms &amp; Conditions</a> and <a
					href="privacy.jsp" target="_blank">Privacy Policy</a>.</span> <br>&nbsp;<br>
				<input type="hidden" name="captcha" id="recaptcha-response">
				<input type="submit" name="submit" value="Sign up">
			</fieldset>
		</form>
		<!-- end sign up form -->
		<p>
			<a href="activate.jsp">Activate account</a><br> <a
				href="index.jsp">Go to home page</a>
		</p>
	</div>
	<!-- end wrap div -->
	<!-- FOOTER -->
	<footer class="footer"><jsp:include page="include/footer.jsp"></jsp:include></footer>
</body>
</html>