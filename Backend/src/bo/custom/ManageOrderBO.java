package bo.custom;

import bo.SuperBO;
import dto.OrderDTO;
import dto.OrderDetailDTO;

import java.sql.Connection;
import java.util.ArrayList;

public interface ManageOrderBO extends SuperBO {
    OrderDTO getOrder(Connection con, String id);
    ArrayList<OrderDTO> getAllOrders(Connection con);
    OrderDetailDTO getOrderDetail(Connection con, String orderId, String itemCode);
    boolean updateOrder(Connection con, OrderDTO dto);
    boolean deleteOrder(Connection con, String id);
    boolean updateOrderDetail(Connection con, OrderDTO dto, OrderDetailDTO detailDTO);
    boolean deleteOrderDetail(Connection con, OrderDetailDTO detailDTO);
}
