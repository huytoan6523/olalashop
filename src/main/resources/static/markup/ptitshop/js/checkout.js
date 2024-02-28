const GET_PROVINCE_URL = 'https://online-gateway.ghn.vn/shiip/public-api/master-data/province';
const GET_DISTIRCT_URL = 'https://online-gateway.ghn.vn/shiip/public-api/master-data/district';
const GET_WARD_URL = 'https://online-gateway.ghn.vn/shiip/public-api/master-data/ward';
const ADD_DELIVERY_ADDRESS_URL = '/user/delivery-address/add'
const GET_DELIVERY_LIST_URL = '/user/delivery-address/'
const GET_DEFAULT_DELIVERY_ADDRESS = '/user/delivery-address/default'
const GET_DEFAULT_DELIVERY_BY_ID = '/user/delivery-address/find-by-id'
const FROM_DISTRICT_ID = 1542; // mo la- ha dong - ha noi
const GET_DELIVERY_FEE_URL = 'https://online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee?token=https://online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee';
const PAYMENT_URL = '/user/payment/';

var TOTAL_PRICE = 0;
var DELIVER_FEE = 0;
$(document).ready(function() {
	displayPrice();

	getDeliverList();
	
	ChangRadioButton();


	$('.change-location-link').click(function() {
		$('#myModal').modal();


	});

	$('.add-delivery-address-btn').click(function() {
		$('#myModal').modal('hide');

		$.ajax({
			url: GET_PROVINCE_URL,
			type: 'GET',
			dataType: 'json',
			headers: {
				'token': '606ae2a4-5da1-11ed-b824-262f869eb1a7',
			},
			contentType: 'application/json; charset=utf-8',
			success: function(result) {
				loadProvince(result.data);
			},
			error: function(error) {

			}
		});




		//show add-address-modal 
		$('#add-address-modal').modal();


	});

	//add-address
	addDeliveryAddress();


	//change address

	changeDeliveryAddress();


	//checkout

	checkout();

});


function loadProvince(data) {

	let html = ``;
	html += `<option value="">----</option>`;
	for (let i = 0; i < data.length; i++) {
		html +=
			`<option value=${data[i].ProvinceID}>${data[i].ProvinceName}</option>`
	}
	$('#provice-select').html(html);

	loadDistrictFromAPI();

}

function loadDistrictFromAPI() {


	$('#provice-select').change(function() {
		let provinceId = $('#provice-select').val();
		let data = new Object();
		data.province_id = provinceId;

		if (provinceId) {
			$.ajax({
				url: GET_DISTIRCT_URL,
				type: 'GET',
				dataType: 'json',
				data: data,
				headers: {
					'token': '606ae2a4-5da1-11ed-b824-262f869eb1a7',
				},
				contentType: 'application/json; charset=utf-8',
				success: function(result) {
					loadDistrictToSelect(result.data);
				},
				error: function(error) {

				}
			});
		}

	});
}

function loadDistrictToSelect(data) {
	let html = ``;
	html += `<option value="">----</option>`;
	for (let i = 0; i < data.length; i++) {
		html +=
			`<option value=${data[i].DistrictID}>${data[i].DistrictName}</option>`
	}
	$('#district-select').html(html);

	loadWardFormAPI();
}

function loadWardFormAPI() {
	$('#district-select').change(function() {
		let districtId = $('#district-select').val();

		let data = new Object();
		data.district_id = districtId;

		if (districtId) {
			$.ajax({
				url: GET_WARD_URL,
				type: 'GET',
				dataType: 'json',
				data: data,
				headers: {
					'token': '606ae2a4-5da1-11ed-b824-262f869eb1a7',
				},
				contentType: 'application/json; charset=utf-8',
				success: function(result) {
					loadWardToSelect(result.data);
				},
				error: function(error) {

				}
			});
		}

	});
}

function loadWardToSelect(data) {
	let html = ``;
	html += `<option value="">----</option>`;
	for (let i = 0; i < data.length; i++) {
		html +=
			`<option value=${data[i].WardCode}>${data[i].WardName}</option>`
	}
	$('#ward-select').html(html);


}

function addDeliveryAddress() {
	$('#add-delivery-btn').click(function() {

		let districtId = $('#district-select option:selected').val();
		let provinceId = $('#provice-select option:selected').val();
		let wardCode = $('#ward-select option:selected').val();

		let districtName = $('#district-select option:selected').text();
		let provinceName = $('#provice-select option:selected').text();
		let wardName = $('#ward-select option:selected').text();
		let detail = $('.address-detail').val();
		let isDefault = $('input[name="isDefaultAdress"]:checked').val();


		if (!districtName || !provinceName || !wardName || !detail) {
			alert('Vui lòng nhập đủ thông tin');
		}
		else {
			let deliveryAddress = new Object();
			deliveryAddress.provinceName = provinceName;
			deliveryAddress.districtName = districtName;
			deliveryAddress.wardName = wardName;
			deliveryAddress.districtId = districtId;
			deliveryAddress.provinceId = provinceId;
			deliveryAddress.wardCode = wardCode;
			deliveryAddress.addressDetail = detail;

			if (!isDefault) {
				isDefault = false;
			} else {
				isDefault = true;
			}

			//add delivery address
			$.ajax({
				type: "POST",
				url: ADD_DELIVERY_ADDRESS_URL + '?isDefault=' + isDefault,
				data: deliveryAddress,
				success: function(data) {
					$('#add-address-modal').modal('hide');
					getDeliverList();
				}
			});

		}
	});
}

function getDeliverList() {

	//get default delivery address
	$.ajax({
		type: "GET",
		url: GET_DEFAULT_DELIVERY_ADDRESS,
		data: null,
		success: function(data) {
			if (data) {
				$('#delivery_address_id').val(data.id);
				$('#province_id').val(data.provinceId);
				$('#district_id').val(data.districtId);
				$('#ward_code').val(data.wardCode);
				$('.location-infomation').text(data.addressDetail + ", " + data.wardName + ", " + data.districtName + ", " + data.provinceName);
				caculatorShipFee();
			}
			else {
				$('.location-infomation').text('Vui lòng thêm địa chỉ!');
			}

		}
	});

	$.ajax({
		type: "GET",
		url: GET_DELIVERY_LIST_URL,
		data: null,
		success: function(data) {
			$('#my-address').html();
			let html = ``;
			for (let i = 0; i < data.length; i++) {
				html += `
				 <div class="address-item">
						<input type="radio" name="deliveryAddress" value="${data[i].id}">
						${data[i].addressDetail},${data[i].wardName},${data[i].districtName},${data[i].provinceName}		  		
				 </div>
				`
			}

			$('#my-address').html(html);
		}
	});


}

function changeDeliveryAddress() {
	$('#change-deliver-address-btn').click(function() {
		let id = $('input[name="deliveryAddress"]:checked').val();
		if (!id) {
			alert('Vui lòng chọn 1 địa chỉ!!!');
		} else {
			//update default
			$.ajax({
				type: "GET",
				url: GET_DEFAULT_DELIVERY_BY_ID + '?id=' + id,
				data: null,
				success: function(data) {
					$('#myModal').modal('hide');
					if (data) {
						$('#delivery_address_id').val(data.id);
						$('#province_id').val(data.provinceId);
						$('#district_id').val(data.districtId);
						$('#ward_code').val(data.wardCode);
						$('.location-infomation').text(data.addressDetail + "," + data.wardName + "," + data.districtName + "," + data.provinceName);
						caculatorShipFee();
					}


				}


			});


		}
	});


}

function caculatorShipFee() {
	let totalPrice = TOTAL_PRICE;
	if (totalPrice >= 200000) {
		let zero = parseInt(0);
		zero = zero.toLocaleString('vi', { style: 'currency', currency: 'VND' });
		$('#delivery-fee').text(zero);
		updateFinalPrice();
	}

	else {
		let from_district_id = FROM_DISTRICT_ID;
		let to_district_id = $('#district_id').val();
		let to_ward_code = $('#ward_code').val();
		let heigh = 15;
		let length = 15;
		let weight = 2000;
		let width = 15;
		let service_id = 100039;
		let coupon = null;
		let insurance_value = $('#total-price').text();

		let shipModel = new Object();
		shipModel.from_district_id = from_district_id;
		shipModel.to_district_id = to_district_id;
		shipModel.to_ward_code = to_ward_code;
		shipModel.heigh = heigh;
		shipModel.length = length;
		shipModel.weight = weight;
		shipModel.width = width;
		shipModel.coupon = null;
		shipModel.service_id = service_id;
		shipModel.insurance_value = parseInt(insurance_value);

		//get Fee
		$.ajax({
			url: GET_DELIVERY_FEE_URL,
			type: 'GET',
			dataType: 'json',
			data: shipModel,
			headers: {
				'token': '606ae2a4-5da1-11ed-b824-262f869eb1a7',
			},
			contentType: 'application/json; charset=utf-8',
			success: function(result) {
				updateDeliveryFee(result);
				updateFinalPrice();
			},
			error: function(error) {
				$('#delivery-fee').text(0);
				updateFinalPrice();
			}
		});
	}
}


function updateDeliveryFee(result) {
	let deliverFee = parseInt(result.data.total);
	DELIVER_FEE = deliverFee;
	deliverFee = deliverFee.toLocaleString('vi', { style: 'currency', currency: 'VND' });
	$('#delivery-fee').text(deliverFee);
}

function updateFinalPrice() {
	let finalPrice = DELIVER_FEE + TOTAL_PRICE;
	finalPrice = finalPrice.toLocaleString('vi', { style: 'currency', currency: 'VND' });
	$('#final-price').text(finalPrice);
}



function checkout() {

	$('.checkout-btn').click(function() {
		let deliveryAddressId = $('#delivery_address_id').val();
		console.log('deliveryAddressId: ' + deliveryAddressId);

		let paymentType = $("input[type='radio'].payment_type:checked").val();

		let product_item_ids = $("input[name='product_item_id']").map(
			function() {
				return $(this).val();
			}).get();

		if (!paymentType) {
			alert('Vui lòng chọn phương thức thanh toán !');
		} else if (!deliveryAddressId) {
			alert('Vui lòng chọn địa chỉ giao hàng !');
		} else {
			let orderDto = new Object();
			orderDto.productItemIds = product_item_ids;
			orderDto.deliveryAddressId = deliveryAddressId;
			orderDto.paymentType = paymentType;

			//payment
			$.ajax({
				type: "POST",
				url: PAYMENT_URL,
				data: orderDto,
				success: function(x) {
					if (x.code === '00') {
						location.href = x.data;
					} else {
						alert(x.Message);
					}
				}
			});
		}

	});

}

/* TO DISPLAY VNĐ */
function displayPrice(){
	$(".original_price").map(function() {
	    let temp = parseInt($(this).text());
	    temp = temp.toLocaleString('vi', { style: 'currency', currency: 'VND' });
	    $(this).text(temp);
	});
	
	$(".product_price").map(function() {
	    let temp = parseInt($(this).text());
	    temp = temp.toLocaleString('vi', { style: 'currency', currency: 'VND' });
	    $(this).text(temp);
	});
	
	$(".total_product_price").map(function() {
	    let temp = parseInt($(this).text());
	    temp = temp.toLocaleString('vi', { style: 'currency', currency: 'VND' });
	    $(this).text(temp);
	});
	
	//totalPrice
	let temp = parseInt($("#total-price").text());
	TOTAL_PRICE = temp;
	temp = temp.toLocaleString('vi', { style: 'currency', currency: 'VND' });
	$('#total-price').text(temp);
}

function ChangRadioButton(){
	$('.lable-sort-type').click(function(){
		$(this).find('.attribute-value').attr('checked', 'checked');
	});
}