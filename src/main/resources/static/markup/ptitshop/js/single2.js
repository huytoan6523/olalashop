const LOAD_CONTENT_URL='/visitor/product/view';

$( document ).ready(function() {
    getContent();
 
	addToCart();
	
	ChangRadioButton();
		
});

function getContent(){
	let values = [];
		$("input[type='radio']:checked").each(function(){
			values.push ($(this).val()) ;
		});	
		
		let currentValuesSize = values.length;
		
		let productLineId = $('#productLineId').val();
		
		let productInfo = new Object();
		productInfo.productLineId = productLineId;
		productInfo.values = values;
		
		$.ajax({
		type: "POST",
		url: LOAD_CONTENT_URL,
		data: productInfo,
		success: function(productInfo) {
			updateProductInfo(productInfo, currentValuesSize);
		}
	});
}

function updateProductInfo(productInfo, currentValuesSize){
	let id = productInfo.id;
	let price = parseFloat(productInfo.price);
	let minPrice = parseFloat(productInfo.minPrice);
	let maxPrice = parseFloat(productInfo.maxPrice);
	let stock = productInfo.stock;
	let discount = parseFloat(productInfo.discount)/100;
	let totalStock = productInfo.totalStock;
	let valuesSize = $('#valuesSize').val();	
	let acutalMinPrice = minPrice*(1-discount);
	let actualMaxPrice  = maxPrice*(1-discount);
	
	minPrice = minPrice.toLocaleString('vi', { style: 'currency', currency: 'VND' });
	maxPrice = maxPrice.toLocaleString('vi', { style: 'currency', currency: 'VND' });
	acutalMinPrice = acutalMinPrice.toLocaleString('vi', { style: 'currency', currency: 'VND' });
	actualMaxPrice = actualMaxPrice.toLocaleString('vi', { style: 'currency', currency: 'VND' });
	
	
	if( price ){ // id null or not ~ price null or not
		let productPrice = price*(1-discount);
		price = price.toLocaleString('vi', { style: 'currency', currency: 'VND' });
		productPrice = productPrice.toLocaleString('vi', { style: 'currency', currency: 'VND' });
		
		$('#productId').val(id);
		$('.original_price').text(price);
		$('.product_price').text(productPrice);
		$('#stock').text(stock);
		
	} else {
		$('#productId').val('');
		$('.original_price').text(minPrice+'-'+maxPrice);
		$('.product_price').text(acutalMinPrice+' - '+actualMaxPrice);
		
		if( valuesSize == currentValuesSize  ){
			$('#stock').text('0');
		} else {
			$('#stock').text(totalStock);
		}
		
	}	
	
	//reset Msg 
	$('#msg').text('');
}

function addToCart(){
	
	$('.add_to_cart_button').click(function(e){
		e.preventDefault();
		var href = $(this).find('a').attr('href');
		console.log(href);
		//quantity 
		let quantity = parseInt( $('#quantity_value').text() );
		let stock = parseInt($('#stock').text());
		
		let productId = $('#productId').val();
		
		//check productId IS NULL
		if( !productId ){
			$('#msg').text('Vui lòng chọn thuộc tính phù hợp');
		} else {
			if( stock =='0' || (quantity > stock) ){
				$('#msg').text('Vui lòng chọn số lượng hàng phù hợp');
			} else{
				href = href + '?productId='+productId+'&quantity='+quantity;
				location.href=href;
			}
		}	
	});
	//$("a").attr("href", "http://www.google.com/")
}

function ChangRadioButton(){
	$('.lable-sort-type').click(function(){
		console.log('okk');
		$(this).find('.attribute-value').attr('checked', 'checked');
		console.log('ok2');
		getContent();
	});
}
