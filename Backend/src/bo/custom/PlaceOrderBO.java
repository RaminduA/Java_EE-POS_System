package bo.custom;

import bo.SuperBO;
import dto.CustomerDTO;
import dto.ItemDTO;
import dto.OrderDTO;

import java.sql.Connection;
import java.util.ArrayList;

public interface PlaceOrderBO extends SuperBO {
    ArrayList<String> getAllCustomerIds(Connection con);
    ArrayList<String> getAllItemCodes(Connection con);
    String getOrderId(Connection con);
    CustomerDTO getCustomer(Connection con, String id);
    ItemDTO getItem(Connection con, String code);
    boolean placeOrder(Connection con, OrderDTO dto);
}