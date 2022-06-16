function OrderTM(itemCode,itemName,price,quantity,total) {
    this.__itemCode=itemCode;
    this.__itemName=itemName;
    this.__price=price;
    this.__quantity=quantity;
    this.__total=total;

    this.setItemCode=function(itemCode){
        this.__itemCode=itemCode;
    }
    this.getItemCode=function(){
        return this.__itemCode;
    }
    this.setItemName=function(itemName){
        this.__itemName=itemName;
    }
    this.getItemName=function(){
        return this.__itemName;
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
    this.getQuantity=function() {
        return this.__quantity;
    }
    this.setTotal=function(total){
        this.__total=total;
    }
    this.getTotal=function(){
        return this.__total;
    }

}
