const itemCodeRegEx = /^(I00-)[0-9]{3,4}$/;
const itemNameRegEx = /^[A-z ]{5,20}$/;
const itemPriceRegEx = /^[1-9][0-9]{1,5}[.][0-9]{2}$/;
const itemQtyRegEx = /^[1-9][0-9]*$/;

$("#btnItemSaveOrUpdate").attr('disabled', true);
$("#btnItemDelete").attr('disabled', true);

$("#txtItemCode").keyup(function (event) {
    setItemButtons();
    validateItemCode();
    if (event.key === 'Enter' & itemCodeRegEx.test($("#txtItemCode").val())){
        $("#txtItemName").focus();
    }
});
$("#txtItemName").keyup(function (event) {
    setItemButtons();
    validateItemName();
    if (event.key === 'Enter' & itemNameRegEx.test($("#txtItemName").val())){
        $("#txtItemPrice").focus();
    }
});
$("#txtItemPrice").keyup(function (event) {
    setItemButtons();
    validateItemPrice();
    if (event.key === 'Enter' & itemPriceRegEx.test($("#txtItemPrice").val())){
        $("#txtItemQty").focus();
    }
});
$("#txtItemQty").keyup(function (event) {
    setItemButtons();
    validateItemQty();
});

$("#btnItemSaveOrUpdate").click(function () {
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
    setItemButtons();

    loadFromItemTable();
});

$("#btnItemSearch").click(function () {
    if(isItemExists($("#txtItemSearch").val())){
        var itemObject;
        for(var i in itemDB){
            if(itemDB[i].getCode()===$("#txtItemSearch").val()){
                itemObject = itemDB[i];
            }
        }

        $("#txtItemCode").val(itemObject.getCode());
        $("#txtItemName").val(itemObject.getName());
        $("#txtItemPrice").val(itemObject.getPrice());
        $("#txtItemQty").val(itemObject.getQuantity());

        validateItemCode();
        validateItemName();
        validateItemPrice();
        validateItemQty();
    }else{
        alert("Item Doesn't Exist...")
    }
});

$("#btnItemDelete").click(function () {
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
    setItemButtons();

    loadFromItemTable();
});

function loadFromItemTable() {
    $("#itemTable>tr").click(function () {
        let itmCode = $(this).children(":eq(0)").text();
        let itmName = $(this).children(":eq(1)").text();
        let itmPrice = $(this).children(":eq(2)").text();
        let itmQty = $(this).children(":eq(3)").text();

        console.log(itmCode, itmName, itmPrice, itmQty);

        $("#txtItemCode").val(itmCode);
        $("#txtItemName").val(itmName);
        $("#txtItemPrice").val(itmPrice);
        $("#txtItemQty").val(itmQty);

        validateItemCode();
        validateItemName();
        validateItemPrice();
        validateItemQty();

        setItemButtons();
    });
}

function setItemCombo() {
    $("#cmbOrderItemCode").empty();
    $('#cmbOrderItemCode').append(new Option("Item Code", ""));
    for (var i in itemDB){
        let code=itemDB[i].getCode();
        $('#cmbOrderItemCode').append(new Option(code, code));
    }
}

function clearAllItemFields() {
    $("#txtItemCode").val('');
    $("#txtItemName").val('');
    $("#txtItemPrice").val('');
    $("#txtItemQty").val('');

    $("#txtItemCode").css('border','1px solid #ced4da');
    $("#txtItemName").css('border','1px solid #ced4da');
    $("#txtItemPrice").css('border','1px solid #ced4da');
    $("#txtItemQty").css('border','1px solid #ced4da');
}

function loadAllItems() {
    $("#itemTable").empty();

    for (var i in itemDB){
        let code=itemDB[i].getCode();
        let name=itemDB[i].getName();
        let price=itemDB[i].getPrice();
        let quantity=itemDB[i].getQuantity();

        let row = `<tr><td>${code}</td><td>${name}</td><td>${price}</td><td>${quantity}</td></tr>`;
        $("#itemTable").append(row);
    }
}

function setItemButtons() {
    let a = isItemExists($("#txtItemCode").val());
    let b = itemCodeRegEx.test($("#txtItemCode").val()) & itemNameRegEx.test($("#txtItemName").val()) & itemPriceRegEx.test($("#txtItemPrice").val()) & itemQtyRegEx.test($("#txtItemQty").val());

    if (a) {
        $("#btnItemDelete").attr('disabled', false);
        $("#btnItemSaveOrUpdate").html('Update');
    } else {
        $("#btnItemDelete").attr('disabled', true);
        $("#btnItemSaveOrUpdate").html('Save');
    }

    if (b) {
        $("#btnItemSaveOrUpdate").attr('disabled', false);
    } else {
        $("#btnItemSaveOrUpdate").attr('disabled', true);
    }
}

function isItemExists(code){
    for(var i in itemDB){
        if(itemDB[i].getCode()===code){
            return true;
        }
    }
    return false;
}

function validateItemCode(){
    if (itemCodeRegEx.test($("#txtItemCode").val())) {
        $("#txtItemCode").css('border','3px solid green');
    }else{
        $("#txtItemCode").css('border','3px solid red');
    }
}
function validateItemName(){
    if (itemNameRegEx.test($("#txtItemName").val())) {
        $("#txtItemName").css('border','3px solid green');
    }else{
        $("#txtItemName").css('border','3px solid red');
    }
}
function validateItemPrice(){
    if (itemPriceRegEx.test($("#txtItemPrice").val())) {
        $("#txtItemPrice").css('border','3px solid green');
    }else{
        $("#txtItemPrice").css('border','3px solid red');
    }
}
function validateItemQty(){
    if (itemQtyRegEx.test($("#txtItemQty").val())) {
        $("#txtItemQty").css('border','3px solid green');
    }else{
        $("#txtItemQty").css('border','3px solid red');
    }
}
