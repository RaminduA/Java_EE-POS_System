$(document).ready(function() {
    $('#js-preloader').fadeOut(2000);
});


$("#dashboardContent").css("display","block");
$("#customerContent").css("display","none");
$("#itemContent").css("display","none");
$("#orderContent").css("display","none");


$("#linkHome").click(function (){
    $("#dashboardContent").css("display","block");
    $("#itemContent").css("display","none");
    $("#customerContent").css("display","none");
    $("#orderContent").css("display","none");

    $("#linkHome").removeClass("active");
    $("#linkCustomer").removeClass("active");
    $("#linkItem").removeClass("active");
    $("#linkOrder").removeClass("active");
    $("#linkHome").addClass("active");
});

$("#linkLogo").click(function (){
    $("#dashboardContent").css("display","block");
    $("#itemContent").css("display","none");
    $("#customerContent").css("display","none");
    $("#orderContent").css("display","none");

    $("#linkHome").removeClass("active");
    $("#linkCustomer").removeClass("active");
    $("#linkItem").removeClass("active");
    $("#linkOrder").removeClass("active");
    $("#linkHome").addClass("active");
});

$("#linkCustomer").click(function (){
    $("#customerContent").css("display","block");
    $("#dashboardContent").css("display","none");
    $("#itemContent").css("display","none");
    $("#orderContent").css("display","none");

    $("#linkHome").removeClass("active");
    $("#linkCustomer").removeClass("active");
    $("#linkItem").removeClass("active");
    $("#linkOrder").removeClass("active");
    $("#linkCustomer").addClass("active");

});

$("#linkItem").click(function (){
    $("#itemContent").css("display","block");
    $("#customerContent").css("display","none");
    $("#dashboardContent").css("display","none");
    $("#orderContent").css("display","none");

    $("#linkHome").removeClass("active");
    $("#linkCustomer").removeClass("active");
    $("#linkItem").removeClass("active");
    $("#linkOrder").removeClass("active");
    $("#linkItem").addClass("active");

});

$("#linkOrder").click(function (){
    $("#orderContent").css("display","block");
    $("#dashboardContent").css("display","none");
    $("#itemContent").css("display","none");
    $("#customerContent").css("display","none");

    $("#linkHome").removeClass("active");
    $("#linkCustomer").removeClass("active");
    $("#linkItem").removeClass("active");
    $("#linkOrder").removeClass("active");
    $("#linkOrder").addClass("active");

});