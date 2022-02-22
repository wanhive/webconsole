/**
 * Request for existing user account recovery
 */

"use strict";
$(document).ready(function() {
	const recoveryMessage = "Activation code has been sent to your email.";
	const errorMessage = recoveryMessage;
	$("#passform").on("submit", function() {
		const email = $("#email").val();
		const captcha = $("#recaptcha-response").val();
		const settings = {
			"async": true,
			"crossDomain": true,
			"url": "api/user/challenge?email="
				+ email + "&captcha="
				+ captcha,
			"method": "GET",
			"headers": {
				"Content-Type": "application/x-www-form-urlencoded",
				"cache-control": "no-cache"
			},
			"processData": false,
			"data": ""
		}

		$.ajax(settings).done(function(_) {
			alert(recoveryMessage);
		}).fail(function() {
			alert(errorMessage);
		}).always(function() {
		});

		return false;
	});

	$(document).ajaxStart(function() {
		$("#wait-veil").css("display", "block");
	});
	$(document).ajaxComplete(function() {
		$("#wait-veil").css("display", "none");
	});
});
