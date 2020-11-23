/**
 * 
 */
$(document)
	.ready(
		function() {
			var errorMessage = "Request denied.";
			function requestSecurityChallenge() {

			}

			$("#logonform")
				.on(
					"submit",
					function() {
						if (!$("#agreement").prop('checked')) {
							alert("Please indicate that you have read and agree to the Terms & Conditions and Privacy Policy.");
							return false;
						}
						var email = $("#email").val();
						var alias = $("#alias").val();
						var captcha = $("#recaptcha-response")
							.val();
						var settings = {
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
						}

						$
							.ajax(settings)
							.done(
								function(response) {
									alert("Activation code has been sent to your email.");
								}).fail(function() {
									alert(errorMessage);
								}).always(function() {
									// alert("complete");
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