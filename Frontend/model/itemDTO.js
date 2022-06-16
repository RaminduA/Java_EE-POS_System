function ItemDTO(code,name,price,quantity) {
    this.__code=code;
    this.__name=name;
    this.__price=price;
    this.__quantity=quantity;

    this.setCode=function(code){
        this.__code=code;
    }
    this.getCode=function(){
        return this.__code;
    }
    this.setName=function(name){
        this.__name=name;
    }
    this.getName=function(){
        return this.__name;
    }
    this.setPrice=function(price){
        this.__price=price;
    }
    this.getPrice=function(){
        return this.__price;
    }
    this.setQuantity=function(quantity){
        this.__quantity=quantity;
    }
    this.getQuantity=function(){
        return this.__quantity;
    }

}
