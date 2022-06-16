function OrderDetailDTO(itemId,orderId,itemQuantity,itemPrice,total) {
    this.__itemId=itemId;
    this.__orderId=orderId;
    this.__itemQuantity=itemQuantity;
    this.__itemPrice=itemPrice;
    this.__total=total;

    this.setItemId=function(itemId){
        this.__itemId=itemId;
    }
    this.getItemId=function(){
        return this.__itemId;
    }
    this.setOrderId=function(orderId){
        this.__orderId=orderId;
    }
    this.getOrderId=function(){
        return this.__orderId;
    }
    this.setItemQuantity=function(itemQuantity){
        this.__itemQuantity=itemQuantity;
    }
    this.getItemQuantity=function(){
        return this.__itemQuantity;
    }
    this.setItemPrice=function(itemPrice){
        this.__itemPrice=itemPrice;
    }
    this.getItemPrice=function(){
        return this.__itemPrice;
    }
    this.setTotal=function(total){
        this.__total=total;
    }
    this.getTotal=function(){
        return this.__total;
    }

}
