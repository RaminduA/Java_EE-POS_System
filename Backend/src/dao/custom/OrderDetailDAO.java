package dao.custom;

import dao.CrudDAO;
import entity.OrderDetail;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public interface OrderDetailDAO extends CrudDAO<OrderDetail> {
    boolean delete(Connection connection,String orderId, String itemCode) throws SQLException, ClassNotFoundException;
    OrderDetail get(Connection connection,String orderId,String itemCode) throws SQLException, ClassNotFoundException;
    ArrayList<OrderDetail> getAll(Connection connection,String orderId) throws SQLException, ClassNotFoundException;
}
