<%@page import="com.wanhive.iot.Constants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
if (session == null || session.getAttribute("user") == null) {
	response.sendRedirect("index.jsp");
	return;
}

long domainUid = 0;
try {
	domainUid = Long.parseLong(request.getParameter("domainUid"));
} catch (Exception e) {
	response.sendRedirect("index.jsp");
	return;
}
%>
<jsp:useBean id="user" type="com.wanhive.iot.bean.User" scope="session" />
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title><jsp:include page="include/title.jsp">
		<jsp:param name="pageTitle" value="Things" />
	</jsp:include></title>
<link rel="shortcut icon" href="favicon.ico">
<link rel="stylesheet" type="text/css" href="css/navigation.css">
<link rel="stylesheet" type="text/css" href="css/thingpage.css">
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
	var domainUid =
<%=domainUid%>
	;
	var maxPasswordHashRounds =
<%=Constants.getMaxPasswordHashRounds()%>
	;
	var maxThingsPerDomain =
<%=Constants.getMaxThingsPerDomain()%>
	;
	var minSearchKeywordLength =
<%=Constants.getMinSearchKeywordLength()%>
	;
</script>
<script type="text/javascript" src="js/navbar.js"></script>
<script type="text/javascript" src="js/thing.js"></script>
</head>
<body>
	<div id="wait-veil">
		<i class="fa fa-spinner fa-spin"
			style="font-size: 64px; color: #330000; margin-top: 100px"></i>
		<h2>Loading....</h2>
	</div>
	<div id="add-form" title="Create new thing">
		<p class="validateTips">Name is required.</p>

		<form>
			<fieldset>
				<label for="name">Name</label> <input type="text" name="name"
					id="newName" placeholder="Thing's name"
					class="text ui-widget-content ui-corner-all" required="required">
				<label for="type">Type</label><select name="type" id="newType">
					<option value="1">Actuator</option>
					<option value="2">Controller</option>
					<option value="0">Master</option>
					<option value="4">Processor</option>
					<option value="3">Sensor</option>
				</select><br>&nbsp;<br> <label for="salt">Salt</label>
				<textarea name="salt" id="newSalt"
					placeholder="Salt for authentication"
					class="text ui-widget-content ui-corner-all"></textarea>
				<label for="verifier">Verifier</label>
				<textarea name="verifier" id="newVerifier"
					placeholder="Verifier for authentication"
					class="text ui-widget-content ui-corner-all"></textarea>
				<label for="password">Password</label> <input type="password"
					name="password" id="newPassword" placeholder="Password"
					class="text ui-widget-content ui-corner-all"> <label
					for="cnfPassword">Confirm&nbsp;password</label> <input
					type="password" name="cnfPassword" id="cnfPassword"
					placeholder="Confirm password"
					class="text ui-widget-content ui-corner-all"> <label
					for="rounds">Rounds</label> <input type="number" name="rounds"
					id="newRounds" placeholder="Hash rounds" value="1" min="1"
					max="<%=Constants.getMaxPasswordHashRounds()%>"
					class="text ui-widget-content ui-corner-all" required="required">
				<input type="hidden" name="thingUid" id="thingUid"> <input
					type="hidden" name="computeVerifier" id="computeVerifier">
				<!-- Allow form submission with keyboard without duplicating the dialog button -->
				<input type="submit" tabindex="-1"
					style="position: absolute; top: -1000px">
			</fieldset>
		</form>
	</div>

	<div id="dialog-confirm" title="Delete the selected thing">
		<p>
			<span class="ui-icon ui-icon-alert"
				style="float: left; margin: 12px 12px 20px 0;"></span> Selected
			thing(s) and all the associated records will be permanently deleted
			and cannot be recovered. Are you sure?
		</p>
	</div>

	<!-- TOP NAVIGATION -->
	<div class="topnav" id="topnav">
		<jsp:include page="include/navbar.jsp"></jsp:include>
	</div>
	<!-- HEADER -->
	<header>
		<h1>Things</h1>
	</header>
	<!-- SECTION -->
	<section style="overflow: auto">
		<nav class="left">
			<jsp:include page="include/thing-leftnav.jsp"></jsp:include>
		</nav>

		<article class="main">
			<div style="width: 100%">
				<button class="btn-refresh" title="Refresh the list"
					id="reload-data">
					<i class="fa fa-redo"></i>&nbsp;Refresh
				</button>
				<button class="btn-addnew" title="Create new thing"
					id="create-thing">
					<i class="fa fa-plus-circle"></i>&nbsp;Create
				</button>
				<label for="limit">&nbsp;|&nbsp;Limit&nbsp;<select
					name="limit" id="limit">
						<option value="10">10</option>
						<option value="50">50</option>
						<option value="100">100</option>
						<option value="200">200</option>
				</select></label> <label for="searchKeyword">&nbsp;|&nbsp;<input type="text"
					name="searchKeyword" id="searchKeyword" placeholder="Keyword"
					size="12">&nbsp;
					<button class="btn-search" title="Search" id="search-thing">
						<i class="fa fa-search"></i>&nbsp;Search
					</button></label>
			</div>
			<table id="dataTable" class="ui-widget ui-widget-content auto-index">
				<thead>
					<tr class="ui-widget-header">
						<th style="width: 5%;">SN</th>
						<th id="uidCol" style="cursor: pointer; width: 15%;">Identifier&nbsp;<i
							class="order-caret fa"></i></th>
						<th id="nameCol" style="cursor: pointer; width: 40%;">Name&nbsp;<i
							class="order-caret fa"></i></th>
						<th width="10%" id="typeCol" style="width: 10%;">Type</th>
						<th id="createdOnCol" style="cursor: pointer; width: 12%;">Created&nbsp;<i
							class="order-caret fa fa-caret-down"></i></th>
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

		<aside class="right"></aside>
	</section>
	<p>&nbsp;</p>
	<!-- FOOTER -->
	<footer class="footer">
		<jsp:include page="include/footer.jsp"></jsp:include>
	</footer>
</body>
</html>