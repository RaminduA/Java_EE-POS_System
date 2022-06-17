package dao.custom.impl;

import dao.custom.OrderDetailDAO;
import entity.OrderDetail;
import util.CrudUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDetailDAOImpl implements OrderDetailDAO {
    @Override
    public boolean add(Connection connection, OrderDetail orderDetail) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate(connection,"INSERT INTO `Order Detail` VALUES(?,?,?,?,?)",orderDetail.getOrderId(),orderDetail.getItemCode(),orderDetail.getUnitPrice(),orderDetail.getOrderQty(),orderDetail.getPrice());
    }

    @Override
    public boolean update(Connection connection, OrderDetail orderDetail) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate(connection,"UPDATE `Order Detail` SET orderQty=?, discount=?, price=? WHERE orderId=? AND itemCode=?",orderDetail.getUnitPrice(),orderDetail.getOrderQty(),orderDetail.getPrice(),orderDetail.getOrderId(),orderDetail.getItemCode());
    }

    @Override
    public boolean delete(Connection connection, String id) throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("Not Supported Yet");
    }

    @Override
    public boolean delete(Connection connection, String orderId, String itemCode) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate(connection,"DELETE FROM `Order Detail` WHERE orderId=? AND itemCode=?",orderId,itemCode);
    }

    @Override
    public OrderDetail get(Connection connection, String id) throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("Not Supported Yet");
    }

    @Override
    public OrderDetail get(Connection connection, String orderId, String itemCode) throws SQLException, ClassNotFoundException {
        OrderDetail orderDetail=null;
        ResultSet resultSet=CrudUtil.executeQuery(connection,"SELECT * FROM `Order Detail` WHERE orderId=? AND itemCode=?",orderId,itemCode);
        if(resultSet.next()){
            orderDetail=new OrderDetail(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getInt(4),
                    resultSet.getDouble(5)
            );
        }
        return orderDetail;
    }

    @Override
    public ArrayList<OrderDetail> getAll(Connection connection) throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("Not Supported Yet");
    }

    @Override
    public ArrayList<OrderDetail> getAll(Connection connection, String orderId) throws SQLException, ClassNotFoundException {
        ArrayList<OrderDetail> orderDetails=new ArrayList<>();
        ResultSet resultSet=CrudUtil.executeQuery(connection,"SELECT * FROM `Order Detail` WHERE orderId=?",orderId);
        while(resultSet.next()){
            orderDetails.add(
                    new OrderDetail(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getDouble(3),
                            resultSet.getInt(4),
                            resultSet.getDouble(5)
                    )
            );
        }
        return orderDetails;
    }

    @Override
    public String getId(Connection connection) throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("Not Supported Yet");
    }

    @Override
    public ArrayList<String> getAllIds(Connection connection) throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("Not Supported Yet");
    }

}
