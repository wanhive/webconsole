<%@page import="com.wanhive.iot.Constants"%>
<%@page import="com.wanhive.iot.util.Role"%>
<%@page import="com.wanhive.iot.bean.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
if (session == null || session.getAttribute("user") == null) {
	response.sendRedirect("index.jsp");
	return;
} else {
	User user = (User) session.getAttribute("user");
	if (user.getType() != Role.ADMINISTRATOR.getValue()) {
		response.sendRedirect("index.jsp");
		return;
	}
}
%>
<jsp:useBean id="user" type="com.wanhive.iot.bean.User" scope="session" />
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title><jsp:include page="include/title.jsp">
		<jsp:param name="pageTitle" value="User management" />
	</jsp:include></title>
<link rel="shortcut icon" href="favicon.ico">
<link rel="stylesheet" type="text/css" href="css/navigation.css">
<link rel="stylesheet" type="text/css" href="css/users.css">
<link rel="stylesheet" type="text/css"
	href="widget/jquery-ui/jquery-ui.min.css">
<link rel="stylesheet" type="text/css"
	href="widget/fontawesome/css/all.min.css">
<link rel="stylesheet" type="text/css"
	href="widget/basic-table/basictable.css">
<script type="text/javascript" src="widget/jquery.min.js"></script>
<script type="text/javascript" src="widget/jquery.timeago.js"></script>
<script type="text/javascript" src="widget/jquery-ui/jquery-ui.min.js"></script>
<script type="text/javascript"
	src="widget/basic-table/jquery.basictable.min.js"></script>
<script type="text/javascript">
	var authorizationToken = '<jsp:getProperty name="user" property="token" />';
	var minSearchKeywordLength =
<%=Constants.getMinSearchKeywordLength()%>
	;
</script>
<script type="text/javascript" src="js/navbar.js"></script>
<script type="text/javascript" src="js/users.js"></script>
</head>
<body>
	<div id="wait-veil">
		<i class="fa fa-spinner fa-spin"
			style="font-size: 64px; color: #330000; margin-top: 100px"></i>
		<h2>Loading....</h2>
	</div>
	<div id="add-form" title="Create new user">
		<p class="validateTips">All form fields are required.</p>

		<form>
			<fieldset>
				<label for="alias">Alias</label> <input type="text" name="alias"
					id="alias" placeholder="Alias"
					class="text ui-widget-content ui-corner-all" required="required">
				<label for="email">Email</label> <input type="email" name="email"
					id="email" placeholder="x@y.com"
					class="text ui-widget-content ui-corner-all" required="required">
				<!-- Allow form submission with keyboard without duplicating the dialog button -->
				<input type="submit" tabindex="-1"
					style="position: absolute; top: -1000px">
			</fieldset>
		</form>
	</div>

	<div id="update-form" title="Update user">
		<p class="validateTips">All form fields are required.</p>

		<form>
			<fieldset>
				<label for="password">Password</label> <input type="password"
					name="password" id="newPassword" placeholder="Password"
					class="text ui-widget-content ui-corner-all"> <label
					for="cnfPassword">Confirm&nbsp;password</label> <input
					type="password" name="cnfPassword" id="cnfPassword"
					placeholder="Confirm password"
					class="text ui-widget-content ui-corner-all"> <label
					for="type">Type</label> <select name="newType" id="newType">
					<option value="3">Administrator</option>
					<!-- <option value="2">Maintainer</option>
					<option value="1">Reporter</option> -->
					<option value="0">User</option>
				</select><br>&nbsp;<br> <label for="newStatus">Status</label> <select
					name="newStatus" id="newStatus">
					<option value="0">Inactive</option>
					<option value="1">Active</option>
					<option value="2">Blocked</option>
				</select> <input type="hidden" name="userUid" id="userUid">
				<!-- Allow form submission with keyboard without duplicating the dialog button -->
				<input type="submit" tabindex="-1"
					style="position: absolute; top: -1000px">
			</fieldset>
		</form>
	</div>

	<!-- TOP NAVIGATION -->
	<div class="topnav" id="topnav">
		<jsp:include page="include/navbar.jsp"></jsp:include>
	</div>
	<!-- HEADER -->
	<header>
		<h1>Users</h1>
	</header>
	<!-- SECTION -->
	<section style="overflow: auto">
		<nav class="left">
			<jsp:include page="include/users-leftnav.jsp"></jsp:include>
		</nav>

		<article class="main">
			<div style="width: 100%">
				<button class="btn-refresh" title="Refresh the list"
					id="reload-data">
					<i class="fa fa-redo"></i>&nbsp;Refresh
				</button>
				<button class="btn-addnew" title="Create new user" id="create-user">
					<i class="fa fa-plus-circle"></i>&nbsp;Create
				</button>
				<label for="limit">&nbsp;|&nbsp;Limit&nbsp;<select
					name="limit" id="limit">
						<option value="10">10</option>
						<option value="50">50</option>
						<option value="100">100</option>
						<option value="200">200</option>
				</select></label> <label for="typeFilter">&nbsp;|&nbsp;Type&nbsp;<select
					name="typeFilter" id="typeFilter">
						<option value="-1">All</option>
						<option value="3">Administrator</option>
						<!-- <option value="2">Maintainer</option>
					<option value="1">Reporter</option> -->
						<option value="0">User</option>
				</select></label> <label for="statusFilter">&nbsp;|&nbsp;Status&nbsp;<select
					name="statusFilter" id="statusFilter">
						<option value="-1">All</option>
						<option value="0">Inactive</option>
						<option value="1">Active</option>
						<option value="2">Blocked</option>
				</select></label> <label for="searchKeyword">&nbsp;|&nbsp;<input type="text"
					name="searchKeyword" id="searchKeyword" placeholder="Keyword"
					size="12">&nbsp;
					<button class="btn-search" title="Search" id="search-users">
						<i class="fa fa-search"></i>&nbsp;Search
					</button></label>
			</div>
			<table id="dataTable" class="ui-widget ui-widget-content auto-index">
				<thead>
					<tr class="ui-widget-header">
						<th style="width: 5%;">SN</th>
						<th id="uidCol" style="cursor: pointer; width: 12%;">Identifier&nbsp;<i
							class="order-caret fa fa-caret-down"></i></th>
						<th id="emailCol" style="cursor: pointer; width: 370px;">Email&nbsp;<i
							class="order-caret fa"></i></th>
						<th class="wh-collapsable" style="width: 18%;">Alias</th>
						<th style="width: 10%;">Type</th>
						<th style="width: 8%;">Status</th>
						<th id="createdOnCol" style="cursor: pointer;">Created&nbsp;<i
							class="order-caret fa"></i></th>
						<th>Action</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
				<tfoot>
				</tfoot>
			</table>
			<jsp:include page="include/paginator.jsp"></jsp:include>
			<p>&nbsp;</p>
		</article>

		<aside class="right">
			<h3><jsp:getProperty name="user" property="alias" /></h3>
			<p>
				<em><jsp:getProperty name="user" property="email" /></em>
			</p>
		</aside>
	</section>
	<p>&nbsp;</p>
	<!-- FOOTER -->
	<footer class="footer"><jsp:include page="include/footer.jsp"></jsp:include></footer>
</body>
</html>