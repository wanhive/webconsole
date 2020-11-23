<%@page import="com.wanhive.iot.util.Role"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:useBean id="user" type="com.wanhive.iot.bean.User" scope="session" />

<a
	href=<%=user.getType() == Role.ADMINISTRATOR.getValue() ? "\"users.jsp\"" : "\"domain.jsp\""%>>Home</a>

<div class="dropdown">
	<button class="dropbtn">
		Account <i class="fa fa-caret-down"></i>
	</button>
	<div class="dropdown-content">
		<a href="index.jsp">Sign out</a> <a href="settings.jsp">Settings</a>
	</div>
</div>

<a href="javascript:void(0);" class="icon" onclick="toggleNavBar()">&#9776;</a>