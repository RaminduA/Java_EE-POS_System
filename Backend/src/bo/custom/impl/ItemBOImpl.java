package bo.custom.impl;

import bo.custom.ItemBO;
import dao.DAOFactory;
import dao.custom.ItemDAO;
import dto.ItemDTO;
import entity.Item;

import java.sql.SQLException;
import java.util.ArrayList;

public class ItemBOImpl implements ItemBO {
    private final ItemDAO itemDAO=(ItemDAO)DAOFactory.getInstance().getDAO(DAOFactory.DAOType.ITEM);

    @Override
    public boolean addItem(ItemDTO dto) {
        try {
            return itemDAO.add(new Item(dto.getItemCode(),dto.getDescription(),dto.getQtyOnHand(),dto.getUnitPrice(),dto.getDiscountPercent()));
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateItem(ItemDTO dto) {
        try {
            return itemDAO.update(new Item(dto.getItemCode(),dto.getDescription(),dto.getQtyOnHand(),dto.getUnitPrice(),dto.getDiscountPercent()));
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteItem(String code) {
        try {
            return itemDAO.delete(code);
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public ArrayList<ItemDTO> getAllItems() {
        ArrayList<ItemDTO> items=new ArrayList<>();
        try {
            ArrayList<Item> itemList=itemDAO.getAll();
            for(Item item : itemList) {
                items.add(
                        new ItemDTO(item.getItemCode(),item.getDescription(),item.getQtyOnHand(),item.getUnitPrice(),item.getDiscountPercent())
                );
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return items;
    }

    @Override
    public String getItemCode() {
        try {
            return itemDAO.getId();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return "";
    }
}
