package bo.custom;

import bo.SuperBO;
import dto.ItemDTO;

import java.sql.Connection;
import java.util.ArrayList;

public interface ItemBO extends SuperBO {
    boolean addItem(Connection con, ItemDTO dto);
    boolean updateItem(Connection con, ItemDTO dto);
    boolean deleteItem(Connection con, String code);
    ArrayList<ItemDTO> getAllItems(Connection con);
    String getItemCode(Connection con);
}
