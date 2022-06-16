const orderCusIDRegEx = /^(C00-)[0-9]{3,4}$/;
const orderItemCodeRegEx = /^(I00-)[0-9]{3,4}$/;
const orderIDRegEx = /^(O00-)[0-9]{4,5}$/;
const quantityRegEx = /^[1-9][0-9]*$/;


$("#txtOrderId").val("O00-0001");




$("#btnAddToCart").attr('disabled', true);
$("#btnPurchaseOrder").attr('disabled', true);

$("#txtOrderId").prop('disabled', true);
$("#txtDate").prop('disabled', true);
$("#txtTime").prop('disabled', true);
$("#txtOrderCustName").prop('disabled', true);
$("#txtOrderCustAddress").prop('disabled', true);
$("#txtOrderCustContact").prop('disabled', true);
$("#txtOrderItemName").prop('disabled', true);
$("#txtOrderItemPrice").prop('disabled', true);
$("#txtOrderItemQty").prop('disabled', true);
$("#txtSubTotal").prop('disabled', true);


function displayDateTime() {
    var date = new Date()
    var ampm = date.getHours( ) >= 12 ? 'PM' : 'AM';
    var hours = date.getHours( ) % 12;
    hours = hours ? hours : 12;
    hours=hours.toString().length===1? 0+hours.toString() : hours;

    var minutes=date.getMinutes().toString()
    minutes=minutes.length===1 ? 0+minutes : minutes;

    var seconds=date.getSeconds().toString()
    seconds=seconds.length===1 ? 0+seconds : seconds;

    var year=date.getFullYear().toString();

    var month=(date.getMonth() +1).toString();
    month=month.length===1 ? 0+month : month;

    var day=date.getDate().toString();
    day=day.length===1 ? 0+day : day;

    var x1=day + "/" + month + "/" + year;
    var x2 = hours + ":" +  minutes + ":" +  seconds + " " + ampm;
    $("#txtDate").val(x1);
    $("#txtTime").val(x2);
    playDateTime();
}
function playDateTime(){
    var refresh=1000; // Refresh rate in milli seconds
    mytime=setTimeout('displayDateTime()',refresh)
}
playDateTime();

$('#cmbOrderCustId').on('change', function() {
    var id = $(this).val();

    if(id===""){
        $("#txtOrderCustName").val("");
        $("#txtOrderCustAddress").val("");
        $("#txtOrderCustContact").val("");
    }else{
        var customerObject;
        for(var i in customerDB){
            if(customerDB[i].getId()===id){
                customerObject = customerDB[i];
            }
        }
        $("#txtOrderCustName").val(customerObject.getName());
        $("#txtOrderCustAddress").val(customerObject.getAddress());
        $("#txtOrderCustContact").val(customerObject.getContact());
    }
});

$('#cmbOrderItemCode').on('change', function() {
    var code = $(this).val();

    if(code===""){
        $("#txtOrderItemName").val("");
        $("#txtOrderItemPrice").val("");
        $("#txtOrderItemQty").val("");
    }else{
        var itemObject;
        for(var i in itemDB){
            if(itemDB[i].getCode()===code){
                itemObject = itemDB[i];
            }
        }
        $("#txtOrderItemName").val(itemObject.getName());
        $("#txtOrderItemPrice").val(itemObject.getPrice());
        setQtyOnHand();
        //$("#txtOrderItemQty").val(itemObject.getQuantity());
    }
});

function setOrderButtons() {
    let a = orderItemCodeRegEx.test($("#cmbOrderItemCode").val()) & orderCusIDRegEx.test($("#cmbOrderCustId").val()) & quantityRegEx.test($("#txtQuantity").val()) & parseInt($("#txtQuantity").val())<=parseInt($("#txtOrderItemQty").val());
    let b = orderIDRegEx.test($("#txtOrderId").val()) & cartDB.length>0;
    if (a) {
        $("#btnAddToCart").attr('disabled', false);
    } else {
        $("#btnAddToCart").attr('disabled', true);
    }
    if (b) {
        $("#btnPurchaseOrder").attr('disabled', false);
    } else {
        $("#btnPurchaseOrder").attr('disabled', true);
    }
}

$("#txtQuantity").keyup(function (event) {
    setOrderButtons();
    if($("#txtQuantity").val()===""){
        $("#txtQuantity").css('border','1px solid #ced4da');
        $("#txtSubTotal").val("");
    }else if (parseInt($("#txtQuantity").val())<=parseInt($("#txtOrderItemQty").val()) & quantityRegEx.test($("#txtQuantity").val())){
        $("#txtQuantity").css('border','3px solid green');
        var st=parseInt($("#txtQuantity").val()) * parseFloat($("#txtOrderItemPrice").val());
        $("#txtSubTotal").val(st.toFixed(2));
    }else{
        $("#txtQuantity").css('border','3px solid red');
        $("#txtSubTotal").val("");
    }
});

$('#btnAddToCart').click(function () {
    let itmCode = $("#cmbOrderItemCode").val();
    let itmName = $("#txtOrderItemName").val();
    let itmPrice = $("#txtOrderItemPrice").val();
    let itmQty = $("#txtQuantity").val();
    let itmTotal = $("#txtSubTotal").val();

    var cartObject=new OrderTM(itmCode,itmName,itmPrice,itmQty,itmTotal);
    if(isOrderItemExists(cartObject.getItemCode())){
        for(var i in cartDB){
            if(cartDB[i].getItemCode()===itmCode){
                let newQty=parseInt(cartDB[i].getQuantity())+parseInt(cartObject.getQuantity());
                cartDB[i].setQuantity(newQty);
                let newTotal=parseFloat(cartDB[i].getTotal())+parseFloat(cartObject.getTotal());
                cartDB[i].setTotal(newTotal.toFixed(2));
            }
        }
    }else{
        cartDB.push(cartObject);
    }

    $("#txtQuantity").val("");
    $("#txtSubTotal").val("");
    $("#txtQuantity").css('border','1px solid #ced4da');

    setTotalPurchase();
    setQtyOnHand();
    //clearAllCustomerFields();
    loadAllCartObjects();
    setOrderButtons();
    //setCustomerButtons();
});

$('#btnPurchaseOrder').click(function () {
    let orderID = $("#txtOrderId").val();
    let cusID = $("#cmbOrderCustId").val();
    let orderDate = $("#txtDate").val();
    let orderTime = $("#txtTime").val();
    let orderCost = $("#txtTotal").val();

    let detailList=new Array();

    for (var i in cartDB){
        let itmID=cartDB[i].getItemCode();
        let itmQty=cartDB[i].getQuantity();
        let itmPrice=cartDB[i].getPrice();
        let itmTotal=cartDB[i].getTotal();

        var orderDetail=new OrderDetailDTO(itmID,orderID,itmQty,itmPrice,itmTotal);
        detailList.push(orderDetail);
    }

    var orderObject=new OrderDTO(orderID,cusID,orderDate,orderTime,orderCost,detailList);
    orderDB.push(orderObject);

    reducePurchasedItems();
    cartDB.splice(0, cartDB.length);
    setOrderId();
    setTotalPurchase();
    clearAllOrderFields();
    loadAllCartObjects();
    loadAllItems();
    setOrderButtons();
});

function reducePurchasedItems() {
    for (var i in cartDB){
        var orderDetail=cartDB[i];
        for (var j in itemDB){
            if(itemDB[j].getCode()===orderDetail.getItemCode()){
                itemDB[j].setQuantity(itemDB[j].getQuantity()-orderDetail.getQuantity())
            }
        }
    }
}

function setOrderId() {
    var oldOrderId=$("#txtOrderId").val();
    var index=parseInt(oldOrderId.split("-")[1]);
    var newOrderId;
    if(index<9){
        index++;
        newOrderId="O00-000"+index;
    }else if(index<99){
        index++;
        newOrderId="O00-00"+index;
    }else if(index<999){
        index++;
        newOrderId="O00-0"+index;
    }else if(index<9999){
        index++;
        newOrderId="O00-"+index;
    }
    $("#txtOrderId").val(newOrderId);
}

function clearAllOrderFields() {
    $("#txtOrderCustName").val("");
    $("#txtOrderCustAddress").val("");
    $("#txtOrderCustContact").val("");
    $("#txtOrderItemName").val("");
    $("#txtOrderItemPrice").val("");
    $("#txtOrderItemQty").val("");
    $("#txtQuantity").val("");
    $("#txtSubTotal").val("");
    $("#txtQuantity").css('border','1px solid #ced4da');
}

function loadAllCartObjects() {
    $("#orderTable").empty();

    for (var i in cartDB){
        let itmCode=cartDB[i].getItemCode();
        let itmName=cartDB[i].getItemName();
        let itmPrice=cartDB[i].getPrice();
        let itmQty=cartDB[i].getQuantity();
        let itmTotal=cartDB[i].getTotal();

        let row = `<tr><td>${itmCode}</td><td>${itmName}</td><td>${itmPrice}</td><td>${itmQty}</td><td>${itmTotal}</td></tr>`;
        $("#orderTable").append(row);
    }
}

function setTotalPurchase() {
    let total=0;
    for(var i in cartDB){
        total+=parseFloat(cartDB[i].getTotal());
    }
    $("#txtTotal").text(total.toFixed(2));
}

function setQtyOnHand() {
    var itemObject;
    for(var i in itemDB){
        if(itemDB[i].getCode()===$("#cmbOrderItemCode").val()){
            itemObject = itemDB[i];
        }
    }
    let qty=itemObject.getQuantity();
    for(var i in cartDB){
        if(cartDB[i].getItemCode()===$("#cmbOrderItemCode").val()){
            qty-= cartDB[i].getQuantity();
        }
    }
    $("#txtOrderItemQty").val(qty);
}

function isOrderItemExists(itmCode) {
    for(var i in cartDB){
        if(cartDB[i].getItemCode()===itmCode){
            return true;
        }
    }
    return false;
}