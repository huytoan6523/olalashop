$(document).ready(function() {

	$('#add-productLine-form').validate({
		errorElement: 'lable',
		errorClass: 'error',
		rules: {
			name: {
				required: true,
			},
			discount: {
				required: true,
			},
			categoryId: {
				required: true,
			},
			brandId: {
				required: true,
			},
			imgFile1: {
				required: true,
			},
			imgFile2: {
				required: true,
			},
			imgFile3: {
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