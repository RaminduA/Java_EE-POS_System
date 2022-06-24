$(document).ready(function() {
    $('#js-preloader').fadeOut(2000);
    playDT();
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


function playDT(){
    let Clock_Date = $("#Clock_Date");
    let Clock_Time = $("#Clock_Time");

    /*let dt = new Date().toISOString();
    Clock_Date.text(dt.split('T')[0]);
    Clock_Time.text(dt.split('T')[1].split('.')[0]);*/

    let dt = new Date();
    Clock_Date.text(moment(dt).format("ddd, MMM Do YYYY"));
    Clock_Time.text(moment(dt).format("hh:mm:ss A"));

    setInterval(function(){playDT();},1000);
}