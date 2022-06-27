const orderCusIDRegEx = /^(C-)[0-9]{5}$/;
const orderItemCodeRegEx = /^(I-)[0-9]{6}$/;
const orderIDRegEx = /^(O-)[0-9]{6}$/;
const quantityRegEx = /^[1-9][0-9]*$/;

let btnAddItemToCart = $("#btnAddItemToCart");
let btnPurchaseOrder = $("#btnPurchaseOrder");

let txtOrderId = $("#order-id-text");
let txtTotal = $("#total-text");
let txtOrderCusName = $("#txtOrderCustName");
let txtOrderCusAddress = $("#txtOrderCustAddress");
let txtOrderCusContact = $("#txtOrderCustContact");
let txtOrderItemName = $("#txtOrderItemName");
let txtOrderItemPrice = $("#txtOrderItemPrice");
let txtOrderItemQty = $("#txtOrderItemQty");
let txtQuantity = $("#txtQuantity");
let txtSubTotal = $("#txtSubTotal");

let tblOrder = $("#order-table");

let cart = [];




txtOrderCusName.prop('disabled', true);
txtOrderCusAddress.prop('disabled', true);
txtOrderCusContact.prop('disabled', true);
txtOrderItemName.prop('disabled', true);
txtOrderItemPrice.prop('disabled', true);
txtOrderItemQty.prop('disabled', true);
txtSubTotal.prop('disabled', true);


$(document).ready(function() {
    playDT();
    setOrderId();
});

cmbOrderCusId.on('change', function() {
    if($(this).val()===""){
        txtOrderCusName.val("");
        txtOrderCusAddress.val("");
        txtOrderCusContact.val("");
    }else{
        $.ajax({
            url:"http://localhost:8080/Backend/place-order?option=GET-CUSTOMER&id="+$(this).val(),
            method:"GET",
            contentType:"application/json",
            success:function (jsonResp) {
                if(jsonResp.status===200){
                    txtOrderCusName.val(jsonResp.data.name);
                    txtOrderCusAddress.val(jsonResp.data.address);
                    txtOrderCusContact.val(jsonResp.data.contact);
                }else if(jsonResp.status===404){
                    alert(jsonResp.message);
                }else{
                    alert(jsonResp.data);
                }
            },
            error:function (ob, textStatus, error) {
                console.log(ob);
                console.log(textStatus);
                console.log(error);
            }
        });
    }
});

cmbOrderItemCode.on('change', function() {
    if($(this).val()===""){
        txtOrderItemName.val("");
        txtOrderItemPrice.val("");
        txtOrderItemQty.val("");
    }else{
        $.ajax({
            url:"http://localhost:8080/Backend/place-order?option=GET-ITEM&code="+$(this).val(),
            method:"GET",
            contentType:"application/json",
            success:function (jsonResp) {
                if(jsonResp.status===200){
                    txtOrderItemName.val(jsonResp.data.name);
                    txtOrderItemPrice.val(jsonResp.data.unit_price);
                    txtOrderItemQty.val(jsonResp.data.quantity);
                }else if(jsonResp.status===404){
                    alert(jsonResp.message);
                }else{
                    alert(jsonResp.data);
                }
            },
            error:function (ob, textStatus, error) {
                console.log(ob);
                console.log(textStatus);
                console.log(error);
            }
        });
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

txtQuantity.keyup(function (event) {
    //setOrderButtons();
    if(txtQuantity.val()===""){
        txtQuantity.css('border','1px solid #ced4da');
        txtSubTotal.val("");
    }else if (parseInt(txtQuantity.val()) <= parseInt(txtOrderItemQty.val()) && quantityRegEx.test(txtQuantity.val())){
        txtQuantity.css('border','3px solid green');
        let subtotal = parseInt(txtQuantity.val()) * parseFloat(txtOrderItemPrice.val());
        txtSubTotal.val(subtotal.toFixed(2));
    }else{
        txtQuantity.css('border','3px solid red');
        txtSubTotal.val("");
    }
});

btnAddItemToCart.click(function () {
    let code = cmbOrderItemCode.val();
    let description = txtOrderItemName.val();
    let unit_price = txtOrderItemPrice.val();
    let quantity = txtQuantity.val();
    let subtotal = txtSubTotal.val();

    let cart_row = {code : code, description : description, unit_price : unit_price, quantity : quantity, subtotal : subtotal};

    let isExists = isOrderItemExists(cart_row.code);
    if(isExists.boolean){
        let index = isExists.index;
        cart[index].quantity = parseInt(cart[index].quantity) + parseInt(cart_row.quantity);
        let new_total = parseFloat(cart[index].subtotal) + parseFloat(cart_row.subtotal);
        cart[index].subtotal = new_total.toFixed(2);
    }else{
        cart.push(cart_row);
    }

    txtQuantity.val("");
    txtQuantity.css('border','1px solid #ced4da');
    txtSubTotal.val("");

    //setTotalPurchase();
    //setQtyOnHand();
    //clearAllCustomerFields();
    loadAllCartObjects();
    //setOrderButtons();
    //setCustomerButtons();
});

btnPurchaseOrder.click(function () {
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
    for (let i=0; i<cart.length; i++){
        var orderDetail=cartDB[i];
        for (var j in itemDB){
            if(itemDB[j].getCode()===orderDetail.getItemCode()){
                itemDB[j].setQuantity(itemDB[j].getQuantity()-orderDetail.getQuantity())
            }
        }
    }
}


function clearAllOrderFields() {
    txtOrderCusName.val("");
    txtOrderCusAddress.val("");
    txtOrderCusContact.val("");
    txtOrderItemName.val("");
    txtOrderItemPrice.val("");
    txtOrderItemQty.val("");
    txtQuantity.val("");
    txtQuantity.css('border','1px solid #ced4da');
    txtSubTotal.val("");
}

function loadAllCartObjects() {
    tblOrder.empty();

    for (let i=0; i<cart.length; i++){
        let code=cart[i].code;
        let description = cart[i].description;
        let unit_price = cart[i].unit_price;
        let quantity = cart[i].quantity;
        let subtotal = cart[i].subtotal;

        let row = `<tr scope="row"><td>${i+1}</td><td><a href="#">${code}</a></td><td>${description}</td><td>${unit_price}</td><td>${quantity}</td><td>${subtotal}</td></tr><tr class="spacer"><td colspan="100"></td></tr>`;
        tblOrder.append(row);
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
        if(itemDB[i].getCode()===cmbOrderItemCode.val()){
            itemObject = itemDB[i];
        }
    }
    let qty=itemObject.getQuantity();
    for(var i in cartDB){
        if(cartDB[i].getItemCode()===cmbOrderItemCode.val()){
            qty-= cartDB[i].getQuantity();
        }
    }
    txtOrderItemQty.val(qty);
}

function isOrderItemExists(code) {
    for(let i=0; i<cart.length; i++){
        if(cart[i].code===code){
            return {boolean : true, index : i};
        }
    }
    return {boolean : false, index : -1};
}

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

function setOrderId() {

    $.ajax({
        url:"http://localhost:8080/Backend/place-order?option=GET-ORDER-ID",
        method:"GET",
        contentType:"application/json",
        success:function (jsonResp) {
            if(jsonResp.status===200){
                txtOrderId.text(jsonResp.data.id);
            }else if(jsonResp.status===404){
                alert(jsonResp.message);
            }else{
                alert(jsonResp.data);
            }
        },
        error:function (ob, textStatus, error) {
            console.log(ob);
            console.log(textStatus);
            console.log(error);
        }
    });

}