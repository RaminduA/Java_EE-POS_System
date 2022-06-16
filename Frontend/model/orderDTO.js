function OrderDTO(orderId,customerId,orderDate,orderTime,cost,detailList) {
    this.__orderId=orderId;
    this.__customerId=customerId;
    this.__orderDate=orderDate;
    this.__orderTime=orderTime;
    this.__cost=cost;
    this.__detailList=detailList;

    this.setOrderId=function(orderId){
        this.__orderId=orderId;
    }
    this.getOrderId=function(){
        return this.__orderId;
    }
    this.setCustomerId=function(customerId){
        this.__customerId=customerId;
    }
    this.getCustomerId=function(){
        return this.__customerId;
    }
    this.setOrderDate=function(orderDate){
        this.__orderDate=orderDate;
    }
    this.getOrderDate=function(){
        return this.__orderDate;
    }
    this.setOrderTime=function(orderTime){
        this.__orderTime=orderTime;
    }
    this.getOrderTime=function(){
        return this.__orderTime;
    }
    this.setCost=function(cost){
        this.__cost=cost;
    }
    this.getCost=function(){
        return this.__cost;
    }
    this.setDetailList=function(detailList){
        this.__detailList=detailList;
    }
    this.getDetailList=function(){
        return this.__detailList;
    }

}
