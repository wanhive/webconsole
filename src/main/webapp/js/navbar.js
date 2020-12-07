/**
 * Toggle between adding and removing the "responsive" class to topnav when
 * the user clicks on the icon.
 */

function toggleNavBar() {
	var x = document.getElementById("topnav");
	if (x.className === "topnav") {
		x.className += " responsive";
	} else {
		x.className = "topnav";
	}
}
