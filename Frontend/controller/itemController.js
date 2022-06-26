const itemCodeRegEx = /^(I-)[0-9]{6}$/;
const itemNameRegEx = /^[A-z ]{2,20}$/;
const itemPriceRegEx = /^[1-9][0-9]{0,5}[.][0-9]{2}$/;
const itemQtyRegEx = /^[1-9][0-9]*$/;

let btnItemSearch = $("#btnItemSearch");
let btnItemSave = $("#btnItemSave");
let btnItemUpdate = $("#btnItemUpdate");
let btnItemDelete = $("#btnItemDelete");

let txtItemSearch = $("#txtItemSearch");
let txtItemCode = $("#txtItemCode");
let txtItemName = $("#txtItemName");
let txtItemPrice = $("#txtItemPrice");
let txtItemQty = $("#txtItemQty");

let cmbOrderItemCode = $("#cmbOrderItemCode");
let tblItem = $("#item-table");



txtItemCode.keyup(function (event) {
    validateItemCode();
    if (event.key === 'Enter' && itemCodeRegEx.test(txtItemCode.val())){
        txtItemName.focus();
    }
});
txtItemName.keyup(function (event) {
    validateItemName();
    if (event.key === 'Enter' && itemNameRegEx.test(txtItemName.val())){
        txtItemPrice.focus();
    }
});
txtItemPrice.keyup(function (event) {
    validateItemPrice();
    if (event.key === 'Enter' && itemPriceRegEx.test(txtItemPrice.val())){
        txtItemQty.focus();
    }
});
txtItemQty.keyup(function (event) {
    validateItemQty();
});



$(document).ready(function() {
    loadAllItems();
    loadFromItemTable();
});

btnItemSearch.click(function () {
    clearAllItemFields();

    $.ajax({
        url:"http://localhost:8080/Backend/item?option=SEARCH&code="+txtItemSearch.val(),
        method:"GET",
        contentType:"application/json",
        success:function (jsonResp) {
            if(jsonResp.status===200){
                loadItemToFields(jsonResp.data);
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

    function loadItemToFields(data) {
        txtItemCode.val(data.code);
        txtItemName.val(data.name);
        txtItemPrice.val(data.unit_price);
        txtItemQty.val(data.quantity);

        validateItemCode();
        validateItemName();
        validateItemPrice();
        validateItemQty();
    }

    loadFromItemTable();

});

btnItemSave.click(function () {

    let jsonReq = {option : "", data : {code : txtItemCode.val(), name : txtItemName.val(), unit_price : txtItemPrice.val(), quantity : txtItemQty.val()}}

    $.ajax({
        url:"http://localhost:8080/Backend/item",
        method:"POST",
        contentType:"application/json",
        data:JSON.stringify(jsonReq),
        success:function (jsonResp) {
            if(jsonResp.status===200){
                alert(jsonResp.message);
                loadAllItems();
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

    loadFromItemTable();

});

btnItemUpdate.click(function () {

    let jsonReq = {option : "", data : {code : txtItemCode.val(), name : txtItemName.val(), unit_price : txtItemPrice.val(), quantity : txtItemQty.val()}}

    $.ajax({
        url:"http://localhost:8080/Backend/item",
        method:"PUT",
        contentType:"application/json",
        data:JSON.stringify(jsonReq),
        success:function (jsonResp) {
            if(jsonResp.status===200){
                alert(jsonResp.message);
                loadAllItems();
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

    loadFromItemTable();

});

btnItemDelete.click(function () {

    let jsonReq = {option : "",data : {code : txtItemCode.val()}}

    $.ajax({
        url:"http://localhost:8080/Backend/item",
        method:"DELETE",
        contentType:"application/json",
        data:JSON.stringify(jsonReq),
        success:function (jsonResp) {
            if(jsonResp.status===200){
                alert(jsonResp.message);
                loadAllItems();
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

    loadFromItemTable();

});

function loadFromItemTable() {

    $("#item-table>tr:not(.spacer)").click(function () {
        let code = $(this).children(":eq(1)").text();
        let name = $(this).children(":eq(2)").text();
        let unit_price = $(this).children(":eq(3)").text();
        let quantity = $(this).children(":eq(4)").text();

        console.log(code, name, unit_price, quantity);

        txtItemCode.val(code);
        txtItemName.val(name);
        txtItemPrice.val(unit_price);
        txtItemQty.val(quantity);

        validateItemCode();
        validateItemName();
        validateItemPrice();
        validateItemQty();
    });
}

function setItemCombo() {
    cmbOrderItemCode.empty();
    cmbOrderItemCode.append(new Option("Item Code", ""));

    $.ajax({
        url:"http://localhost:8080/Backend/place-order?option=GET-ALL-ITEM-CODES",
        method:"GET",
        contentType:"application/json",
        success:function (jsonResp) {
            if(jsonResp.status===200){
                for (let i=0; i<jsonResp.data; i++) {
                    let code=jsonResp.data.code;
                    cmbOrderItemCode.append(new Option(code, code));
                }
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

function clearAllItemFields() {
    txtItemCode.val('');
    txtItemName.val('');
    txtItemPrice.val('');
    txtItemQty.val('');

    txtItemCode.css('border','1px solid #ced4da');
    txtItemName.css('border','1px solid #ced4da');
    txtItemPrice.css('border','1px solid #ced4da');
    txtItemQty.css('border','1px solid #ced4da');
}

function loadAllItems() {

    $.ajax({
        url:"http://localhost:8080/Backend/item?option=GET-ALL",
        method:"GET",
        contentType:"application/json",
        success:function (jsonResp) {
            if(jsonResp.status===200){
                loadItemTable(jsonResp.data);
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

    function loadItemTable(data) {
        tblItem.empty();

        for (let i=0; i<data.length; i++){
            let code=data[i].code;
            let name=data[i].name;
            let unit_price=data[i].unit_price;
            let quantity=data[i].quantity;

            let row = `<tr scope="row"><td>${i+1}</td><td><a href="#">${code}</a></td><td>${name}</td><td>${unit_price}</td><td>${quantity}</td></tr><tr class="spacer"><td colspan="100"></td></tr>`;
            tblItem.append(row);
        }
    }
}

function validateItemCode(){
    if (itemCodeRegEx.test(txtItemCode.val())) {
        txtItemCode.css('border','3px solid green');
    }else{
        txtItemCode.css('border','3px solid red');
    }
}
function validateItemName(){
    if (itemNameRegEx.test(txtItemName.val())) {
        txtItemName.css('border','3px solid green');
    }else{
        txtItemName.css('border','3px solid red');
    }
}
function validateItemPrice(){
    if (itemPriceRegEx.test(txtItemPrice.val())) {
        txtItemPrice.css('border','3px solid green');
    }else{
        txtItemPrice.css('border','3px solid red');
    }
}
function validateItemQty(){
    if (itemQtyRegEx.test(txtItemQty.val())) {
        txtItemQty.css('border','3px solid green');
    }else{
        txtItemQty.css('border','3px solid red');
    }
}
