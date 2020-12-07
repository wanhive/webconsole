/**
 * Request for existing user account recovery
 */

$(document).ready(function() {
	var recoveryMessage = "Activation code has been sent to your email.";
	var errorMessage = recoveryMessage;
	$("#passform").on("submit", function() {
		var email = $("#email").val();
		var captcha = $("#recaptcha-response")
			.val();
		var settings = {
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
