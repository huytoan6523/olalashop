const LOAD_CONTENT_URL = '/user/cart/';
const UPDATE_PROUCT_ITEM_URL = '/user/cart/update-product-item';
const CHECKOUT_URL = '/user/checkout/'
$(document).ready(function() {
	loadContent();


});

function loadContent() {
	$.ajax({
		type: "POST",
		url: LOAD_CONTENT_URL,
		data: null,
		success: function(data) {
			loadCart(data);
		}
	});

}

// load productItem to table 

function loadCart(data) {
	$('.list-product-item').html('');
	if (data.length == 0) {
		$('.list-product-item').html(`
			<div class="empty-cart">Giỏ hàng trống </div>
			`
		);
	} else {
		let html = ``;

		for (let i = 0; i < data.length; i++) {
			//image
			let e = data[i];
			let id = e.id;
			let imgUrl1 = e.product.productLine.imgUrl1;

			//name
			//console.log(imgUrl1);
			let productName = e.product.productLine.name;

			//attributes
			let attributes = e.product.productAttributes;
			//console.log(attributes);

			//price 
			let originalPrice = e.product.price;
			//.log('orgin '+originalPrice);
			let productPrice = parseFloat(e.product.price) * (1 - parseFloat(e.product.productLine.discount) / 100);
			//console.log('price: '+productPrice);

			//quantity 
			let quantity = e.quantity;
			//console.log('quantity: '+quantity); 

			// 
			let totalPrice = productPrice * quantity;

			// to display VNĐ
			originalPrice = originalPrice.toLocaleString('vi', { style: 'currency', currency: 'VND' });
			productPrice = productPrice.toLocaleString('vi', { style: 'currency', currency: 'VND' });
			totalPrice = totalPrice.toLocaleString('vi', { style: 'currency', currency: 'VND' });

			//stock
			let stock = e.product.stock;

			let eHTML =
				`
			<div class="d-flex flex-row justify-content-between align-items-center p-2 bg-white mt-2 px-3 ">
				<div class="cart-description">
                    	<div class="cart-checkbox">
                    		<input class="checkmark product-item-checkbox" type="checkbox" name="${id}">
                    	</div>
                      <!-- image -->
                        <div class="mr-1"><img  src="${imgUrl1}" width="100">
                        	 
                        </div>
                        
                         <!--  Name -->
                        <div class="d-flex flex-column align-items-start product-details"><span class="font-weight-bold item-name">${productName}</span>
                        	<div class="d-flex flex-row product-desc">
                               
                    `;

			for (let j = 0; j < attributes.length; j++) {

				let att = attributes[j];
				eHTML = eHTML +
					`
				<div class="size mr-1"><span class="text-grey">${att.attribute.name}:</span><span class="font-weight-bold">&nbsp;${att.value}</span></div>
                         
					`
			}



			eHTML = eHTML +
				`              
                         </div>
                        	
                        </div>
                        
                    </div>
                        
                        <!-- Price per Item -->
                        <div class="price" style="width: 15%;">
                        	<div class="original_price">${originalPrice}</div>
                            <div class="product_price">${productPrice}</div>
                        </div>
                        
                     <!--ProductItemId --> 
                       <input type="hidden" class="produc-item-id" value ="${id}"/>
                     
                       <!--Stock --> 
                       <input type="hidden" class="stock" value ="${stock}"/>
                        
                        <!-- Quantity -->
                        <div class="d-flex flex-rowalign-items-start qty"><span class="icon-minus" ><i class="fa fa-minus text-danger"></i></span>
                            <input style="width: 60px; " class="text-grey mt-1 mr-1 ml-1 quantity-value" value="${quantity}"/><span class="icon-plus"><i class="fa fa-plus text-success"></i><span>
                        </div>
                            
                         <!-- total Price Price -->
                        <div style="width: 10%;">
                            <div class="total_product_price" >${totalPrice}</div>
                        </div>
                        
                        <!--  Remove -->
                        <div class="d-flex align-items-center"><a href="/user/cart/delete-product-item?id=${id}" class="remove-item-link"><i class="fa fa-trash mb-1 text-danger remove-icon"></i></a></div>
					</div>
					`;
			html = html + eHTML;


		}

		$('.list-product-item').html(html);

		handleChangeQuantity();
		hanldeCheckout();
	}
	//set total product Item lable
	$('#count-product-item-lable').text(data.length);
}

function handleChangeQuantity() {
	$('.icon-minus').click(function() {
		let parent = $(this).parent();
		let id = parent.parent().find('.produc-item-id').val();
		let quantity_input = parent.find('.quantity-value');
		let quantity = parseInt(quantity_input.val());
		if (quantity > 1) {
			quantity_input.val(quantity - 1);
			updateProductItem(id, quantity - 1);
		}

	});


	$('.icon-plus').click(function() {
		let parent = $(this).parent();
		let id = parent.parent().find('.produc-item-id').val();
		let quantity_input = parent.find('.quantity-value');
		let quantity = parseInt(quantity_input.val());
		let stock_input = parent.parent().find('.stock');
		let stock = parseInt(stock_input.val());

		if (quantity + 1 > stock) {
			alert("Tối đa " + stock + " sản phẩm");
			quantity_input.val(stock);
		} else {
			quantity_input.val(quantity + 1);
			updateProductItem(id, quantity + 1);
		}


	});

	$('.quantity-value').blur(function() {
		let parent = $(this).parent();
		let id = parent.parent().find('.produc-item-id').val();
		let quantity_input = parent.find('.quantity-value');
		let quantity = parseInt(quantity_input.val());
		if (quantity) {

			if (quantity < 1) {
				alert("Vui lòng nhập đúng giá trị");
			} else {

				let stock_input = parent.parent().find('.stock');
				let stock = parseInt(stock_input.val());

				if (quantity > stock) {
					alert("Tối đa " + stock + " sản phẩm");
					quantity_input.val(stock);
				} else {
					quantity_input.val(quantity);
					updateProductItem(id, quantity);
				}
			}
		}


		else {
			alert("Vui lòng nhập đúng giá trị");
			quantity_input.val(1);
		}

	})
}

function updateProductItem(id, quantity) {
	//console.log('id'+id);
	//console.log('quantity: '+quantity);
	let productItem = new Object();
	productItem.id = id;
	productItem.quantity = quantity;

	$.ajax({
		type: "POST",
		url: UPDATE_PROUCT_ITEM_URL,
		data: productItem,
		success: function(data) {
			if (data == "success") {
				loadContent();
			}
		}
	});

}

function hanldeCheckout() {
	// Listen for click on toggle checkbox
	$('#select-all').click(function(event) {
		if (this.checked) {
			// Iterate each checkbox
			$(':checkbox').each(function() {
				this.checked = true;
			});
		} else {
			$(':checkbox').each(function() {
				this.checked = false;
			});
		}
	});


	$('#select-all2').click(function(event) {
		if (this.checked) {
			// Iterate each checkbox
			$(':checkbox').each(function() {
				this.checked = true;
			});
		} else {
			$(':checkbox').each(function() {
				this.checked = false;
			});
		}
	});



	$('.checkout-btn').click(function() {
		var arr = [];
		let total = 0;
		$('input.product-item-checkbox:checkbox:checked').each(function() {
			arr.push($(this).parent().parent().parent().find('.produc-item-id').val());
			total += parseInt($(this).parent().parent().parent().find('.total_product_price').text());
		});

		total = total * 1000;
		total = total.toLocaleString('vi', { style: 'currency', currency: 'VND' });
		//total productItem
		$('.total-selected-product-item').text(arr.length);

		//total price
		$('.total-price').text(total);

		if (arr.length == 0) {
			alert('Vui lòng chọn ít nhất một mặt hàng!!!');
		} else {
			$('#checkout-form').submit();
		}

	});

	$('.checkmark').change(function() {
		var arr = [];
		let total = 0;
		$('input.product-item-checkbox:checkbox:checked').each(function() {
			arr.push($(this).parent().parent().parent().find('.produc-item-id').val());
			total += parseInt($(this).parent().parent().parent().find('.total_product_price').text());
		});
		total = total * 1000;
		total = total.toLocaleString('vi', { style: 'currency', currency: 'VND' });

		//total productItem
		$('.total-selected-product-item').text(arr.length);

		//total price
		$('.total-price').text(total);

	});

}

