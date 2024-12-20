/**
 * 
 */

// lock account by select box 
document.addEventListener("DOMContentLoaded", function() {
	
	// retrive the single select box 
	const chooseAll = document.getElementById('selectAllUsers');
	
	// retrive all select checkbox 
	const selectionBoxes = document.querySelectorAll('.checkboxUser');
	
	// retrive the submit button 
	const switchSelectedButton = document.getElementById('toggleSelected');

	
	// select single box listener
	// it check the box when user click
	chooseAll.addEventListener('change', function() {
		selectionBoxes.forEach(selectbox => {
			selectbox.checked = chooseAll.checked;
		});
	});
	
	// submit button listener 
	// 1 case (retrieve the user information by user select)
	switchSelectedButton.addEventListener('click', function() {
		const userIdentity = Array.from(selectionBoxes)
		
			// only the pick item are selected
			.filter(currentBox => currentBox.checked)
			
			// retrive the data from the select box 
			.map(currentBox => currentBox.value);
	
		
		// if not user select it will print the message
		if (userIdentity.length === 0) {
			bootbox.alert('No user is seleted.');
			return;
		}
		
		// retrieve the row information user select 
		const currentSelect = document.querySelector(`tr input[value="${userIdentity[0]}"]`).closest('tr');
		
		// check if current user is enable or not 
		const checkCurrent = currentSelect.querySelector('td:nth-child(7)').textContent.trim() === 'true';
		
		// check current status(lock or unlock) of the user
		const operation = checkCurrent ? 'lock' : 'unlock';
		
		// pop up message that user want to lock or unlock the user 
		const approvalPrompt = `Do you really want to ${operation} this user(s) profile?`;

		
		// apply bootbox when submit button is click   
		bootbox.confirm({
			message: approvalPrompt,
			buttons: {
				confirm: {
					label: 'Verifies',
					className: 'btn-success'
				},
				cancel: {
					label: 'Exit',
					className: 'btn-danger'
				}
			},
			callback: function(validated) {
				if (validated) {
					
					// retrive the the current lock status of (lockUsers/unlockUsers)
					const link = checkCurrent ? '/admin/lockUsers' : '/admin/unlockUsers';

					// pass the data to the front end 
					fetch(link, {
						method: 'PUT',
						headers: {
							'Content-Type': 'application/json'
						},
						body: JSON.stringify({ userIds: userIdentity })
					})
						.then(reply => {
							if (reply.ok) {
								
								// create the bootbox stye for update successufykkt
								const completionMessage = 'User account has been successfully updated.';
								bootbox.alert(completionMessage);

								userIdentity.forEach(identification => {
									
									// for all the data user select 
									const entry = document.querySelector(`tr input[value="${identification}"]`).closest('tr');
									
									// check the status(lock/unlock)
									if (checkCurrent) {
										
										// update the status of table condition to false 
										entry.querySelector('td:nth-child(7)').textContent = 'false';
										
										// for the button user click 
										const trigger = entry.querySelector('button');
										
										// change the lock status to red 
										trigger.classList.remove('btn-success');
										trigger.classList.add('btn-danger');
										
										// change the icon image of lock status 
										trigger.querySelector('span.fa').classList.remove('fa-unlock');
										trigger.querySelector('span.fa').classList.add('fa-lock');
										
										// change the lock status of the word to lock 
										trigger.querySelector('span:last-child').textContent = ' Lock';
										
									} else {
										// update the status of table condition to true 
										entry.querySelector('td:nth-child(7)').textContent = 'true';
										
										// for the button user click 
										const trigger = entry.querySelector('button');
										
										// change the lock status to green 
										trigger.classList.remove('btn-danger');
										trigger.classList.add('btn-success');
										
										// change the icon image of lock status 
										trigger.querySelector('span.fa').classList.remove('fa-lock');
										trigger.querySelector('span.fa').classList.add('fa-unlock');
										
										// change the lock status of the word to unlock 
										trigger.querySelector('span:last-child').textContent = ' Unlock';
									}
								});
							} else {
								// show the text where the account is not lock 

								bootbox.alert('Update failed for user(s) status.');
							}
						})
						.catch(error => {
							// print out the console on webpage and show on html error  
							console.error('Error:', error);
							bootbox.alert('Error encountered during update for user(s) status.');
						});
				}
			}
		});
	});
});


// lock account by click the button(one user at time)
document.addEventListener("DOMContentLoaded", function() {

	// retrive the user information from html
	document.querySelectorAll('.toggle-user').forEach(function(button) {

		// set the button listener 
		button.addEventListener('click', function(event) {

			// prevent default enter from js when button click 
			event.preventDefault();

			// retrive user id 
			let identity = this.getAttribute('data-user-id');
			if (!identity) {
				console.error('No User ID provided');
				return;
			}
			// retrive button location 
			const entity = this.closest('tr');

			// check is acitive or not for the buttoon 
			const isActived = entity.querySelector('td:nth-child(7)').textContent.trim() === "true";

			// retrive the lock and unclick condition  
			const task = isActived ? 'lock' : 'unlock';

			// confirm message 
			const approvalPrompt = `Do you really want to ${task} this user profile?`;

			// set up the look for 
			bootbox.confirm({
				message: approvalPrompt,
				buttons: {
					confirm: {
						label: 'Approve',
						className: 'btn-success'
					},
					cancel: {
						label: 'Terminate',
						className: 'btn-danger'
					}
				},
				callback: function(Validate) {
					if (Validate) {

						// if (active when is lock) else (lock when is active) 
						const link = isActived ? `/admin/lockUser/${identity}` : `/admin/unlockUser/${identity}`;

						// pass to the frontend 
						fetch(link, {
							method: 'PUT',
							headers: {
								'Content-Type': 'application/json'
							}
						})
							.then(reply => {
								if (reply.ok) {
									// update successful message 
									const confirmationMessage = 'The update to the user account was successful.';
									bootbox.alert(confirmationMessage);

									if (isActived) {
										// update the status of table condition to false 
										entity.querySelector('td:nth-child(7)').textContent = "false";

										// change the lock status to red 
										button.classList.remove('btn-success');
										button.classList.add('btn-danger');

										// change the icon of lock status 
										button.querySelector('span.fa').classList.remove('fa-unlock');
										button.querySelector('span.fa').classList.add('fa-lock');

										// change the lock status of the word to lock 
										button.querySelector('span:last-child').textContent = ' Lock';
									} else {

										// update the status of table condition to true 
										entity.querySelector('td:nth-child(7)').textContent = "true";

										// change the lock status to green 
										button.classList.remove('btn-danger');
										button.classList.add('btn-success');

										// change the icon of unlock status 
										button.querySelector('span.fa').classList.remove('fa-lock');
										button.querySelector('span.fa').classList.add('fa-unlock');

										// change the lock status of the word to unlock 
										button.querySelector('span:last-child').textContent = ' Unlock';
									}
								} else {
									// show the text where the account is not lock 
									const failureMessage = isActived ? 'User account could not be locked.' : 'Unable to unlock the user account.';
									bootbox.alert(failureMessage);
								}
							})
							.catch(error => {
								// print out the console on webpage and show on html error  
								console.error('Error:', error);
								bootbox.alert('Something went wrong.');
							});
					}
				}
			});
		});
	});
});









// delete user by click the button
$(document).ready(function() {
	
	// listener for delete single shirt button
	$('.delete-shirt').on('click', function() {
		
		// set the path of @{/} so it fit with html format 
		var route = /*[[@{/}]]*/'remove';

		// retrive the id from html for the shirt item 
		var identity = $(this).attr('id');

		// set up the bootbox for the single delete button 
		bootbox.confirm({
			message: "Are you certain you want to remove this shirt? This action cannot be undone.",
			buttons: {
				cancel: {
					label: '<i class="fa fa-times"></i> Cancel'
				},
				confirm: {
					label: '<i class="fa fa-check"></i> Confirm'
				}
			},
			// after user has interact with bootbox 
			callback: function(validated) {
				if (validated) {
					// send a post request to the /remove 
					$.post(route, { 'id': identity }, function() {
						location.reload();
					});
				}
			}
		});
	});


	// delete the user by select box 
	$('#deleteSelected').click(function() {
		
		// retrive all row of user informtion 
		var identity = $('.checkboxShirt');
		
		
		
		var identityList = [];
		for (var i = 0; i < identity.length; i++) {
			if (identity[i].checked == true) {
				identityList.push(identity[i]['id'])
			}
		}

		//  set the path of @{/} so it fit with html format 
		var link = /*[[@{/}]]*/'removeList';

		// bootbox applciation
		bootbox.confirm({
			message: "Do you really want to delete all selected shirts? This change is irreversible.",
			buttons: {
				cancel: {
					label: '<i class="fa fa-times"></i> Cancel'
				},
				confirm: {
					label: '<i class="fa fa-check"></i> Confirm'
				}
			},
			// interaction completed on bootbox and pass the information to the frontend 
			callback: function(approved) {
				if (approved) {
					$.ajax({
						type: 'POST',
						url: link,
						data: JSON.stringify(identityList),
						contentType: "application/json",
						success: function() {
							location.reload()
						},
						error: function() {
							location.reload();
						}
					});
				}
			}
		});
	});


	// check or uncheck all listener 
	$("#selectAllShirts").click(function() {
		if ($(this).prop("checked") == true) {
			$(".checkboxShirt").prop("checked", true);
		} else if ($(this).prop("checked") == false) {
			$(".checkboxShirt").prop("checked", false);
		}
	})
});