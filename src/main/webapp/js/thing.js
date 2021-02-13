/**
 * Things management
 */

$(document).ready(function() {
	var errorMessage = "Request denied";
	var orderBy = "createdon";
	var order = "desc";
	var limit = $("#limit");
	var offset = 0;
	var next = 0;
	var previous = 0;

	var currentRow;
	var dataTable = $("#dataTable");
	var name = $("#newName");
	var type = $("#newType");
	var salt = $("#newSalt");
	var verifier = $("#newVerifier");
	var password = $("#newPassword");
	var cnfPassword = $("#cnfPassword");
	var rounds = $("#newRounds");
	var thingUid = $("#thingUid");
	var computeVerifier = $("#computeVerifier");
	var allFields = $([]).add(name).add(type).add(salt).add(
		verifier).add(password).add(cnfPassword)
		.add(rounds);
	var tips = $(".validateTips");

	var searchActive = false;
	var searchKeyword = $("#searchKeyword");

	var addDialog = $("#add-form").dialog({
		autoOpen: false,
		height: 520,
		width: 400,
		modal: true,
		buttons: {
			"Submit": addThing,
			Cancel: function() {
				addDialog.dialog("close");
			}
		},
		close: function() {
			addForm[0].reset();
			thingUid.val("");
			computeVerifier.val("");
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
			"Delete": deleteThing,
			Cancel: function() {
				confirmDialog.dialog("close");
			}
		}
	});

	var addForm = addDialog.find("form").on("submit", function(event) {
		event.preventDefault();
		addThing();
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

	function checkRange(x, n, min, max) {
		if (x.val() < min || x.val() > max) {
			x.addClass("ui-state-error");
			updateTips("Value of " + n + " must be between "
				+ min + " and " + max + ".");
			return false;
		} else {
			return true;
		}
	}

	function addThing() {
		var valid = true;
		allFields.removeClass("ui-state-error");
		if (computeVerifier.val().length > 0) {
			valid = valid
				&& checkLength(password, "Password", 8, 32)
				&& checkInputMatch(password, cnfPassword,
					"Password")
				&& checkRange(rounds, "Rounds", 1,
					maxPasswordHashRounds);
		} else {
			valid = valid && checkLength(name, "Name", 1, 64);
		}
		if (valid) {
			if (thingUid.val() != null
				&& thingUid.val().length > 0) {
				if (computeVerifier.val().length > 0) {
					updateVerifier(thingUid.val(), password
						.val(), rounds.val());
				} else {
					updateThing(thingUid.val(), name.val(),
						type.val(), salt.val(), verifier
							.val());
				}
			} else {
				createThing(name.val(), type.val());
			}
			addDialog.dialog("close");
		}
		return valid;
	}

	function deleteThing() {
		var i = currentRow.find('td:eq(1)').text();
		var settings = {
			"async": true,
			"crossDomain": true,
			"url": "api/thing/" + i,
			"method": "DELETE",
			"headers": {
				"Authorization": "Bearer "
					+ authorizationToken,
				"Cache-Control": "no-cache",
				"Content-Type": "application/x-www-form-urlencoded"
			}
		}

		$.ajax(settings).done(function(_) {
			// Edge case: the last element in the record
			if ($("#offsetFrom").text() == $(
				"#offsetTo").text()) {
				if (offset >= limit.val()) {
					offset -= limit.val();
				} else {
					offset = 0;
				}
			}
			populateThings();
			confirmDialog.dialog("close");
		}).fail(function() {
			alert(errorMessage);
		}).always(function() {
		});
	}

	function createThing(n, t) {
		var settings = {
			"async": true,
			"crossDomain": true,
			"url": "api/thing",
			"method": "POST",
			"headers": {
				"Content-Type": "application/x-www-form-urlencoded",
				"Authorization": "Bearer "
					+ authorizationToken,
				"Cache-Control": "no-cache"
			},
			"data": {
				"name": n,
				"domainUid": domainUid,
				"type": t
			}
		}

		$.ajax(settings).done(function(_) {
			populateThings();
		}).fail(function() {
			alert(errorMessage);
		}).always(function() {
		});
	}

	function updateThing(i, n, t, s, v) {
		var settings = {
			"async": true,
			"crossDomain": true,
			"url": "api/thing/" + i,
			"method": "PUT",
			"headers": {
				"Content-Type": "application/x-www-form-urlencoded",
				"Authorization": "Bearer "
					+ authorizationToken,
				"Cache-Control": "no-cache"
			},
			"data": {
				"name": n,
				"type": t,
				"salt": s.trim(),
				"verifier": v.trim()
			}
		}

		$.ajax(settings).done(function(_) {
			populateThings();
		}).fail(function() {
			alert(errorMessage);
		}).always(function() {
		});
	}

	function updateVerifier(i, p, r) {
		var settings = {
			"async": true,
			"crossDomain": true,
			"url": "api/thing/" + i + "/verifier",
			"method": "PUT",
			"headers": {
				"Content-Type": "application/x-www-form-urlencoded",
				"Authorization": "Bearer "
					+ authorizationToken,
				"cache-control": "no-cache"
			},
			"data": {
				"rounds": r,
				"password": p
			}
		}

		$.ajax(settings).done(function(_) {
			alert("Verifier updated");
		}).fail(function() {
			alert(errorMessage);
		}).always(function() {
		});
	}

	function populateThings() {
		setTimeout(function() {
			if (searchActive == true) {
				searchThings();
				return;
			} else {
				listThings();
				return;
			}

		}, 200);
	}

	function listThings() {
		var settings = {
			"async": true,
			"crossDomain": true,
			"url": "api/thing?domainUid=" + domainUid
				+ "&orderBy=" + orderBy + "&order=" + order
				+ "&limit=" + limit.val() + "&offset="
				+ offset,
			"method": "GET",
			"headers": {
				"Content-Type": "application/x-www-form-urlencoded",
				"Authorization": "Bearer "
					+ authorizationToken,
				"Cache-Control": "no-cache"
			}
		}

		$.ajax(settings).done(function(response) {
			populateDataTable(response);
		}).fail(function() {
			alert(errorMessage);
		}).always(function() {
		});
	}

	function searchThings() {
		var settings = {
			"async": true,
			"crossDomain": true,
			"url": "api/thing/search?domainUid=" + domainUid
				+ "&keyword=" + searchKeyword.val()
				+ "&orderBy=" + orderBy + "&order=" + order
				+ "&limit=" + limit.val() + "&offset="
				+ offset,
			"method": "GET",
			"headers": {
				"Content-Type": "application/x-www-form-urlencoded",
				"Authorization": "Bearer "
					+ authorizationToken,
				"cache-control": "no-cache"
			}
		}

		$.ajax(settings).done(function(response) {
			populateDataTable(response);
		}).fail(function() {
			alert(errorMessage);
		}).always(function() {
		});
	}

	function typeCodeToText(type) {
		switch (type) {
			case 0:
				return "Master";
			case 1:
				return "Actuator";
			case 2:
				return "Controller";
			case 3:
				return "Sensor";
			case 4:
				return "Processor";
			default:
				return "Invalid";
		}
	}

	function typeTextToCode(type) {
		switch (type) {
			case "Master":
				return 0;
			case "Actuator":
				return 1;
			case "Controller":
				return 2;
			case "Sensor":
				return 3;
			case "Processor":
				return 4;
			default:
				return 255;
		}
	}

	function populateDataTable(json) {
		var tBody = dataTable.children('tbody');
		tBody.empty();
		for (var i = 0; i < json.data.length; i++) {
			tBody
				.append('<tr><td>'
					+ '</td><td>'
					+ json.data[i].uid
					+ '</td><td>'
					+ json.data[i].name
					+ '</td><td>'
					+ typeCodeToText(json.data[i].type)
					+ '</td><td>'
					+ '<time class="timeago" datetime="'
					+ json.data[i].createdOn
					+ '" title="'
					+ json.data[i].createdOn
					+ '">'
					+ json.data[i].createdOn
					+ '</time>'
					+ '</td><td><span style="white-space: nowrap;">'
					+ '<button class="btn-settings" title="Settings"><i class="fas fa-shield-alt"></i></button>&nbsp;'
					+ '<button class="btn-modify" title="Modify"><i class="fa fa-edit"></i></button>&nbsp;'
					+ '<button class="btn-remove" title="Remove"><i class="fa fa-trash"></i></button>'
					+ '</span></td></tr>');
		}
		$("time.timeago").timeago();

		var currentLimit = parseInt(limit.val());
		$("#offsetFrom").text(((i > 0) ? (offset + 1) : 0));
		$("#offsetTo").text((offset + i));
		$("#totalRecords").text(json.recordsTotal);
		previous = (offset > currentLimit) ? (offset - parseInt(limit.val())) : 0;
		next = ((offset + limit.val()) < json.recordsTotal) ? (offset + currentLimit) : 0;
		$("#pageCounter").val(Math.floor((offset + currentLimit) / currentLimit));
		$("#pageCounter").attr("min", 1);
		$("#pageCounter").attr("max", Math.floor((json.recordsTotal + currentLimit) / currentLimit));
		$("#dataTable").basictable('restart');
		$(window).scrollTop(0);
	}

	function loadDomainInfo() {
		var settings = {
			"async": true,
			"crossDomain": true,
			"url": "api/domain/" + domainUid,
			"method": "GET",
			"headers": {
				"Content-Type": "application/x-www-form-urlencoded",
				"Authorization": "Bearer "
					+ authorizationToken,
				"Cache-Control": "no-cache",
			}
		}

		$.ajax(settings).done(function(response) {
			$("aside.right")
				.html(
					'<h3>'
					+ response.name
					+ '</h3><p><em>'
					+ response.description
					+ '</em></p>'
					+ '<p><strong>Things limit:</strong> '
					+ maxThingsPerDomain
					+ ' things</p>');
		}).fail(function() {
			alert(errorMessage);
		}).always(function() {
		});
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
		populateThings();
		col.children("i").addClass(
			(order == "asc") ? "fa-caret-up"
				: "fa-caret-down");
	}

	limit.change(function() {
		offset = 0;
		previous = 0;
		next = 0;
		populateThings();
	});
	$("#uidCol").click(function() {
		reorderTable($(this), "uid");
	});

	$("#nameCol").click(function() {
		reorderTable($(this), "name");
	});
	$("#createdOnCol").click(function() {
		reorderTable($(this), "createdon");
	});

	$("#previousPage").click(function() {
		if (offset != previous) {
			offset = previous;
			populateThings();
		}
	});
	$("#nextPage").click(function() {
		if (offset != next) {
			offset = next;
			populateThings();
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
			populateThings();
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
		populateThings();
	});

	$("#create-thing").on("click", function() {
		$("span.ui-dialog-title").text('Create new thing');
		$('label[for=newName], #newName').show();
		$('label[for=newType], #newType').show();
		$('label[for=newSalt], textarea#newSalt').hide();
		$('label[for=newVerifier], textarea#newVerifier').hide();
		$('label[for=newPassword], #newPassword').hide();
		$('label[for=cnfPassword], #cnfPassword').hide();
		$('label[for=newRounds], #newRounds').hide();
		type.val("0");
		updateTips("Name is required.");
		addDialog.dialog("open");
	});

	$("#search-thing").on("click", function() {
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
			populateThings();
		}
	});

	$("#dataTable").on("click", ".btn-settings", function(_) {
		currentRow = $(this).closest("tr");
		thingUid.val(currentRow
			.find('td:eq(1)').text());
		$("span.ui-dialog-title").text(
			'Generate verifier for thing '
			+ currentRow.find(
				'td:eq(1)')
				.text());
		$('label[for=newName], #newName').hide();
		$('label[for=newType], #newType').hide();
		$('label[for=newSalt], textarea#newSalt')
			.hide();
		$(
			'label[for=newVerifier], textarea#newVerifier')
			.hide();
		$('label[for=newPassword], #newPassword')
			.show();
		$(
			'label[for=cnfPassword], #cnfPassword')
			.show();
		$('label[for=newRounds], #newRounds')
			.show();
		computeVerifier.val("true");
		updateTips("All form fields are required.");
		addDialog.dialog("open");
	});

	$("#dataTable").on("click", ".btn-modify", function(_) {
		currentRow = $(this).closest("tr");
		thingUid.val(currentRow
			.find('td:eq(1)').text());
		name.val(currentRow.find('td:eq(2)')
			.text());
		type.val(typeTextToCode(currentRow
			.find('td:eq(3)').text()));
		$("span.ui-dialog-title").text(
			'Update thing '
			+ currentRow.find(
				'td:eq(1)')
				.text());
		$('label[for=newName], #newName').show();
		$('label[for=newType], #newType').show();
		$('label[for=newSalt], textarea#newSalt')
			.show();
		$(
			'label[for=newVerifier], textarea#newVerifier')
			.show();
		$('label[for=newPassword], #newPassword')
			.hide();
		$(
			'label[for=cnfPassword], #cnfPassword')
			.hide();
		$('label[for=newRounds], #newRounds')
			.hide();
		updateTips("Name is required.");
		addDialog.dialog("open");
	});

	$("#dataTable").on("click", ".btn-remove", function(_) {
		currentRow = $(this).closest("tr");
		$("span.ui-dialog-title").text(
			'Delete thing '
			+ currentRow.find('td:eq(1)')
				.text());
		confirmDialog.dialog("open");
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
	loadDomainInfo();
	populateThings();
});
