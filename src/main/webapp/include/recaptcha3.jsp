<%@page import="com.wanhive.iot.Constants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	if (Constants.isCaptchaEnabled()) {
%>
<script
	src='https://www.google.com/recaptcha/api.js?render=<%=Constants.getReCaptchaSiteKey()%>'></script>
<script>
	grecaptcha.ready(function() {
		grecaptcha.execute('<%=Constants.getReCaptchaSiteKey() %>', {
			action : '<%=request.getParameter("reCaptchaAction") %>'
		}).then(
				function(token) {
					var recaptchaResponse = document
							.getElementById('recaptcha-response');
					recaptchaResponse.value = token;
				});
	});
</script>
<%
	}
%>