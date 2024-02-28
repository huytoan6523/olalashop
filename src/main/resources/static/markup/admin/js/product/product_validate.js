$(document).ready(function() {

	$('#add-product-form').validate({
		errorElement: 'lable',
		errorClass: 'error',
		rules: {
			name: {
				required: true,
			},
			price: {
				required: true,
				number: true,
			},
			stock: {
				required: true,
				number: true,
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