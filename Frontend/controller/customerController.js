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

    let jsonReq = {option : "SEARCH",data : {id: txtCusSearch.val()}}

    $.ajax({
        url:"http://localhost:8080/Backend/customer",
        method:"GET",
        contentType:"application/json",
        //JSON.stringify() method converts a js object to a valid json string
        data:JSON.stringify(jsonReq),
        success:function (jsonResp) {
            if(jsonResp.status===200){
                alert(jsonResp.message);
                //searchAndLoadCustomer(jsonResp.data);
                console.log(jsonResp.data);
                loadAllCustomers();
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
    //loadFromCustomerTable();




    /*if(isCustomerExists(txtCusSearch.val())){
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
    }*/
});

btnCusSave.click(function () {

    let jsonReq = {option : "",data : {id: txtCusID.val(),name: txtCusName.val(),address: txtCusAddress.val(),contact: txtCusContact.val()}}

    $.ajax({
        url:"http://localhost:8080/Backend/customer",
        method:"POST",
        contentType:"application/json",
        data:JSON.stringify(jsonReq),
        success:function (jsonResp) {
            if(jsonResp.status===200){
                alert(jsonResp.message);
                console.log(jsonResp.data);
                loadAllCustomers();
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

});

btnCusUpdate.click(function () {

    let jsonReq = {option : "",data : {id: txtCusID.val(),name: txtCusName.val(),address: txtCusAddress.val(),contact: txtCusContact.val()}}

    $.ajax({
        url:"http://localhost:8080/Backend/customer",
        method:"PUT",
        contentType:"application/json",
        data:JSON.stringify(jsonReq),
        success:function (jsonResp) {
            if(jsonResp.status===200){
                alert(jsonResp.message);
                console.log(jsonResp.data);
                loadAllCustomers();
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

});

btnCusDelete.click(function () {

    let jsonReq = {option : "",data : {id: txtCusID.val()}}

    $.ajax({
        url:"http://localhost:8080/Backend/customer",
        method:"DELETE",
        contentType:"application/json",
        data:JSON.stringify(jsonReq),
        success:function (jsonResp) {
            if(jsonResp.status===200){
                alert(jsonResp.message);
                console.log(jsonResp.data);
                loadAllCustomers();
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

});

function loadFromCustomerTable() {

    $("#customerTable>tr").click(function () {
        let cusID = $(this).children(":eq(1)").text();
        let cusName = $(this).children(":eq(2)").text();
        let cusAddress = $(this).children(":eq(3)").text();
        let cusContact = $(this).children(":eq(4)").text();

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

function searchAndLoadCustomer(data) {
    txtCusID.val(data.id);
    txtCusName.val(data.name);
    txtCusAddress.val(data.address);
    txtCusContact.val(data.contact);
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

    let jsonReq = {option : "GET-ALL",data : ""}

    $.ajax({
        url:"http://localhost:8080/Backend/customer",
        method:"GET",
        contentType:"application/json",
        data:JSON.stringify(jsonReq),
        success:function (jsonResp) {
            if(resp.status===200){
                alert(jsonResp.message);
                console.log(jsonResp.data);
                loadCustomerTable(jsonResp.data);
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

    function loadCustomerTable(data) {
        tblCustomer.empty();

        for (let i=0; i<data.length; i++){
            let id=data[i].id;
            let name=data[i].name;
            let address=data[i].address;
            let contact=data[i].contact;

            let row = `<tr scope="row"><td>${i+1}</td><td><a href="#">${id}</a></td><td>${name}</td><td>${address}</td><td>${contact}</td></tr><tr class="spacer"><td colspan="100"></td></tr>`;
            tblCustomer.append(row);
        }
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