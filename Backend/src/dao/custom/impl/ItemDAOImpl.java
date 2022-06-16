package dao.custom.impl;

import dao.custom.ItemDAO;
import entity.Item;
import util.CrudUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ItemDAOImpl implements ItemDAO {
    @Override
    public boolean add(Connection connection, Item item) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate(connection,"INSERT INTO Item VALUES (?,?,?,?)",item.getItemCode(),item.getName(),item.getUnitPrice(),item.getQtyOnHand());

    }

    @Override
    public boolean update(Connection connection, Item item) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate(connection,"UPDATE Item SET name=?, price=?, quantity=? WHERE itemCode=?",item.getName(),item.getUnitPrice(),item.getQtyOnHand(),item.getItemCode());
    }

    @Override
    public boolean delete(Connection connection, String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate(connection,"DELETE FROM Item WHERE itemCode=?",id);
    }

    @Override
    public Item get(Connection connection, String id) throws SQLException, ClassNotFoundException {
        Item item=null;
        ResultSet resultSet=CrudUtil.executeQuery(connection,"SELECT * FROM Item WHERE itemCode=?",id);
        if(resultSet.next()){
            item=new Item(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getInt(4)
            );
        }
        return item;
    }

    @Override
    public ArrayList<Item> getAll(Connection connection) throws SQLException, ClassNotFoundException {
        ArrayList<Item> items=new ArrayList<>();
        ResultSet resultSet=CrudUtil.executeQuery(connection,"SELECT * FROM Item");
        while(resultSet.next()){
            items.add(
                    new Item(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getDouble(3),
                            resultSet.getInt(4)
                    )
            );
        }
        return items;
    }

    @Override
    public String getId(Connection connection) throws SQLException, ClassNotFoundException {
        ResultSet resultSet=CrudUtil.executeQuery(connection,"SELECT itemCode FROM Item ORDER BY itemCode DESC LIMIT 1");
        if(resultSet.next()){
            int index=Integer.parseInt(resultSet.getString(1).split("-")[1]);
            if(index<9){
                return "I-00000"+ ++index;
            }else if(index<99){
                return "I-0000"+ ++index;
            }else if(index<999){
                return "I-000"+ ++index;
            }else if(index<9999){
                return "I-00"+ ++index;
            }else if(index<99999){
                return "I-0"+ ++index;
            }else{
                return "I-"+ ++index;
            }
        }else{
            return "I-000001";
        }
    }

    @Override
    public ArrayList<String> getAllIds(Connection connection) throws SQLException, ClassNotFoundException {
        ArrayList<String> items=new ArrayList<>();
        ResultSet resultSet=CrudUtil.executeQuery(connection,"SELECT itemCode FROM Item");
        while(resultSet.next()){
            items.add(resultSet.getString(1));
        }
        return items;
    }

}
