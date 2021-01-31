/**
 * User account management
 */

$(document).ready(function() {
	var errorMessage = "Request denied.";
	var currentPassword = $("#currentPassword");
	var newPassword = $("#newPassword");
	var cnfPassword = $("#cnfPassword");

	var allFields = $([]).add(currentPassword).add(newPassword)
		.add(cnfPassword);
	var tips = $(".validateTips");

	var changePasswordDialog = $("#password-form").dialog({
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

	var confirmDialog = $("#dialog-confirm").dialog({
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

	var changePasswordForm = changePasswordDialog.find("form").on("submit", function(event) {
		event.preventDefault();
		changePassword();
	});

	function updateTips(t) {
		tips.text(t).addClass("ui-state-highlight");
		setTimeout(function() {
			tips.removeClass("ui-state-highlight", 1500);
		}, 500);
	}

	function checkLength(o, n, min, max) {
		if (o.val().length > max || o.val().length < min) {
			o.addClass("ui-state-error");
			updateTips("Length of " + n + " must be between "
				+ min + " and " + max + ".");
			return false;
		} else {
			return true;
		}
	}

	function checkInputMatch(x, y, n) {
		if (x.val() != y.val()) {
			x.addClass("ui-state-error");
			y.addClass("ui-state-error");
			updateTips(n + " mismatch.");
			return false;
		} else {
			return true;
		}
	}

	function changePassword() {
		var valid = true;
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

	function updatePassword(o, n) {
		var settings = {
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
				"oldPassword": o,
				"password": n
			}
		}

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
