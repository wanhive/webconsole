<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><jsp:include page="include/title.jsp">
		<jsp:param name="pageTitle" value="Recover account" />
	</jsp:include></title>
<link rel="shortcut icon" href="favicon.ico">
<link rel="stylesheet" type="text/css" href="css/recoverpage.css">
<link rel="stylesheet" type="text/css"
	href="widget/jquery-ui/jquery-ui.min.css">
<link rel="stylesheet" type="text/css"
	href="widget/fontawesome/css/all.min.css">
<script type="text/javascript" src="widget/jquery-3.3.1.min.js"></script>
<script type="text/javascript" src="widget/jquery.timeago.js"></script>
<script type="text/javascript" src="widget/jquery-ui/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/recover.js"></script>
<jsp:include page="include/recaptcha3.jsp">
	<jsp:param name="reCaptchaAction" value="recover" />
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
		<h3>Recover</h3>
	</div>
	<!-- end header div -->

	<!-- start wrap div -->
	<div class="wrap">
		<form id="passform">
			<fieldset>
				<legend>Enter your email address</legend>
				<input type="email" name="email" id="email" required="required"
					autofocus="autofocus" placeholder="Email Address"><br>
				<input type="hidden" name="captcha" id="recaptcha-response">
				<input type="submit" name="submit" value="Generate token">
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