/**
 * User account activation: either a new user completing the sign up process
 * or, an existing user attempting account recovery.
 */

$(document).ready(function() {
	$('#secret, #cnfsecret').on('keyup', function() {
		if ($('#secret').val() == $('#cnfsecret').val()) {
			$('#message').html('&nbsp;');
			$(':input[type="submit"]').prop('disabled', false);
		} else {
			$('#message').html('Passwords mismatch').css('color', 'red');
			$(':input[type="submit"]').prop('disabled', true);
		}
	});

	$("#activationForm").submit(function() {
		var challenge = $('input[name=challenge]');
		challenge.val(challenge.val().trim());
		return true;
	});
});
