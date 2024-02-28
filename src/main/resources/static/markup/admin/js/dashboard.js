const LOAD_CONTENT_URL = '/admin/dashboard';
let TABLE_HEADER = `
    		<tr>
    			<th>ID</th>
    			<th>Tên sản phẩm</th>
    			<th>Hình ảnh</th>
    			<th>Giá sản phẩm</th>
    			<th>Đã bán</th>
    			<th>Tổng doanh thu</th>
    			</tr>`;
$(document).ready(function() {

	loadCurrentDate();

	loadData();
	
	search();
	
	$('#search-form').submit(function(event) {
		loadData();
	});;
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

function loadData() {
	$('#data-table').html('');
	$.ajax({
		type: "POST",
		url: LOAD_CONTENT_URL,
		data: $('#search-form').serialize(),
		success: function(list) {
			setTimeout(function() { loadDataToTable(list); }, 150);
		}
	});
}

function loadDataToTable(list) {

	$(".loader").css("display", "none");

	$('#data-table').html(`
		<thead></thead>
		<tbody></tbody>
	`);
	$('#data-table > thead').html(TABLE_HEADER);
	$("#data-table > tbody").html("");
	let total = 0;
	for (let i = 0; i < list.length; i++) {

		var obj = list[i];
		var id = obj.id;
		var productName = obj.productName;
		var imgUrl1 = obj.imgUrl1;
		var price = obj.price;
		
		var sold = parseInt(obj.sold);
		var totalIncome = parseInt(obj.totalIncome);
		total+=totalIncome;
		price = price.toLocaleString('vi', { style: 'currency', currency: 'VND' });
		totalIncome = totalIncome.toLocaleString('vi', { style: 'currency', currency: 'VND' });

		let html =
			`<tr>
			<td>${id}</td>
			<td>${productName}</td>
			<td><img src="${imgUrl1}"/></td>
			<td>${price}</td>
			<td>${sold}</td>
			<td>${totalIncome}</td>
			`;
			
		$('#data-table').append(html);
	}
	
	
	total = total.toLocaleString('vi', { style: 'currency', currency: 'VND' });
	let html =
			`<tr>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td>Tổng: </td>
			<td>${total}</td>
			`;
	$('#data-table').append(html);
}

function search(){
	$('.sort-option').change(function(){
		loadData();
	});
	
	$('#from-date').change(function(){
		loadData();
	});
	
	$('#to-date').change(function(){
		loadData();
	});
}

/* END  DETAIL ORDER */