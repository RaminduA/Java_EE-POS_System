package dao.custom.impl;

import dao.custom.CustomerDAO;
import entity.Customer;
import util.CrudUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerDAOImpl implements CustomerDAO {

    @Override
    public boolean add(Connection connection, Customer customer) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate(connection,"INSERT INTO Customer VALUES (?,?,?,?)",customer.getCustomerId(),customer.getName(),customer.getAddress(),customer.getContact());
    }

    @Override
    public boolean update(Connection connection, Customer customer) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate(connection,"UPDATE Customer SET name=?, address=?, contact=? WHERE customerId=?",customer.getName(),customer.getAddress(),customer.getContact(),customer.getCustomerId());
    }

    @Override
    public boolean delete(Connection connection, String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate(connection,"DELETE FROM Customer WHERE customerId=?",id);
    }

    @Override
    public Customer get(Connection connection, String id) throws SQLException, ClassNotFoundException {
        Customer customer=null;
        ResultSet resultSet=CrudUtil.executeQuery(connection,"SELECT * FROM Customer WHERE customerId=?",id);
        if(resultSet.next()){
            customer=new Customer(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getString(4)
                    );
        }
        return customer;
    }

    @Override
    public ArrayList<Customer> getAll(Connection connection) throws SQLException, ClassNotFoundException {
        ArrayList<Customer> customers=new ArrayList<>();
        ResultSet resultSet=CrudUtil.executeQuery(connection,"SELECT * FROM Customer");
        while(resultSet.next()){
            customers.add(
                new Customer(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4)
                )
            );
        }
        return customers;
    }

    @Override
    public String getId(Connection connection) throws SQLException, ClassNotFoundException {
        ResultSet resultSet=CrudUtil.executeQuery(connection,"SELECT customerId FROM Customer ORDER BY customerId DESC LIMIT 1");
        if(resultSet.next()){
            int index=Integer.parseInt(resultSet.getString(1).split("-")[1]);
            if(index<9){
                return "C-0000"+ ++index;
            }else if(index<99){
                return "C-000"+ ++index;
            }else if(index<999){
                return "C-00"+ ++index;
            }else if(index<9999){
                return "C-0"+ ++index;
            }else{
                return "C-"+ ++index;
            }
        }else{
            return "C-00001";
        }
    }

    @Override
    public ArrayList<String> getAllIds(Connection connection) throws SQLException, ClassNotFoundException {
        ArrayList<String> customers=new ArrayList<>();
        ResultSet resultSet=CrudUtil.executeQuery(connection,"SELECT customerId FROM Customer");
        while(resultSet.next()){
            customers.add(resultSet.getString(1));
        }
        return customers;
    }
}
