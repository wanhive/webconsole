/**
 * New user sign up
 */

"use strict";
$(document).ready(function() {
	const consentMessage = "Please indicate that you have read and agree to the Terms & Conditions and Privacy Policy.";
	const activationMessage = "Activation code has been sent to your email.";
	const errorMessage = activationMessage;
	$("#logonform").on("submit", function() {
		if (!$("#agreement").prop('checked')) {
			alert(consentMessage);
			return false;
		}
		const email = $("#email").val();
		const alias = $("#alias").val();
		const captcha = $("#recaptcha-response").val();
		const settings = {
			"async": true,
			"crossDomain": true,
			"url": "api/user",
			"method": "POST",
			"headers": {
				"Content-Type": "application/x-www-form-urlencoded",
				"cache-control": "no-cache"
			},
			"data": {
				"email": email,
				"alias": alias,
				"captcha": captcha
			}
		};

		$.ajax(settings).done(function(_) {
			alert(activationMessage);
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
