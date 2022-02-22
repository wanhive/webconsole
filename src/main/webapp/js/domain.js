/**
 * Domains management
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
	const domainUid = $("#domainUid");
	const name = $("#newName");
	const description = $("#newDescription");
	const allFields = $([]).add(name).add(description);
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
			"Submit": addDomain,
			Cancel: function() {
				addDialog.dialog("close");
			}
		},
		close: function() {
			addForm[0].reset();
			domainUid.val("");
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
			"Delete": deleteDomain,
			Cancel: function() {
				confirmDialog.dialog("close");
			}
		}
	});

	const addForm = addDialog.find("form").on("submit", function(event) {
		event.preventDefault();
		addDomain();
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

	function addDomain() {
		let valid = true;
		allFields.removeClass("ui-state-error");
		valid = valid && checkLength(name, "Name", 1, 64);
		valid = valid
			&& checkLength(description, "Description", 1,
				128);
		if (valid) {
			if (domainUid.val() != null
				&& domainUid.val().length > 0) {
				updateDomain(domainUid.val(), name.val(),
					description.val())
			} else {
				createDomain(name.val(), description.val());
			}
			addDialog.dialog("close");
		}
		return valid;
	}

	function deleteDomain() {
		const id = currentRow.find('td:eq(1)').text();
		const settings = {
			"async": true,
			"crossDomain": true,
			"url": "api/domain/" + id,
			"method": "DELETE",
			"headers": {
				"Authorization": "Bearer "
					+ authorizationToken,
				"Cache-Control": "no-cache",
				"Content-Type": "application/x-www-form-urlencoded"
			}
		};

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
			populateDomains();
			confirmDialog.dialog("close");
		}).fail(function() {
			alert(errorMessage);
		}).always(function() {
		});
	}

	function createDomain(name, description) {
		const settings = {
			"async": true,
			"crossDomain": true,
			"url": "api/domain",
			"method": "POST",
			"headers": {
				"Content-Type": "application/x-www-form-urlencoded",
				"Authorization": "Bearer "
					+ authorizationToken,
				"Cache-Control": "no-cache"
			},
			"data": {
				"name": name,
				"description": description
			}
		};

		$.ajax(settings).done(function(_) {
			populateDomains();
		}).fail(function() {
			alert(errorMessage);
		}).always(function() {
		});
	}

	function updateDomain(id, name, description) {
		const settings = {
			"async": true,
			"crossDomain": true,
			"url": "api/domain/" + id,
			"method": "PUT",
			"headers": {
				"Content-Type": "application/x-www-form-urlencoded",
				"Authorization": "Bearer "
					+ authorizationToken,
				"Cache-Control": "no-cache"
			},
			"data": {
				"name": name,
				"description": description
			}
		};

		$.ajax(settings).done(function(_) {
			populateDomains();
		}).fail(function() {
			alert(errorMessage);
		}).always(function() {
		});
	}

	function populateDomains() {
		setTimeout(function() {
			if (searchActive == true) {
				searchDomains();
				return;
			} else {
				listDomains();
				return;
			}

		}, 200);
	}

	function listDomains() {
		const settings = {
			"async": true,
			"crossDomain": true,
			"url": "api/domain?orderBy=" + orderBy + "&order="
				+ order + "&limit=" + limit.val()
				+ "&offset=" + offset,
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

	function searchDomains() {
		const settings = {
			"async": true,
			"crossDomain": true,
			"url": "api/domain/search?keyword="
				+ searchKeyword.val() + "&orderBy="
				+ orderBy + "&order=" + order + "&limit="
				+ limit.val() + "&offset=" + offset,
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

	function populateDataTable(json) {
		const tBody = dataTable.children('tbody');
		tBody.empty();
		const count = json.data.length;
		for (const data of json.data) {
			tBody.append('<tr><td>'
				+ '</td><td>'
				+ data.uid
				+ '</td><td>'
				+ data.name
				+ '</td><td>'
				+ data.description
				+ '</td><td>'
				+ '<time class="timeago" datetime="'
				+ data.createdOn
				+ '" title="'
				+ data.createdOn
				+ '">'
				+ data.createdOn
				+ '</time>'
				+ '</td><td><span style="white-space: nowrap;">'
				+ '<button class="btn-things" title="Manage things"><i class="fab fa-connectdevelop"></i></button>&nbsp;'
				+ '<button class="btn-modify" title="Modify"><i class="fa fa-edit"></i></button>&nbsp;'
				+ '<button class="btn-remove" title="Remove"><i class="fa fa-trash"></i></button>'
				+ '</span></td></tr>');
		}
		$("time.timeago").timeago();

		const currentLimit = parseInt(limit.val());
		$("#offsetFrom").text(((count > 0) ? (offset + 1) : 0));
		$("#offsetTo").text((offset + count));
		$("#totalRecords").text(json.recordsTotal);
		previous = (offset > currentLimit) ? (offset - parseInt(limit.val())) : 0;
		next = ((offset + limit.val()) < json.recordsTotal) ? (offset + currentLimit) : 0;
		$("#pageCounter").val(Math.floor((offset + currentLimit) / currentLimit));
		$("#pageCounter").attr("min", 1);
		$("#pageCounter").attr("max", Math.floor((json.recordsTotal + currentLimit) / currentLimit));
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
		populateDomains();
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
			populateDomains();
		}
	}

	limit.change(function() {
		offset = 0;
		previous = 0;
		next = 0;
		populateDomains();
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

	$("#nameCol").click(function() {
		reorderTable($(this), "name");
	});
	$("#createdOnCol").click(function() {
		reorderTable($(this), "createdon");
	});

	$("#previousPage").click(function() {
		if (offset != previous) {
			offset = previous;
			populateDomains();
		}
	});
	$("#nextPage").click(function() {
		if (offset != next) {
			offset = next;
			populateDomains();
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
			populateDomains();
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
		populateDomains();
	});

	$("#create-domain").on("click", function() {
		$("span.ui-dialog-title").text('Create new domain');
		updateTips("All form fields are required.");
		addDialog.dialog("open");
	});

	$("#search-domain").on("click", function() {
		doSearch();
	});

	$("#dataTable").on("click", ".btn-things", function(_) {
		window.location = "thing.jsp?domainUid="
			+ $(this).closest("tr")
				.find('td:eq(1)').text();
	});

	$("#dataTable").on("click", ".btn-modify", function(_) {
		currentRow = $(this).closest("tr");
		domainUid.val(currentRow.find('td:eq(1)')
			.text());
		name.val(currentRow.find('td:eq(2)').text());
		description.val(currentRow.find('td:eq(3)')
			.text());
		$("span.ui-dialog-title").text(
			'Update domain '
			+ currentRow.find('td:eq(1)')
				.text());
		updateTips("All form fields are required.");
		addDialog.dialog("open");
	});

	$("#dataTable").on("click", ".btn-remove", function(_) {
		currentRow = $(this).closest("tr");
		$("span.ui-dialog-title").text(
			'Delete domain '
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
	populateDomains();

});
