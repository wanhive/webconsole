/**
 * Users management
 */

"use strict";
$(document).ready(function() {
	//Service request error message
	const errorMessage = "Request denied";

	//Pagination
	const limit = $("#limit");
	let orderBy = "uid";
	let order = "desc";
	let offset = 0;
	let next = 0;
	let previous = 0;

	//Data table
	let currentRow = null;
	const dataTable = $("#dataTable");
	const alias = $("#alias");
	const email = $("#email");
	const password = $("#newPassword");
	const cnfPassword = $("#cnfPassword");
	const userType = $("#newType");
	const userStatus = $("#newStatus");
	const userUid = $("#userUid");
	const typeFilter = $("#typeFilter");
	const statusFilter = $("#statusFilter");
	const allFields = $([]).add(alias).add(email).add(password)
		.add(cnfPassword).add(userType).add(userStatus);
	const tips = $(".validateTips");

	//Search
	let searchActive = false;
	const searchKeyword = $("#searchKeyword");

	const addDialog = $("#add-form").dialog({
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

	const updateDialog = $("#update-form").dialog({
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

	const addForm = addDialog.find("form").on("submit", function(event) {
		event.preventDefault();
		addUser();
	});

	const updateForm = updateDialog.find("form").on("submit", function(event) {
		event.preventDefault();
		addUser();
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
			updateTips(name + " mismatch");
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
		let valid = true;
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
		let valid = true;
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

	function createUser(alias, email) {
		const settings = {
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
				"alias": alias,
				"email": email
			}
		};

		$.ajax(settings).done(function(_) {
			populateUsers();
		}).fail(function() {
			alert(errorMessage);
		}).always(function() {
		});
	}

	function updateUser(id, password, type, status) {
		const settings = {
			"async": true,
			"crossDomain": true,
			"url": "api/admin/user/" + id,
			"method": "PUT",
			"headers": {
				"Content-Type": "application/x-www-form-urlencoded",
				"Authorization": "Bearer "
					+ authorizationToken,
				"Cache-Control": "no-cache"
			},
			"data": {
				"password": password,
				"type": type,
				"status": status
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
		const settings = {
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
		const settings = {
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
		const settings = {
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
		const tBody = dataTable.children('tbody');
		tBody.empty();
		const count = json.data.length;
		for (const data of json.data) {
			tBody.append('<tr><td>'
				+ '</td><td>'
				+ data.uid
				+ '</td><td style="word-break:break-all;">'
				+ data.email
				+ '</td><td style="word-break:break-all;">'
				+ data.alias
				+ '</td><td>'
				+ typeCodeToText(data.type)
				+ '</td><td>'
				+ statusCodeToText(data.status)
				+ '</td><td>'
				+ '<time class="timeago" datetime="'
				+ data.createdOn
				+ '" title="'
				+ data.createdOn
				+ '">'
				+ data.createdOn
				+ '</time>'
				+ '</td><td><span style="white-space: nowrap;">'
				+ '<button class="btn-modify" title="Modify"><i class="fa fa-edit"></i></button>&nbsp;'
				+ '</span></td></tr>');
		}

		$("time.timeago").timeago();

		const currentLimit = parseInt(limit.val());
		$("#offsetFrom").text(((count > 0) ? (offset + 1) : 0));
		$("#offsetTo").text((offset + count));
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

	function doSearch() {
		if (searchKeyword.val() == null
			|| searchKeyword.val().length < minSearchKeywordLength) {
			alert("Search keyword should be at least " + minSearchKeywordLength + " characters long.");
			return;
		} else if (searchKeyword.val().length > maxSearchKeywordLength) {
			alert("Search keyword should not exceed " + maxSearchKeywordLength + " characters.");
			return;
		} else {
			searchActive = true;
			offset = 0;
			next = 0;
			previous = 0;
			populateUsers();
		}
	}

	limit.change(function() {
		offset = 0;
		previous = 0;
		next = 0;
		populateUsers();
	});

	searchKeyword.keyup(function(event) {
		if (event.keyCode === 13) { //Enter key-code
			event.preventDefault();
			doSearch();
		}
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

	$("#pageCounter").on("keyup", function(event) {
		if (event.keyCode === 13) { //Enter key-code
			const pageNo = parseInt($(this).val());
			const maxPageNo = parseInt($(this)
				.attr("max"));
			const minPageNo = parseInt($(this)
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
		}
		searchKeyword.val("");
		populateUsers();
	});

	$("#create-user").on("click", function() {
		$("span.ui-dialog-title").text('Create new user');
		updateTips("All form fields are required.");
		addDialog.dialog("open");
	});

	$("#search-users").on("click", function() {
		doSearch();
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
