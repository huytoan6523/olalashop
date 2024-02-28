const LOAD_CONTENT_URL = '/employee/findByOrderInfo';
const UPDATE_STATUS_URL = '/employee/updateOrderStatus';
const VIEW_DETAIL_ORDER = '/employee/viewDetailOrder';
let DEFAULT_PAGE_NUMBER = 0;
let DEFAULT_PAGE_SIZE = 10;
let TABLE_HEADER = `
    		<tr>
    			<th>ID</th>
    			<th>Mã KH</th>
    			<th>Giá sản phẩm</th>
    			<th>Phí vận chuyển</th>
    			<th>Tổng thanh toán</th>
    			<th>Đã thanh toán</th>
    			<th>Trạng thái</th>
    			<th>Action</th>
    			</tr>`;

let DETAIL_ORDER_HEADER = `
    		<tr>
    			<th>ID</th>
    			<th>Hình ảnh</th>
    			<th>Tên sản phẩm</th>
    			<th>Giá Tiền</th>
    			<th>Số lượng</th>
    			<th>Thành tiền</th>
    			</tr>`
$(document).ready(function() {

	loadCurrentDate();

	//load Claim By Default
	loadData(DEFAULT_PAGE_NUMBER);

	$('#search-form').submit(function(event) {
		event.preventDefault();
		//loading animation
		$(".loader").css("display", "block");
		resetPagingBlock();
		loadData(DEFAULT_PAGE_NUMBER);
	});;


	$('.status-option').change(function() {
		$(".loader").css("display", "block");
		resetPagingBlock();
		loadData(DEFAULT_PAGE_NUMBER);
	});

	$('#from-date').change(function() {
		$(".loader").css("display", "block");
		resetPagingBlock();
		loadData(DEFAULT_PAGE_NUMBER);
	});


	$('#to-date').change(function() {
		$(".loader").css("display", "block");
		resetPagingBlock();
		loadData(DEFAULT_PAGE_NUMBER);
	});

});


function loadCurrentDate() {
	var date = new Date();

	var day = date.getDate();
	var month = date.getMonth() + 1;
	var year = date.getFullYear();

	if (month < 10) month = "0" + month;
	if (day < 10) day = "0" + day;

	var today = year + "-" + month + "-" + day;
	$('#from-date').val(today);
	$('#to-date').val(today);
}

function loadData(pageNumber) {
	//reset error-msg
	//	$('.error-msg').text('');
	//reset table
	$('#data-table').html('');
	$.ajax({
		type: "POST",
		url: LOAD_CONTENT_URL,
		data: $('#search-form').serialize(),
		success: function(page) {
			//handle if no data 
			let totalPages = page.totalPages;

			if (totalPages == 0) {
				setTimeout(handleNoResult, 200);
			} else {
				//loading animation
				$(".loader").css("display", "block");
				setTimeout(function() { loadDataToTable(page); pagingData(page); }, 150);
			}
		}
	});
}

function loadDataToTable(page) {

	$(".loader").css("display", "none");

	$('#data-table').html(`
		<thead></thead>
		<tbody></tbody>
	`);
	$('#data-table > thead').html(TABLE_HEADER);
	$("#data-table > tbody").html("");
	let list = page.content;
	for (let i = 0; i < list.length; i++) {


		var order = list[i];
		var id = order.id;
		var userId = order.users.id;


		var productPrice = order.productPrice;
		productPrice = productPrice.toLocaleString('vi', { style: 'currency', currency: 'VND' });

		var deliveryFee = order.deliveryFee;
		deliveryFee = deliveryFee.toLocaleString('vi', { style: 'currency', currency: 'VND' });

		var totalAmount = order.totalAmount;
		totalAmount = totalAmount.toLocaleString('vi', { style: 'currency', currency: 'VND' });

		var paidAmount = order.paidAmount;
		paidAmount = paidAmount.toLocaleString('vi', { style: 'currency', currency: 'VND' });

		var status = order.status;

		let html =
			`<tr>
			<td class="id">${id}</td>
			<td>${userId}</td>
			<td>${productPrice}</td>
			<td>${deliveryFee}</td>
			<td class="total-amount">${totalAmount}</td>
			<td class="paid-amount">${paidAmount}</td>
			`;

		if (status == 1) {
			html += `
			<td>
			 <select class="status-option">
			 	<option value="1" selected>Chờ xử lý</option>
			 	<option value="2" >Đang vận chuyển</option>
			 	<option value="3" >Đã giao</option>
			 	<option value="4" >Hoàn trả</option>
			 </select>
			</td>
			`
		} else if (status == 2) {
			html += `
			<td>
			 <select class="status-option">
			 	<option value="1" >Chờ xử lý</option>
			 	<option value="2" selected>Đang vận chuyển</option>
			 	<option value="3" >Đã giao</option>
			 	<option value="4" >Hoàn trả</option>
			 </select>
			</td>
			`
		} else if (status == 3) {
			html += `
			<td>
			 <select class="status-option">
			 	<option value="1" >Chờ xử lý</option>
			 	<option value="2" >Đang vận chuyển</option>
			 	<option value="3" selected>Đã giao</option>
			 	<option value="4" >Hoàn trả</option>
			 </select>
			</td>`
		} else {
			html += `
			<td>
			 <select class="status-option">
			 	<option value="1" >Chờ xử lý</option>
			 	<option value="2" >Đang vận chuyển</option>
			 	<option value="3" >Đã giao</option>
			 	<option value="4" selected>Hoàn trả</option>
			 </select>
			</td>
			`
		}

		html += `
			</td>
			<td data-label="Xem Sp" class="right__iconTable view-detail-order">
            <img src="/markup/admin/assets/eyes.svg" alt="" style="width: 30px;"></td>
		</tr>`;

		$("#data-table > tbody").append(html);
	}

	changeStatus();
	viewDetailOrder();
}

function pagingData(page) {

	let totalPages = page.totalPages;
	$('#paging-block').twbsPagination({
		initiateStartPageClick: false,
		hideOnlyOnePage: false,
		totalPages: totalPages,
		visiblePages: 3,
		// Text labels
		first: 'Đầu',
		prev: 'Trước',
		next: 'Tiếp',
		last: 'Sau',
		onPageClick: function(event, pageNumber) {
			loadData(pageNumber - 1);
		}
	});
}

function resetPagingBlock() {
	$('#paging-block').empty();

	$('#paging-block').removeData("twbs-pagination");

	$('#paging-block').unbind("page");
}

function handleNoResult() {
	$(".loader").css("display", "none");
	$('#data-table').html(`
		<div class="no-result-msg">No Result</div>
	`);
}

function changeStatus() {
	$('.status-option').change(function() {
		let orderInfo = new Object();
		let status = $(this).val();
		let id = $(this).parent().parent().find('.id').text();

		orderInfo.id = id;
		orderInfo.status = status;


		if (status == 3) {
			let amount = $(this).parent().parent().find('.total-amount').text();
			let x = $(this).parent().parent().find('.paid-amount').val(amount);
		}
		$.ajax({
			type: "POST",
			url: UPDATE_STATUS_URL,
			data: orderInfo,
			success: function(message) {
				if (message == 'success') {
					alert("Cập nhật thành công!");
				} else {
					alert("Có lỗi xảy ra!");
				}
			}
		});
	});
}


function viewDetailOrder() {
	$('.view-detail-order').click(function() {
		let idElement = $(this).parent().find('td.id');
		let id = idElement.text();
		//get Detail Orders
		$.ajax({
			type: "POST",
			url: VIEW_DETAIL_ORDER + '/' + id,
			data: null,
			success: function(order) {
				showDetailOrder(order);
			}
		});

	});
}

/* SHOW DETAIL ORDER */
function showDetailOrder(order) {

	console.log(order);
	//setId
	$('.order-id-span').text(order.id);



	//set Customer 
	$('.customer-id-span').text('Mã KH: ' + order.users.id + ' - ' + order.users.fullName);
	$('#detail-order-modal').modal('show');

	let data = order.productItems;
	$('#order-detail-table').html('');
	
	let html = ``;
	
	// title 
	html+= `
		<div class="d-flex flex-row justify-content-between align-items-center p-2 bg-white mt-2 px-3 header-detail-order"
			style="text-align: center;">
				<div class="cart-description" style=" 
				display: flex;
				margin-left: 10px; font-size: 18px;
			    width: 40%;">
                      Sản phẩm
                 </div>
                        
                        <!-- Price per Item -->
                        <div class="price" style="width: 20%;">
                       <!-- 	<div class="original_price">55&nbsp;₫</div> -->
                            <div class="product_price header-detail-order" style="width: 100px;">Giá tiền</div>
                        </div>
                        
                        <!-- Quantity -->
                        <div class="d-flex flex-rowalign-items-start qty" style="width: 20%">
                            <div class="product_price header-detail-order" style="width: 100px;">Số lượng</div>
                        </div>
                            
                         <!-- total Price Price -->
                        <div style="width: 20%;">
                            <div class="product_price header-detail-order"style="width: 100px;">Thành tiền</div>
                        </div>
					</div>
	`

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
			<div class="d-flex flex-row justify-content-between align-items-center p-2 bg-white mt-2 px-3 "
				style="text-align: center;">
				<div class="cart-description" style="display: flex; width: 40%;">
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
	                        <div class="price" style="width: 20%;">
	                       <!-- 	<div class="original_price">${originalPrice}</div> -->
	                            <div class="product_price" style="width: 100px;">${productPrice}</div>
	                        </div>
	                        
	                     <!--ProductItemId --> 
	                       <input type="hidden" class="produc-item-id" value ="${id}"/>
	                     
	                       <!--Stock --> 
	                       <input type="hidden" class="stock" value ="${stock}"/>
	                        
	                        <!-- Quantity -->
	                        <div class="d-flex flex-rowalign-items-start qty" style="width: 20%;">
	                            <div class="product_price" style="width: 100px;">${quantity}</div>
	                        </div>
	                            
	                         <!-- total Price Price -->
	                        <div style="width: 20%;">
	                            <div class="product_price" style="width: 100px;">${totalPrice}</div>
	                        </div>
	                      </div>
					`;
		html = html + eHTML;
		$('#order-detail-table').html(html);
		
		
		//SHIP FEE
		let shipFee = order.deliveryFee;
		shipFee =  shipFee.toLocaleString('vi', { style: 'currency', currency: 'VND' });
		$('.shipFee-id-span').text(shipFee);
		
		let totalAmount = order.totalAmount;
		totalAmount =  totalAmount.toLocaleString('vi', { style: 'currency', currency: 'VND' });
		$('.totalAmount-id-span').text(totalAmount);
		
		let paidAmount = order.paidAmount;
		paidAmount =  paidAmount.toLocaleString('vi', { style: 'currency', currency: 'VND' });
		$('.paidAmount-id-span').text(paidAmount);
		
		let product_price = order.productPrice;
		product_price =  product_price.toLocaleString('vi', { style: 'currency', currency: 'VND' });
		$('.productPrice-id-span').text(product_price);
	}
}

/* END  DETAIL ORDER */