let DEFAULT_PAGE_NUMBER = 0;
let DEFAULT_PAGE_SIZE = 5;

let TABLE_HEADER = `
    		<th>ID</th>
    			<th>Tên dòng sản phẩm</th>
    			<th>Hình ảnh</th>
    			<th>Mô tả</th>
    			<th>Sửa</th>
    			<th>Xóa</th>
    			<th>Xem sản phẩm</th>
    			<th>Thêm Sản Phẩm</th>
    			</tr>`

$(document).ready(function() {

	//load Claim By Default
	loadData(DEFAULT_PAGE_NUMBER);

	$('#search-form').submit(function(event) {
		event.preventDefault();

		//loading animation
		$(".loader").css("display", "block");
			resetPagingBlock();
			loadData(DEFAULT_PAGE_NUMBER);
	});
	$('#keyword').focus(function() {
		$('.error-msg').text('');
	});

});

function loadData(pageNumber) {
	//reset error-msg
	//	$('.error-msg').text('');
	//reset table
	$('#data-table').html('');

	let keyword = $('#keyword').val().trim();
	let sortType = $('#sortType').val();
	//let searchType = $('#searchType').val();
	let pageSize = $('#pageSize').val();

	var pageInfo = new Object();
	pageInfo.keyword = keyword;
	pageInfo.sortType = sortType;
	//	pageInfo.searchType = searchType;
	pageInfo.pageSize = pageSize;
	pageInfo.pageNumber = pageNumber;

	$.ajax({
		type: "POST",
		url: LOAD_DATA_URL,
		data: pageInfo,
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


		var obj = list[i];
		var id = obj.id;
		var name = obj.name;
		var imgUrl1  = obj.imgUrl1;
		var description = obj.description;

		$("#data-table > tbody").append(`
		<tr>
			<td>${id}</td>
			<td>${name}</td>
			<td><img src="${imgUrl1}"/></td>
			<td class="descrption">${description}</td>
			<td data-label="Sửa" class="right__iconTable"><a href="/admin/productLine/edit/${id}">
			<img src="/markup/admin/assets/icon-edit.svg" alt=""></a></td>
			
            <td data-label="Xoá" class="right__iconTable"><a href="/admin/productLine/delete/${id}">
            <img src="/markup/admin/assets/icon-trash-black.svg" alt=""></a></td>
            
            <td data-label="Xem Sp" class="right__iconTable"><a href="/admin/productLine/product/view/${id}">
            <img src="/markup/admin/assets/eyes.svg" alt=""></a></td>
            
            <td data-label="Thêm Sp" class="right__iconTable"><a href="/admin/productLine/product/add/${id}">
            <img src="/markup/admin/assets/plus.svg" alt=""></a></td>
		</tr>`);

	}
}

function pagingData(page) {

	let totalPages = page.totalPages;
	$('#paging-block').twbsPagination({
		initiateStartPageClick: false,
		hideOnlyOnePage: false,
		totalPages: totalPages,
		visiblePages: 3,
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