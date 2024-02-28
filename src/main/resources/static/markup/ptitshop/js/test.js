let URL = 'https://online-gateway.ghn.vn/shiip/public-api/master-data/province';
const token = '606ae2a4-5da1-11ed-b824-262f869eb1a7';
let data = "";
$( document ).ready(function() {
    console.log('ok');
    
    $.ajax({
    url: URL,
    headers: {
        'token': token,
    },
    method: 'GET',
    dataType: 'json',
    data: data,
    success: function(data){
      console.log(data.data[0].ProvinceID);
    }
  });
    
});