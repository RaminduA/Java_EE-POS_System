const cusIDRegEx = /^(C-)[0-9]{4}[1-9]{1}$/;
const cusNameRegEx = /^[A-z ]{2,20}$/;
const cusAddressRegEx = /^[0-9/A-z. ,]{5,}$/;
const cusContactRegEx = /^(0)[0-9]{2}(-)[0-9]{7}$/;

let btnCusSearch = $("#btnCustSearch");
let btnCusSave = $("#btnCustSave");
let btnCusUpdate = $("#btnCustUpdate");
let btnCusDelete = $("#btnCustDelete");

let txtCusSearch = $("#txtCusSearch");
let txtCusID = $("#txtCusID");
let txtCusName = $("#txtCusName");
let txtCusAddress = $("#txtCusAddress");
let txtCusContact = $("#txtCusContact");

let cmbOrderCusId = $("#cmbOrderCustId");
let tblCustomer = $("#customer-table");



txtCusID.keyup(function (event) {
    validateCustId();
    if (event.key === 'Enter' && cusIDRegEx.test(txtCusID.val())){
        txtCusName.focus();
    }
});

txtCusName.keyup(function (event) {
    validateCustName();
    if (event.key === 'Enter' && cusNameRegEx.test(txtCusName.val())){
        txtCusAddress.focus();
    }
});

txtCusAddress.keyup(function (event) {
    validateCustAddress();
    if (event.key === 'Enter' && cusAddressRegEx.test(txtCusAddress.val())){
        txtCusContact.focus();
    }
});

txtCusContact.keyup(function (event) {
    validateCustContact();
});

btnCusSearch.click(function () {
    if(isCustomerExists(txtCusSearch.val())){
        var customerObject;
        for(var i in customerDB){
            if(customerDB[i].getId()===$("#txtCusSearch").val()){
                customerObject = customerDB[i];
            }
        }

        txtCusID.val(customerObject.getId());
        txtCusName.val(customerObject.getName());
        txtCusAddress.val(customerObject.getAddress());
        txtCusContact.val(customerObject.getContact());

        validateCustId();
        validateCustName();
        validateCustAddress();
        validateCustContact();

    }else{
        alert("Customer Doesn't Exist...")
    }
});

btnCusSave.click(function () {
    let customerID = txtCusID.val();
    let customerName = txtCusName.val();
    let customerAddress = txtCusAddress.val();
    let customerContact = txtCusContact.val();

    /*var customerObject={
        id:customerID,
        name:customerName,
        address:customerAddress,
        contact:customerContact
    };*/

    var customerObject=new CustomerDTO(customerID,customerName,customerAddress,customerContact);

    if(isCustomerExists(customerID)){
        for(var i in customerDB){
            if(customerDB[i].getId()===customerID){
                customerDB[i]=customerObject;
            }
        }
    }else{
        customerDB.push(customerObject);
    }

    clearAllCustomerFields();
    setCustomerCombo();
    loadAllCustomers();

    loadFromCustomerTable();

});

btnCusUpdate.click(function () {
    let customerID = txtCusID.val();
    let customerName = txtCusName.val();
    let customerAddress = txtCusAddress.val();
    let customerContact = txtCusContact.val();

    /*var customerObject={
        id:customerID,
        name:customerName,
        address:customerAddress,
        contact:customerContact
    };*/

    var customerObject=new CustomerDTO(customerID,customerName,customerAddress,customerContact);

    if(isCustomerExists(customerID)){
        for(var i in customerDB){
            if(customerDB[i].getId()===customerID){
                customerDB[i]=customerObject;
            }
        }
    }else{
        customerDB.push(customerObject);
    }

    clearAllCustomerFields();
    setCustomerCombo();
    loadAllCustomers();

    loadFromCustomerTable();

});

btnCusDelete.click(function () {
    var index=-1;
    for(var i in customerDB){
        if(customerDB[i].getId()===$("#txtCusID").val()){
            index=i;
        }
    }
    if (index !== -1) {
        customerDB.splice(index, 1);
    }
    clearAllCustomerFields();
    setCustomerCombo();
    loadAllCustomers();

    loadFromCustomerTable();
});

function loadFromCustomerTable() {

    $("#customerTable>tr").click(function () {
        let cusID = $(this).children(":eq(0)").text();
        let cusName = $(this).children(":eq(1)").text();
        let cusAddress = $(this).children(":eq(2)").text();
        let cusContact = $(this).children(":eq(3)").text();

        console.log(cusID, cusName, cusAddress, cusContact);

        txtCusID.val(cusID);
        txtCusName.val(cusName);
        txtCusAddress.val(cusAddress);
        txtCusContact.val(cusContact);

        validateCustId();
        validateCustName();
        validateCustAddress();
        validateCustContact();
    });
}

function setCustomerCombo() {
    cmbOrderCusId.empty();
    cmbOrderCusId.append(new Option("Customer ID", ""));
    for (var i in customerDB){
        let id=customerDB[i].getId();
        cmbOrderCusId.append(new Option(id, id));
    }
}

function clearAllCustomerFields() {
    txtCusID.val('');
    txtCusName.val('');
    txtCusAddress.val('');
    txtCusContact.val('');

    txtCusID.css('border','1px solid #ced4da');
    txtCusName.css('border','1px solid #ced4da');
    txtCusAddress.css('border','1px solid #ced4da');
    txtCusContact.css('border','1px solid #ced4da');
}

function loadAllCustomers() {
    tblCustomer.empty();

    for (var i in customerDB){
        let id=customerDB[i].getId();
        let name=customerDB[i].getName();
        let address=customerDB[i].getAddress();
        let contact=customerDB[i].getContact();

        let row = `<tr><td>${id}</td><td>${name}</td><td>${address}</td><td>${contact}</td></tr>`;
        tblCustomer.append(row);
    }
}

function validateCustId(){
    if (cusIDRegEx.test(txtCusID.val())) {
        txtCusID.css('border','3px solid green');
    }else{
        txtCusID.css('border','3px solid red');
    }
}
function validateCustName(){
    if (cusNameRegEx.test(txtCusName.val())) {
        txtCusName.css('border','3px solid green');
    }else{
        txtCusName.css('border','3px solid red');
    }
}
function validateCustAddress(){
    if (cusAddressRegEx.test(txtCusAddress.val())) {
        txtCusAddress.css('border','3px solid green');
    }else{
        txtCusAddress.css('border','3px solid red');
    }
}
function validateCustContact(){
    if (cusContactRegEx.test(txtCusContact.val())) {
        txtCusContact.css('border','3px solid green');
    }else{
        txtCusContact.css('border','3px solid red');
    }
}