/**
 * User account activation: either a new user completing the sign up process
 * or, an existing user attempting account recovery.
 */

$(document).ready(function() {
	$('#secret, #cnfsecret').on('keyup', function() {
		if ($('#secret').val() == $('#cnfsecret').val()) {
			$('#message').html('Matching').css('color', 'green');
			$(':input[type="submit"]').prop('disabled', false);
		} else {
			$(':input[type="submit"]').prop('disabled', true);
			$('#message').html('Not Matching').css('color', 'red');
		}
	});

	$("#activationForm").submit(function() {
		var challenge = $('input[name=challenge]');
		challenge.val(challenge.val().trim());
		return true;
	});
});
