<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	if (session == null || session.getAttribute("user") == null) {
	response.sendRedirect("index.jsp");
	return;
}
%>
<jsp:useBean id="user" type="com.wanhive.iot.bean.User" scope="session" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title><jsp:include page="include/title.jsp">
		<jsp:param name="pageTitle" value="Personal settings" />
	</jsp:include></title>
<link rel="shortcut icon" href="favicon.ico">
<link rel="stylesheet" type="text/css" href="css/navigation.css">
<link rel="stylesheet" type="text/css" href="css/settingspage.css">
<link rel="stylesheet" type="text/css"
	href="widget/jquery-ui/jquery-ui.min.css">
<link rel="stylesheet" type="text/css"
	href="widget/fontawesome/css/all.min.css">
<script type="text/javascript" src="widget/jquery.min.js"></script>
<script type="text/javascript" src="widget/jquery.timeago.js"></script>
<script type="text/javascript" src="widget/jquery-ui/jquery-ui.min.js"></script>
<script type="text/javascript">
	var authorizationToken = '<jsp:getProperty name="user" property="token" />';
</script>
<script type="text/javascript" src="js/navbar.js"></script>
<script type="text/javascript" src="js/settings.js"></script>
</head>
<body>

	<div id="wait-veil">
		<i class="fa fa-spinner fa-spin"
			style="font-size: 64px; color: #330000; margin-top: 100px"></i>
		<h2>Loading....</h2>
	</div>

	<div id="password-form" title="Change password">
		<p class="validateTips">All form fields are required.</p>

		<form>
			<fieldset>
				<label for="currentPassword">Current&nbsp;password</label> <input
					type="password" name="currentPassword" id="currentPassword"
					placeholder="Current password"
					class="text ui-widget-content ui-corner-all" required="required">
				<label for="password">New&nbsp;password</label> <input
					type="password" name="password" id="newPassword"
					placeholder="New password"
					class="text ui-widget-content ui-corner-all"> <label
					for="cnfPassword">Confirm&nbsp;password</label> <input
					type="password" name="cnfPassword" id="cnfPassword"
					placeholder="Confirm password"
					class="text ui-widget-content ui-corner-all">
				<!-- Allow form submission with keyboard without duplicating the dialog button -->
				<input type="submit" tabindex="-1"
					style="position: absolute; top: -1000px">
			</fieldset>
		</form>
	</div>

	<div id="dialog-confirm" title="Update settings">
		<p>
			<span class="ui-icon ui-icon-alert"
				style="float: left; margin: 12px 12px 20px 0;"></span> Update
			settings?
		</p>
	</div>

	<!-- TOP NAVIGATION -->
	<div class="topnav" id="topnav">
		<jsp:include page="include/navbar.jsp"></jsp:include>
	</div>
	<!-- HEADER -->
	<header>
		<h1>Personal settings</h1>
	</header>
	<!-- SECTION -->
	<section style="overflow: auto">
		<nav class="left">&nbsp;</nav>

		<article class="main">
			<table id="dataTable" class="ui-widget ui-widget-content">
				<tr>
					<td><strong>Alias</strong>&nbsp;</td>
					<td><jsp:getProperty name="user" property="alias" /></td>
				</tr>
				<tr>
					<td><strong>Email</strong>&nbsp;</td>
					<td><jsp:getProperty name="user" property="email" /></td>
				</tr>
				<tr>
					<td><strong>Active since</strong>&nbsp;</td>
					<td><time class="timeago"
							datetime='<jsp:getProperty name="user" property="createdOn" />'
							title='<jsp:getProperty name="user" property="createdOn" />'><jsp:getProperty
								name="user" property="createdOn" /></time></td>
				</tr>
				<tr>
					<td><strong>Password</strong>&nbsp;</td>
					<td>
						<button id="change-password" title="Change password">Change</button>
					</td>
				</tr>
			</table>
		</article>

		<!--  <aside class="right">&nbsp;</aside>  -->
	</section>
	<!-- FOOTER -->
	<footer class="footer"><jsp:include page="include/footer.jsp"></jsp:include></footer>
</body>
</html>