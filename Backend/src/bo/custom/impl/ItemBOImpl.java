package bo.custom.impl;

import bo.custom.ItemBO;
import dao.DAOFactory;
import dao.custom.ItemDAO;
import dto.ItemDTO;
import entity.Item;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class ItemBOImpl implements ItemBO {
    private final ItemDAO itemDAO=(ItemDAO)DAOFactory.getInstance().getDAO(DAOFactory.DAOType.ITEM);

    @Override
    public boolean addItem(Connection connection, ItemDTO dto) {
        try {
            return itemDAO.add(connection, new Item(dto.getItemCode(),dto.getName(),dto.getUnitPrice(),dto.getQtyOnHand()));
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateItem(Connection connection, ItemDTO dto) {
        try {
            return itemDAO.update(connection, new Item(dto.getItemCode(),dto.getName(),dto.getUnitPrice(),dto.getQtyOnHand()));
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteItem(Connection connection, String code) {
        try {
            return itemDAO.delete(connection, code);
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public ArrayList<ItemDTO> getAllItems(Connection connection) {
        ArrayList<ItemDTO> items=new ArrayList<>();
        try {
            ArrayList<Item> itemList=itemDAO.getAll(connection);
            for(Item item : itemList) {
                items.add(
                        new ItemDTO(item.getItemCode(),item.getName(),item.getUnitPrice(),item.getQtyOnHand())
                );
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return items;
    }

    @Override
    public String getItemCode(Connection connection) {
        try {
            return itemDAO.getId(connection);
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return "";
    }
}
