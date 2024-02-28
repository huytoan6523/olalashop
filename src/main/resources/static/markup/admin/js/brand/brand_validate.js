$(document).ready(function() {

	$('#add-brand-form').validate({
		errorElement: 'lable',
		errorClass: 'error',
		rules: {
			name: {
				required: true,
			},
		},

		// messages: {
		//     name: {
		//         required: 'msg'
		//     },
		//     email: {
		//         required: "msg"
		//     }
		// },
		errorClass: "error",
		validClass: "valid success-alert"
	});
	
	//handle validate from Server
	
	$('input').focus(function() {
		$('.error').text('');
	});
});