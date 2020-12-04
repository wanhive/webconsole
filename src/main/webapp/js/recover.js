/**
 * 
 */

$(document)
	.ready(
		function() {
			$("#passform")
				.on(
					"submit",
					function() {
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

						$
							.ajax(settings)
							.done(
								function(response) {
									alert("Activation code has been sent to your email.");
								})
							.fail(
								function() {
									alert("Activation code has been sent to your email.");
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