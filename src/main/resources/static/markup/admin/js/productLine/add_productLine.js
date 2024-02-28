
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
				<td><input id="attributes${rowCount}.name" name="attributes[${rowCount}].name" value="" /></td>
				<td><input id="attributes${rowCount}.description" name="attributes[${rowCount}].description" value=""/></td>

				<td>
               <button  class="right__iconTable bg-transparent plus-record-btn" type="button">
                <img src="/markup/admin/assets/plus.svg" class="bg-green" alt="" style="margin-left: 0">
				</button>
				</td>
								                    
				<td>
               <button class="right__iconTable bg-transparent minus-record-btn" type="button">
                <img src="/markup/admin/assets/minus.svg" class="bg-green" alt="" style="margin-left: 0">
				</button>
				</td>
			 </tr>			
		`);
	
	
		//remove event listner
		$(this).off('click');
		handlePlusRecord();
		handleMinusRecord();

	});
	


};

function handleMinusRecord() {
	$('.minus-record-btn').click(function() {
		//get rowCount // ingnore tr > th
		var rowCount = $('#record-table tr').length - 1;
		if( rowCount > 1  ){
			 rowCount = rowCount - 1;
			 $(this).closest("tr").remove();
		}
	});
	


};


//TODO handle plus + minus + validate record