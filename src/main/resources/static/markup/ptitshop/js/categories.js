const LOAD_CONTENT_URL = '/visitor/productLine/view/'
const PAGE_SIZE = 8;
const PAGE_NUMBER = 0;


$(document).ready(function() {
	console.log("??");
	initContent();
	
	$('input[type=checkbox]').click(function(){
		resetPagingBlock();
		getContent(PAGE_NUMBER);
	});
	
	$('input[type=radio]').click(function(){
		resetPagingBlock();
		getContent(PAGE_NUMBER);
	});
	
	$('#product-line-search').click(function() {
		resetPagingBlock();
		getContent(PAGE_NUMBER);
	});
	
	$('#filter-btn').click(function(){
		resetPagingBlock();
		getContent(PAGE_NUMBER);
	});
});

function initContent(){
		resetPagingBlock();
		getContent(PAGE_NUMBER);
}

function getContent(pageNumber) {
	productLineInfo = new Object();
	
	let parentCategoryId = $("#parentCategoryId").val();

	let keyword = $('#keyword').val();


	let categoryIds = [];
	$("input[type=checkbox]:checked").each(function() {
		categoryIds.push($(this).val());
	});


	let orderBy = $('input[name="orderBy"]:checked').val();


	let minPrice = $('#minPrice').val();
	let maxPrice = $('#maxPrice').val();

	productLineInfo.keyword = keyword;
	productLineInfo.categoryIds = categoryIds;
	productLineInfo.orderBy = orderBy;
	productLineInfo.minPrice = minPrice;
	productLineInfo.maxPrice = maxPrice;
	productLineInfo.parentCategoryId = parentCategoryId;
	productLineInfo.pageSize = PAGE_SIZE;
	productLineInfo.pageNumber = pageNumber;
	
//	console.log(productLineInfo);
	$.ajax({
		type: "POST",
		url: LOAD_CONTENT_URL,
		data: productLineInfo,
		success: function(pageData) {
			updateContent(pageData);

			//resetPagingBlock();

			pagingData(pageData);
		}
	});
}


function updateContent(pageData) {
	$('.product-grid').html('');
	let arr = pageData.content;

	var content = '';
	for (let i = 0; i < arr.length; i++) {
		let e = arr[i];
		
		let minPrice = parseFloat(e.minPrice);
		let maxPrice = parseFloat(e.maxPrice);
		let discount = parseFloat(e.discount)/100;
		//HTML for each element
		let eHTML = `
	
			<div class="product-item men">
			<div class="product discount product_filter">
				<div class="product_image">
					<img src="${e.imgUrl1}"  alt="">
				</div>
				<div class="favorite favorite_left"></div>
				<div class="product_bubble product_bubble_right product_bubble_red d-flex flex-column align-items-center"><span>-${discount*100}%</span></div>
				<div class="product_info">
					<h6 class="product_name"><a href="/product/view-by-product-line?productLineId=${e.id}">${e.name}</a></h6>
					
					<!-- MIN MAX PRICE  -->
					<div class="discount-product_price">${minPrice}đ - ${maxPrice}đ</div>
					<div class="product_price">${minPrice*(1-discount)}đ - ${maxPrice*(1-discount)}đ</div>
					
					
					<!-- SOLD -->
					<span>Đã bán: </span> 
					<span>${e.totalSold}</span>
					
					
				</div>
			</div>
			<div class="red_button add_to_cart_button"><a href="/product/view-by-product-line?productLineId=${e.id}">Add to card</a></div>
		</div> `


		content = content + eHTML;
	};
	$('.product-grid').html(content);
}

function pagingData(page) {

	let totalPages = page.totalPages;
	$('#paging-block').twbsPagination({
		initiateStartPageClick: false,
		hideOnlyOnePage: false,
		totalPages: totalPages,
		visiblePages: 3,
		onPageClick: function(event, pageNumber) {
			
			getContent(pageNumber-1);
		}
	});
}

function resetPagingBlock() {
	$('#paging-block').empty();

	$('#paging-block').removeData("twbs-pagination");

	$('#paging-block').unbind("page");
}