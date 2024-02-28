const LOAD_CONTENT_URL = '/visitor/productLine/bestseller/'
const PAGE_SIZE = 12;
const PAGE_NUMBER = 0;


$(document).ready(function() {
	initContent();
	updateProductList();
});

function initContent(){
		resetPagingBlock();
		getContent(PAGE_NUMBER);
}

function getContent(pageNumber) {
	pageInfo = new Object();
	
	pageInfo.pageNumber = pageNumber;
	pageInfo.pageSize = PAGE_SIZE;
	
	$.ajax({
		type: "POST",
		url: LOAD_CONTENT_URL,
		data: pageInfo,
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
		let discountMinPrice =  minPrice*(1-discount);
		let discountMaxPrice = maxPrice*(1-discount);
		
		minPrice = x.toLocaleString('it-IT', {style : 'currency', currency : 'VND'});
		maxPrice = x.toLocaleString('it-IT', {style : 'currency', currency : 'VND'});
		discountMinPrice = x.toLocaleString('it-IT', {style : 'currency', currency : 'VND'});
		discountMaxPrice = x.toLocaleString('it-IT', {style : 'currency', currency : 'VND'});
		
		
		
		//HTML for each element
		let eHTML = `
	
			<div class="product-item men">
			<div class="product discount product_filter">
				<div class="product_image">
					<img src="${e.imgUrl1}"  alt="">
				</div>
				<div class="product_bubble product_bubble_right product_bubble_red d-flex flex-column align-items-center"><span>-${discount*100}%</span></div>
				<div class="product_info">
					<h6 class="product_name"><a href="/product/view-by-product-line?productLineId=${e.id}">${e.name}</a></h6>
					
					<!-- MIN MAX PRICE  -->
					<div class="discount-product_price">${minPrice} - ${maxPrice}</div>
					<div class="product_price">${discountMinPrice} - ${discountMaxPrice}</div>
					
					
					<!-- SOLD -->
					<span>Đã bán: </span> 
					<span>${e.totalSold}</span>
					
					
				</div>
			</div>
			<div class="red_button add_to_cart_button"><a href="/visitor/product/view-by-product-line?productLineId=${e.id}">Thêm vào giỏ hàng</a></div>
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
		// Text labels
		first:'Đầu',
		prev:'Trước',
		next:'Tiếp',
		last:'Sau',
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

function updateProductList(){
	$('.lable-sort-type').click(function(){
		$(this).find('.sort-type').attr('checked', 'checked');
		console.log($('input[name="sort-type"]:checked').val());
	});
}