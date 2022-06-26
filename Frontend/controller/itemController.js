const itemCodeRegEx = /^(I-)[0-9]{5}$/;
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

});

btnItemSave.click(function () {
    let itemCode = $("#txtItemCode").val();
    let itemName = $("#txtItemName").val();
    let itemPrice = $("#txtItemPrice").val();
    let itemQuantity = $("#txtItemQty").val();

    var itemObject=new ItemDTO(itemCode,itemName,itemPrice,itemQuantity);

    if(isItemExists(itemCode)){
        for(var i in itemDB){
            if(itemDB[i].getCode()===itemCode){
                itemDB[i]=itemObject;
            }
        }
    }else{
        itemDB.push(itemObject);
    }

    clearAllItemFields();
    setItemCombo();
    loadAllItems();

    loadFromItemTable();
});

btnItemUpdate.click(function () {
    let itemCode = $("#txtItemCode").val();
    let itemName = $("#txtItemName").val();
    let itemPrice = $("#txtItemPrice").val();
    let itemQuantity = $("#txtItemQty").val();

    var itemObject=new ItemDTO(itemCode,itemName,itemPrice,itemQuantity);

    if(isItemExists(itemCode)){
        for(var i in itemDB){
            if(itemDB[i].getCode()===itemCode){
                itemDB[i]=itemObject;
            }
        }
    }else{
        itemDB.push(itemObject);
    }

    clearAllItemFields();
    setItemCombo();
    loadAllItems();

    loadFromItemTable();
});

btnItemDelete.click(function () {
    var index=-1;
    for(var i in itemDB){
        if(itemDB[i].getCode()===$("#txtItemCode").val()){
            index=i;
        }
    }
    if (index !== -1) {
        itemDB.splice(index, 1);
    }
    clearAllItemFields();
    setItemCombo();
    loadAllItems();

    loadFromItemTable();
});

function loadFromItemTable() {
    $("#itemTable>tr").click(function () {
        let code = $(this).children(":eq(1)").text();
        let name = $(this).children(":eq(2)").text();
        let price = $(this).children(":eq(3)").text();
        let qty = $(this).children(":eq(4)").text();

        console.log(code, name, price, qty);

        $("#txtItemCode").val(code);
        $("#txtItemName").val(name);
        $("#txtItemPrice").val(price);
        $("#txtItemQty").val(qty);

        validateItemCode();
        validateItemName();
        validateItemPrice();
        validateItemQty();

    });
}

function setItemCombo() {
    cmbOrderItemCode.empty();
    cmbOrderItemCode.append(new Option("Item Code", ""));
    for (var i in itemDB){
        let code=itemDB[i].getCode();
        cmbOrderItemCode.append(new Option(code, code));
    }
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
    tblItem.empty();

    for (var i in itemDB){
        let code=itemDB[i].getCode();
        let name=itemDB[i].getName();
        let price=itemDB[i].getPrice();
        let quantity=itemDB[i].getQuantity();

        let row = `<tr><td>${code}</td><td>${name}</td><td>${price}</td><td>${quantity}</td></tr>`;
        $("#itemTable").append(row);
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
