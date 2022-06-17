package dao.custom.impl;

import dao.custom.OrderDAO;
import entity.Order;
import util.CrudUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDAOImpl implements OrderDAO {

    @Override
    public boolean add(Connection connection, Order order) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate(connection,"INSERT INTO `Order` VALUES(?,?,?,?,?)",order.getOrderId(),order.getCustomerId(),order.getOrderDate(),order.getOrderTime(),order.getCost());
    }

    @Override
    public boolean update(Connection connection, Order order) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate(connection,"UPDATE `Order` SET customerId=?, orderDate=?, orderTime=?, cost=? WHERE orderId=?",order.getCustomerId(),order.getOrderDate(),order.getOrderTime(),order.getCost(),order.getOrderId());
    }

    @Override
    public boolean delete(Connection connection, String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate(connection,"DELETE FROM `Order` WHERE orderId=?",id);
    }

    @Override
    public Order get(Connection connection, String id) throws SQLException, ClassNotFoundException {
        Order order=null;
        ResultSet resultSet=CrudUtil.executeQuery(connection,"SELECT * FROM `Order` WHERE orderId=?",id);
        if(resultSet.next()){
            order=new Order(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getDouble(5)
            );
        }
        return order;
    }

    @Override
    public ArrayList<Order> getAll(Connection connection) throws SQLException, ClassNotFoundException {
        ArrayList<Order> orders=new ArrayList<>();
        ResultSet resultSet=CrudUtil.executeQuery(connection,"SELECT * FROM `Order`");
        while(resultSet.next()){
            orders.add(
                    new Order(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getString(4),
                            resultSet.getDouble(5)
                    )
            );
        }
        return orders;
    }

    @Override
    public String getId(Connection connection) throws SQLException, ClassNotFoundException {
        ResultSet resultSet=CrudUtil.executeQuery(connection,"SELECT orderId FROM `Order` ORDER BY orderId DESC LIMIT 1");
        if(resultSet.next()){
            int index=Integer.parseInt(resultSet.getString(1).split("-")[1]);
            if(index<9){
                return "O-00000"+ ++index;
            }else if(index<99){
                return "O-0000"+ ++index;
            }else if(index<999){
                return "O-000"+ ++index;
            }else if(index<9999){
                return "O-00"+ ++index;
            }else if(index<99999){
                return "O-0"+ ++index;
            }else{
                return "O-"+ ++index;
            }
        }else{
            return "O-000001";
        }
    }

    @Override
    public ArrayList<String> getAllIds(Connection connection) throws SQLException, ClassNotFoundException {
        ArrayList<String> orders=new ArrayList<>();
        ResultSet resultSet=CrudUtil.executeQuery(connection,"SELECT orderId FROM `Order`");
        while(resultSet.next()){
            orders.add(resultSet.getString(1));
        }
        return orders;
    }
}
