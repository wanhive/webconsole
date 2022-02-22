/**
 * User account management
 */

"use strict";
$(document).ready(function() {
	//Service request error message
	const errorMessage = "Request denied.";

	const currentPassword = $("#currentPassword");
	const newPassword = $("#newPassword");
	const cnfPassword = $("#cnfPassword");

	const allFields = $([]).add(currentPassword).add(newPassword)
		.add(cnfPassword);
	const tips = $(".validateTips");

	const changePasswordDialog = $("#password-form").dialog({
		autoOpen: false,
		height: 520,
		width: 400,
		modal: true,
		buttons: {
			"Submit": changePassword,
			Cancel: function() {
				changePasswordDialog.dialog("close");
			}
		},
		close: function() {
			changePasswordForm[0].reset();
			allFields.removeClass("ui-state-error");
			updateTips("All form fields are required.");
		}
	});

	const confirmDialog = $("#dialog-confirm").dialog({
		autoOpen: false,
		resizable: false,
		height: "auto",
		width: 400,
		modal: true,
		buttons: {
			"Update": changePassword,
			Cancel: function() {
				confirmDialog.dialog("close");
			}
		}
	});

	const changePasswordForm = changePasswordDialog.find("form").on("submit", function(event) {
		event.preventDefault();
		changePassword();
	});

	function updateTips(t) {
		tips.text(t).addClass("ui-state-highlight");
		setTimeout(function() {
			tips.removeClass("ui-state-highlight", 1500);
		}, 500);
	}

	function checkLength(input, name, min, max) {
		if (input.val().length > max || input.val().length < min) {
			input.addClass("ui-state-error");
			updateTips("Length of " + name + " must be between "
				+ min + " and " + max + ".");
			return false;
		} else {
			return true;
		}
	}

	function checkInputMatch(x, y, name) {
		if (x.val() != y.val()) {
			x.addClass("ui-state-error");
			y.addClass("ui-state-error");
			updateTips(name + " mismatch.");
			return false;
		} else {
			return true;
		}
	}

	function changePassword() {
		let valid = true;
		allFields.removeClass("ui-state-error");
		valid = valid
			&& checkLength(currentPassword, "Password", 1,
				32)
			&& checkLength(newPassword, "New Password", 8, 32)
			&& checkInputMatch(newPassword, cnfPassword,
				"Password");

		if (valid) {
			updatePassword(currentPassword.val(), newPassword
				.val())
			changePasswordDialog.dialog("close");
		}
		return valid;
	}

	function updatePassword(oldPassword, password) {
		const settings = {
			"async": true,
			"crossDomain": true,
			"url": "api/user/changepassword",
			"method": "POST",
			"headers": {
				"Content-Type": "application/x-www-form-urlencoded",
				"Authorization": "Bearer "
					+ authorizationToken,
				"cache-control": "no-cache"
			},
			"data": {
				"oldPassword": oldPassword,
				"password": password
			}
		};

		$.ajax(settings).done(function(_) {
			alert("Password updated");
		}).fail(function() {
			alert(errorMessage);
		}).always(function() {
		});
	}

	$("#change-password").on("click", function() {
		$("span.ui-dialog-title").text('Update password');
		updateTips("All form fields are required.");
		changePasswordDialog.dialog("open");
	});

	$(document).ajaxStart(function() {
		$("#wait-veil").css("display", "block");
	});
	$(document).ajaxComplete(function() {
		$("#wait-veil").css("display", "none");
	});
	$(document).tooltip();
	$("time.timeago").timeago();
});
