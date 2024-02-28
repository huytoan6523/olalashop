$(document).ready(function() {

	$('#add-productLine-form').validate({
		errorElement: 'lable',
		errorClass: 'error',
		rules: {
			name: {
				required: true,
			},
			categoryId: {
				required: true,
			},
			brandId: {
				required: true,
			},discount: {
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