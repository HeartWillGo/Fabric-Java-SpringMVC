// $(document).ready(function () {
//
//     $("#search-form").submit(function (event) {
//
//         //stop submit the form, we will post it manually.
//         event.preventDefault();
//
//         fire_ajax_submit();
//
//     });
// });
window.onload = function() {


    fire_ajax_submit();
};


function fire_ajax_submit() {

   var search = {"organizationid":"pingan"};//

//  search["organizationid"] = $("#organizationid").val();
                                                            //search["organizationid"] = "pingan";
  // search["email"] = $("#email").val();

  //  $("#btn-search").prop("disabled", true);


    $.ajax({
        type: "POST",
        contentType: "application/json",
        // url: "/api/search",
        url: "/admin/yz",
        data: JSON.stringify(search),
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            var json = "<h4>Ajax Response</h4><pre>"
                + JSON.stringify(data, null, 4) + "</pre>";
            $('#feedbackTest').html(json);

            var TestArrarys=new Array(100);   //存放时间类型转换后的数据

            for(var i=0;i<data.transactionsResult.length;i++){　//数组的遍历
                console.log("SUCCESS : ", data.transactionsResult[i].transactiondate);
             //   alert(data.transactionsResult[i].amount);
            }


            console.log("SUCCESS : ", data);
           // console.log("SUCCESS : ", data.transactionsResult[0].amount);


            $("#btn-search").prop("disabled", false);

        },
        error: function (e) {

            var json = "<h4>Ajax Response</h4><pre>"
                + e.responseText + "</pre>";
            $('#feedbackTest').html(json);

            console.log("ERROR : ", e);
            $("#btn-search").prop("disabled", false);

        }
    })
}
