/**
 * Users management
 */

$(document).ready(function() {
	var errorMessage = "Request denied";
	var orderBy = "uid";
	var order = "desc";
	var limit = $("#limit");
	var offset = 0;
	var next = 0;
	var previous = 0;

	var currentRow;
	var dataTable = $("#dataTable");
	var alias = $("#alias");
	var email = $("#email");
	var password = $("#newPassword");
	var cnfPassword = $("#cnfPassword");
	var userType = $("#newType");
	var userStatus = $("#newStatus");
	var userUid = $("#userUid");

	var typeFilter = $("#typeFilter");
	var statusFilter = $("#statusFilter");

	var allFields = $([]).add(alias).add(email).add(password)
		.add(cnfPassword).add(userType).add(userStatus);
	var tips = $(".validateTips");

	var searchActive = false;
	var searchKeyword = $("#searchKeyword");

	var addDialog = $("#add-form").dialog({
		autoOpen: false,
		height: 520,
		width: 400,
		modal: true,
		buttons: {
			"Submit": addUser,
			Cancel: function() {
				addDialog.dialog("close");
			}
		},
		close: function() {
			addForm[0].reset();
			userUid.val("");
			allFields.removeClass("ui-state-error");
			updateTips("All form fields are required.");
		}
	});

	var updateDialog = $("#update-form").dialog({
		autoOpen: false,
		height: 520,
		width: 400,
		modal: true,
		buttons: {
			"Submit": modifyUser,
			Cancel: function() {
				updateDialog.dialog("close");
			}
		},
		close: function() {
			updateForm[0].reset();
			userUid.val("");
			allFields.removeClass("ui-state-error");
			updateTips("All form fields are required.");
		}
	});

	var addForm = addDialog.find("form").on("submit", function(event) {
		event.preventDefault();
		addUser();
	});

	var updateForm = updateDialog.find("form").on("submit", function(event) {
		event.preventDefault();
		addUser();
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
			updateTips(n + " mismatch");
			return false;
		} else {
			return true;
		}
	}

	function typeCodeToText(t) {
		switch (t) {
			case 0:
				return "User";
			case 1:
				return "Reporter";
			case 2:
				return "Maintainer";
			case 3:
				return "Administrator";
			default:
				return "Invalid";
		}
	}

	function typeTextToCode(t) {
		switch (t) {
			case "User":
				return 0;
			case "Reporter":
				return 1;
			case "Maintainer":
				return 2;
			case "Administrator":
				return 3;
			default:
				return -1;
		}
	}

	function statusCodeToText(s) {
		switch (s) {
			case 0:
				return "Inactive";
			case 1:
				return "Active";
			case 2:
				return "Blocked";
			default:
				return "Invalid";
		}
	}

	function statusTextToCode(s) {
		switch (s) {
			case "Inactive":
				return 0;
			case "Active":
				return 1;
			case "Blocked":
				return 2;
			default:
				return -1;
		}
	}

	function addUser() {
		var valid = true;
		allFields.removeClass("ui-state-error");
		valid = valid && checkLength(alias, "Alias", 1, 64);
		valid = valid && checkLength(email, "Email", 1, 64);
		if (valid) {
			createUser(alias.val(), email.val());
			addDialog.dialog("close");
		}
		return valid;
	}

	function modifyUser() {
		var valid = true;
		allFields.removeClass("ui-state-error");
		if (password.val().length > 0) {
			valid = valid
				&& checkLength(password, "Password", 8, 32)
				&& checkInputMatch(password, cnfPassword,
					"Password");
		}

		if (valid) {
			updateUser(userUid.val(), password.val(), userType
				.val(), userStatus.val());
			updateDialog.dialog("close");
		}
		return valid;
	}

	function createUser(a, e) {
		var settings = {
			"async": true,
			"crossDomain": true,
			"url": "api/admin/user",
			"method": "POST",
			"headers": {
				"Content-Type": "application/x-www-form-urlencoded",
				"Authorization": "Bearer "
					+ authorizationToken,
				"Cache-Control": "no-cache"
			},
			"data": {
				"alias": a,
				"email": e
			}
		}

		$.ajax(settings).done(function(_) {
			populateUsers();
		}).fail(function() {
			alert(errorMessage);
		}).always(function() {
		});
	}

	function updateUser(i, p, t, s) {
		var settings = {
			"async": true,
			"crossDomain": true,
			"url": "api/admin/user/" + i,
			"method": "PUT",
			"headers": {
				"Content-Type": "application/x-www-form-urlencoded",
				"Authorization": "Bearer "
					+ authorizationToken,
				"Cache-Control": "no-cache"
			},
			"data": {
				"password": p,
				"type": t,
				"status": s
			}
		};

		$.ajax(settings).done(function(_) {
			populateUsers();
		}).fail(function() {
			alert(errorMessage);
		}).always(function() {
		});
	}

	function populateUsers() {
		setTimeout(function() {
			if (searchActive == true) {
				searchUsers();
				return;
			} else {
				listUsers();
				return;
			}

		}, 200);
	}

	function listUsers() {
		var settings = {
			"async": true,
			"crossDomain": true,
			"url": "api/admin/user?orderBy=" + orderBy
				+ "&order=" + order + "&limit="
				+ limit.val() + "&offset=" + offset
				+ "&type=" + typeFilter.val() + "&status="
				+ statusFilter.val(),
			"method": "GET",
			"headers": {
				"Content-Type": "application/x-www-form-urlencoded",
				"Authorization": "Bearer "
					+ authorizationToken,
				"Cache-Control": "no-cache"
			}
		};

		$.ajax(settings).done(function(response) {
			populateDataTable(response);
		}).fail(function() {
			alert(errorMessage);
		}).always(function() {
		});
	}

	function searchUsers() {
		var settings = {
			"async": true,
			"crossDomain": true,
			"url": "api/admin/user/search?keyword="
				+ searchKeyword.val() + "&orderBy="
				+ orderBy + "&order=" + order + "&limit="
				+ limit.val() + "&offset=" + offset
				+ "&type=" + typeFilter.val() + "&status="
				+ statusFilter.val(),
			"method": "GET",
			"headers": {
				"Content-Type": "application/x-www-form-urlencoded",
				"Authorization": "Bearer "
					+ authorizationToken,
				"cache-control": "no-cache"
			}
		};

		$.ajax(settings).done(function(response) {
			populateDataTable(response);
		}).fail(function() {
			alert(errorMessage);
		}).always(function() {
		});
	}

	function purgeBearerTokens() {
		var settings = {
			"async": true,
			"crossDomain": true,
			"url": "api/admin/user/tokens",
			"method": "DELETE",
			"headers": {
				"Authorization": "Bearer "
					+ authorizationToken,
				"cache-control": "no-cache"
			}
		};
		$.ajax(settings).done(function(_) {
			alert("All bearer tokens deleted");
			window.location.href = "index.jsp";
		}).fail(function() {
			alert(errorMessage);
		}).always(function() {
		});
	}

	function populateDataTable(json) {
		var tBody = dataTable.children('tbody');
		tBody.empty();
		for (var i = 0; i < json.data.length; i++) {
			tBody
				.append('<tr><td>'
					+ '</td><td>'
					+ json.data[i].uid
					+ '</td><td style="word-break:break-all;">'
					+ json.data[i].email
					+ '</td><td style="word-break:break-all;">'
					+ json.data[i].alias
					+ '</td><td>'
					+ typeCodeToText(json.data[i].type)
					+ '</td><td>'
					+ statusCodeToText(json.data[i].status)
					+ '</td><td>'
					+ '<time class="timeago" datetime="'
					+ json.data[i].createdOn
					+ '" title="'
					+ json.data[i].createdOn
					+ '">'
					+ json.data[i].createdOn
					+ '</time>'
					+ '</td><td><span style="white-space: nowrap;">'
					+ '<button class="btn-modify" title="Modify"><i class="fa fa-edit"></i></button>&nbsp;'
					+ '</span></td></tr>');
		}
		$("time.timeago").timeago();

		var currentLimit = parseInt(limit.val());
		$("#offsetFrom").text(((i > 0) ? (offset + 1) : 0));
		$("#offsetTo").text((offset + i));
		$("#totalRecords").text(json.recordsTotal);
		previous = (offset > currentLimit) ? (offset - parseInt(limit
			.val()))
			: 0;
		next = ((offset + limit.val()) < json.recordsTotal) ? (offset + currentLimit)
			: 0;
		$("#pageCounter").val(
			Math.floor((offset + currentLimit)
				/ currentLimit));
		$("#pageCounter").attr("min", 1);
		$("#pageCounter").attr(
			"max",
			Math.floor((json.recordsTotal + currentLimit)
				/ currentLimit));
		$("#dataTable").basictable('restart');
		$(window).scrollTop(0);
	}

	function reorderTable(col, ord) {
		$("i.order-caret").removeClass(
			"fa-caret-down fa-caret-up");
		if (orderBy == ord) {
			order = (order == "desc") ? "asc" : "desc";
		} else {
			orderBy = ord;
			order = "desc";
		}
		populateUsers();
		col.children("i").addClass(
			(order == "asc") ? "fa-caret-up"
				: "fa-caret-down");
	}

	limit.change(function() {
		offset = 0;
		previous = 0;
		next = 0;
		populateUsers();
	});
	$("#uidCol").click(function() {
		reorderTable($(this), "uid");
	});

	$("#emailCol").click(function() {
		reorderTable($(this), "email");
	});
	$("#createdOnCol").click(function() {
		reorderTable($(this), "createdon");
	});

	$("#previousPage").click(function() {
		if (offset != previous) {
			offset = previous;
			populateUsers();
		}
	});
	$("#nextPage").click(function() {
		if (offset != next) {
			offset = next;
			populateUsers();
		}
	});

	$("#pageCounter").bind('keyup', function(event) {
		if (event.keyCode === 13) {
			var pageNo = parseInt($(this).val());
			var maxPageNo = parseInt($(this)
				.attr("max"));
			var minPageNo = parseInt($(this)
				.attr("min"));

			if (!isNaN(pageNo) && pageNo <= maxPageNo
				&& pageNo >= minPageNo) {
				offset = parseInt(limit.val())
					* (pageNo - 1);
			}
			populateUsers();
		}
	});

	$("#reload-data").on("click", function() {
		// If a search was active then reset the data table
		if (searchActive == true) {
			searchActive = false;
			offset = 0;
			next = 0;
			previous = 0;
			searchKeyword.val("");
		}
		populateUsers();
	});

	$("#create-user").on("click", function() {
		$("span.ui-dialog-title").text('Create new user');
		updateTips("All form fields are required.");
		addDialog.dialog("open");
	});

	$("#search-users").on("click", function() {
		if (searchKeyword.val() == null
			|| searchKeyword.val().length < minSearchKeywordLength) {
			alert("Search keyword should be at least " + minSearchKeywordLength + " characters long.");
			return;
		} else {
			// Activate the search
			searchActive = true;
			offset = 0;
			next = 0;
			previous = 0;
			populateUsers();
		}
	});

	$("#dataTable").on("click", ".btn-modify", function(_) {
		currentRow = $(this).closest("tr");
		userUid.val(currentRow.find('td:eq(1)')
			.text());
		userType.val(typeTextToCode(currentRow
			.find('td:eq(4)').text()));
		userStatus
			.val(statusTextToCode(currentRow
				.find('td:eq(5)')
				.text()));
		$("span.ui-dialog-title").text(
			'Update user '
			+ currentRow.find(
				'td:eq(2)')
				.text());
		updateTips("Password field is optional.");
		updateDialog.dialog("open");
	});

	$("#purge-tokens").on("click", function() {
		if (confirm("Delete all the existing bearer tokens?") == true) {
			purgeBearerTokens();
		} else {
			return;
		}
	});

	$("#dataTable").basictable({
		breakpoint: 768
	});

	$(document).ajaxStart(function() {
		$("#wait-veil").css("display", "block");
	});
	$(document).ajaxComplete(function() {
		$("#wait-veil").css("display", "none");
	});

	$(document).tooltip();
	populateUsers();

});
