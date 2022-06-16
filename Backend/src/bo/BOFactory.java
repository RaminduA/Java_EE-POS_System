package bo;

import bo.custom.impl.CustomerBOImpl;
import bo.custom.impl.ItemBOImpl;
import bo.custom.impl.ManageOrderBOImpl;
import bo.custom.impl.PlaceOrderBOImpl;

public class BOFactory {
    private static BOFactory boFactory;

    private BOFactory(){}

    public static BOFactory getInstance(){
        return (boFactory==null) ? (boFactory=new BOFactory()) : boFactory;
    }

    public enum BOType{
        CUSTOMER, ITEM, PLACEORDER, MANAGEORDER
    }

    public SuperBO getBO(BOType boType){
        switch(boType){
            case CUSTOMER:
                return new CustomerBOImpl();
            case ITEM:
                return new ItemBOImpl();
            case PLACEORDER:
                return new PlaceOrderBOImpl();
            case MANAGEORDER:
                return new ManageOrderBOImpl();
            default:
                return null;
        }
    }
}