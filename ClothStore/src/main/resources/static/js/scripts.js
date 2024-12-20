/**
 *  
 */

// disable the billing information when box is chcked
function validateBillingAddress() {
	if ($("#theSameAsShippingAddress").is(":checked")) {
		$(".billingAddress").prop("disabled", true);
	} else {
		$(".billingAddress").prop("disabled", false);
	}
}

// if password is not mathc or it is empty, disable the login button 
function ensurePasswordMatch() {
	var secretKey = $("#txtNewPassword").val();
	var confirmSecretKey = $("#txtConfirmPassword").val();

	if (secretKey == "" && confirmSecretKey == "") {
		$("#checkPasswordMatch").html("");
		$("#updateUserInfoButton").prop('disabled', false);
	} else {
		if (secretKey != confirmSecretKey) {
			$("#checkPasswordMatch").html("Passwords do not match!");
			$("#updateUserInfoButton").prop('disabled', true);
		} else {
			$("#checkPasswordMatch").html("Passwords match");
			$("#updateUserInfoButton").prop('disabled', false);
		}
	}
}


$(document).ready(function() {
	// update button appear when change quality of items
	$(".cartItemQty").on('change', function() {
		var id = this.id;
		
		$('#update-item-' + id).css('display', 'inline-block');
	});
	// event listener for BillingAddress 
	$("#theSameAsShippingAddress").on('click', validateBillingAddress);
	// action listen ensure password is match  
	$("#txtConfirmPassword").keyup(ensurePasswordMatch);
	$("#txtNewPassword").keyup(ensurePasswordMatch);
});