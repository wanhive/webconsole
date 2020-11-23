<%@page import="com.wanhive.iot.Constants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<ul>
	<li><a href="#"
		title="Domains provide logical isolation between the things. Only those things which belong to the same domain are allowed to communicate.">What
			is a domain?</a></li>
	<li><a href="#"
		title="There is no restriction on the number of domains.">How many
			domains are allowed?</a></li>
	<li><a href="#"
		title="The total number of things in a domain is limited to <%=Constants.getMaxThingsPerDomain()%> things. There is no restriction on the total number of things.">How
			many things are allowed?</a></li>
</ul>