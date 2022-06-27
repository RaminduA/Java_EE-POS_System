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
        txtQuantity.val("");
        txtQuantity.css('border','1px solid #ced4da');
        txtSubTotal.val("");
    }else{
        $.ajax({
            url:"http://localhost:8080/Backend/place-order?option=GET-ITEM&code="+$(this).val(),
            method:"GET",
            contentType:"application/json",
            success:function (jsonResp) {
                if(jsonResp.status===200){
                    txtOrderItemName.val(jsonResp.data.name);
                    txtOrderItemPrice.val(jsonResp.data.unit_price);
                    setQuantityOnHand();
                    txtQuantity.val("");
                    txtQuantity.css('border','1px solid #ced4da');
                    txtSubTotal.val("");
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

txtQuantity.keyup(function (event) {
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
    if(!orderItemCodeRegEx.test(cmbOrderItemCode.val())){
        alert("Invalid Item Code !!!");

    }else if(!orderCusIDRegEx.test(cmbOrderCusId.val())){
        alert("Invalid Customer ID !!!");

    }else if(!quantityRegEx.test(txtQuantity.val()) || parseInt(txtQuantity.val()) > parseInt(txtOrderItemQty.val())){
        alert("Invalid Item Quantity !!!");

    }else{

        let code = cmbOrderItemCode.val();
        let description = txtOrderItemName.val();
        let unit_price = txtOrderItemPrice.val();
        let quantity = txtQuantity.val();
        let subtotal = txtSubTotal.val();

        let cart_row = {code : code, description : description, unit_price : unit_price, quantity : quantity, subtotal : subtotal};

        let isExists = isOrderItemExists(cart_row.code);
        if(isExists.boolean){
            let index = isExists.index;
            cart[index].quantity = (parseInt(cart[index].quantity) + parseInt(cart_row.quantity)).toString();
            let new_total = parseFloat(cart[index].subtotal) + parseFloat(cart_row.subtotal);
            cart[index].subtotal = new_total.toFixed(2);
        }else{
            cart.push(cart_row);
        }

        txtQuantity.val("");
        txtQuantity.css('border','1px solid #ced4da');
        txtSubTotal.val("");

        setTotalPurchase();
        setQuantityOnHand();
        loadAllCartObjects();

    }
});

btnPurchaseOrder.click(function () {
    if(!orderIDRegEx.test(txtOrderId.text())){
        alert("Invalid Order ID !!!");

    }else if(!orderCusIDRegEx.test(cmbOrderCusId.val())){
        alert("Invalid Customer ID !!!");

    }else if(cart.length <= 0){
        alert("Cart is Empty !!!");

    }else{

        let order_id = txtOrderId.text();
        let customer_id = cmbOrderCusId.val();
        let dt = new Date();
        let date = moment(dt).format("yyyy-MM-DD");
        let time = moment(dt).format("hh:mm:ss A");
        let cost = txtTotal.text();

        let detail_list = [];

        for (let i=0; i<cart.length; i++){
            let item_code = cart[i].code;
            let unit_price = cart[i].unit_price;
            let quantity = cart[i].quantity;
            let price = cart[i].subtotal;

            let order_detail = {order_id : order_id, item_code : item_code, unit_price : unit_price, quantity : quantity, price : price};
            detail_list.push(order_detail);
        }

        let jsonReq = {option : "", data : {order_id : order_id, customer_id : customer_id, date : date, time : time, cost : cost, detail_list : detail_list}};

        $.ajax({
            url:"http://localhost:8080/Backend/place-order",
            method:"POST",
            contentType:"application/json",
            data:JSON.stringify(jsonReq),
            success:function (jsonResp) {
                if(jsonResp.status===200){
                    alert(jsonResp.message);
                    cart = [];
                    setOrderId();
                    setTotalPurchase();
                    clearAllOrderFields();
                    loadAllCartObjects();
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
    for(let i=0; i<cart.length; i++){
        total += parseFloat(cart[i].subtotal);
    }
    txtTotal.text(total.toFixed(2));
}

function setQuantityOnHand() {

    $.ajax({
        url:"http://localhost:8080/Backend/place-order?option=GET-ITEM&code="+cmbOrderItemCode.val(),
        method:"GET",
        contentType:"application/json",
        success:function (jsonResp) {
            if(jsonResp.status===200){
                let new_quantity = parseInt(jsonResp.data.quantity);
                let isExists = isOrderItemExists(jsonResp.data.code);
                if (isExists.boolean) {
                    new_quantity -= parseInt(cart[isExists.index].quantity);
                }
                txtOrderItemQty.val(new_quantity);
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