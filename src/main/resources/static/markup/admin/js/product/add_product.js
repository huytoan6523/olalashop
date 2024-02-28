
const recordTableBody = $("#record-table > tbody")
var rowCount = $('#record-table tr').length - 2;
var numberAttribute = parseInt($('#number_attribute').val());
$(document).ready(function() {
	handlePlusRecord();
	validation();

});

function handlePlusRecord() {
	$('.add-link').click(function() {
		rowCount = rowCount + 1;
		//console.log(rowCount);
		//get Current Row Order
		//var currentRow = $(this).closest("tr");
		//var order = parseInt(currentRow.find('.record-order').text()) + 1;
		//console.log(order);
		//

		var html =
			`
			<tr>				          		
				<td class="record-order">${rowCount}</td>
				<td><input class="price" id="products${rowCount}.price" name="products[${rowCount}].price" value="" /></td>
				<td><input class ="stock" id="products${rowCount}.stock" name="products[${rowCount}].stock" value=""/></td>`;

		for (var i = 0; i < numberAttribute; i++) {
			html += `
					<td><input class="attribute" id="products${rowCount}.productAttributes${i}.value" name="products[${rowCount}].productAttributes[${i}].value" value="" required /></td>`;
		}
		html += `</tr>`;

		recordTableBody.append(html);
	});
};

function validation() {
	$('#add-product-form').submit(function(event) {
		if ( isValid() ){
			$('#add-product-form').submit();
		} else {
			event.preventDefault();
		}
		 
	});
}

function isValid(){
	var count = 0;
	var price = $('.price').val()+'';
	if (!price.match(/^\d+$/)) {
		count++;
	}

	var stock = $('.stock').val()+'';
	if (!stock.match(/^\d+$/)) {
		count++;
	}


	if ($('.stock').length==0)     
	{
		count++;
	}
	
	if ( count == 0 ) 
		return true;	
	else {
		alert('Vui lòng nhập đúng thông tin!');
		return false;
	}
}

