$("#dashboardContent").css("display","block");
$("#customerContent").css("display","none");
$("#itemContent").css("display","none");
$("#orderContent").css("display","none");


$("#linkCustomer").click(function (){
    $("#customerContent").css("display","block");
    $("#dashboardContent").css("display","none");
    $("#itemContent").css("display","none");
    $("#orderContent").css("display","none");

});

$("#linkItem").click(function (){
    $("#itemContent").css("display","block");
    $("#customerContent").css("display","none");
    $("#dashboardContent").css("display","none");
    $("#orderContent").css("display","none");

});


$("#linkHome").click(function (){
    $("#dashboardContent").css("display","block");
    $("#itemContent").css("display","none");
    $("#customerContent").css("display","none");
    $("#orderContent").css("display","none");
});

$("#linkOrder").click(function (){
    $("#orderContent").css("display","block");
    $("#dashboardContent").css("display","none");
    $("#itemContent").css("display","none");
    $("#customerContent").css("display","none");

});