function CustomerDTO(id,name,address,contact) {
    this.__id=id;
    this.__name=name;
    this.__address=address;
    this.__contact=contact;

    this.setId=function(id){
        this.__id=id;
    }
    this.getId=function(){
        return this.__id;
    }
    this.setName=function(name){
        this.__name=name;
    }
    this.getName=function(){
        return this.__name;
    }
    this.setAddress=function(address){
        this.__address=address;
    }
    this.getAddress=function(){
        return this.__address;
    }
    this.setContact=function(contact){
        this.__contact=contact;
    }
    this.getContact=function(){
        return this.__contact;
    }

}
