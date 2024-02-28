
const recordTableBody = $("#record-table > tbody")
var rowCount = $('#record-table tr').length - 2;
$(document).ready(function() {
	handlePlusRecord();
	handleMinusRecord();
});

function handlePlusRecord() {
	$('.plus-record-btn').click(function() {
		rowCount = rowCount + 1;
		//console.log(rowCount);
		//get Current Row Order
		//var currentRow = $(this).closest("tr");
		//var order = parseInt(currentRow.find('.record-order').text()) + 1;
		//console.log(order);
		//
		recordTableBody.append(`
			<tr>
									          		
				<td class="record-order">${rowCount}</td>
				<td><input id="categories${rowCount}.name" name="categories[${rowCount}].name" value="" /></td>
				<td><input id="categories${rowCount}.description" name="categories[${rowCount}].description" value=""/></td>
				<td colspan="2"></td>
			 </tr>			
		`);
	});
};

function handleMinusRecord() {
	$('.minus-record-btn').click(function() {
		//get rowCount // ingnore tr > th
		var rowCount = $('#record-table tr').length - 1;
		if( rowCount > 1  ){
			 rowCount = rowCount - 1;
			 //remove lasted row
			$('#record-table tr:last').remove();
		}
	});
	


};



//TODO handle plus + minus + validate record