package bo.custom.impl;

import bo.custom.PlaceOrderBO;
import dao.DAOFactory;
import dao.custom.CustomerDAO;
import dao.custom.ItemDAO;
import dao.custom.OrderDAO;
import dao.custom.OrderDetailDAO;
import dto.CustomerDTO;
import dto.ItemDTO;
import dto.OrderDTO;
import dto.OrderDetailDTO;
import entity.Customer;
import entity.Item;
import entity.Order;
import entity.OrderDetail;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class PlaceOrderBOImpl implements PlaceOrderBO {
    private final CustomerDAO customerDAO=(CustomerDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.CUSTOMER);
    private final ItemDAO itemDAO=(ItemDAO)DAOFactory.getInstance().getDAO(DAOFactory.DAOType.ITEM);
    private final OrderDAO orderDAO=(OrderDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.ORDER);
    private final OrderDetailDAO detailDAO=(OrderDetailDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.ORDERDETAIL);

    @Override
    public ArrayList<String> getAllCustomerIds(Connection connection) {
        ArrayList<String> customerIds=new ArrayList<>();
        try {
            customerIds=customerDAO.getAllIds(connection);
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return customerIds;
    }

    @Override
    public ArrayList<String> getAllItemCodes(Connection connection) {
        ArrayList<String> itemCodes=new ArrayList<>();
        try {
            itemCodes=itemDAO.getAllIds(connection);
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return itemCodes;
    }

    @Override
    public String getOrderId(Connection connection) {
        try {
            return orderDAO.getId(connection);
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return "";
    }

    @Override
    public ItemDTO getItem(Connection connection, String code) {
        try {
            Item item=itemDAO.get(connection, code);
            return new ItemDTO(item.getItemCode(),item.getName(),item.getUnitPrice(),item.getQtyOnHand());
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public CustomerDTO getCustomer(Connection connection, String id) {
        try {
            Customer customer=customerDAO.get(connection, id);
            return new CustomerDTO(customer.getCustomerId(),customer.getName(),customer.getAddress(),customer.getContact());
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean placeOrder(Connection con, OrderDTO dto){
        Connection connection=null;
        try{
            connection=con;
            connection.setAutoCommit(false);
            boolean isOrderSaved = orderDAO.add(connection, new Order(dto.getOrderId(), dto.getCustomerId(), dto.getOrderDate(), dto.getOrderTime(), dto.getCost()));
            if(isOrderSaved){
                ArrayList<OrderDetailDTO> detailList=dto.getDetailList();
                int affectedDetailRows =0;
                for(OrderDetailDTO detailDTO : detailList) {
                    boolean isDetailAdded = detailDAO.add(connection, new OrderDetail(detailDTO.getOrderId(), detailDTO.getItemCode(), detailDTO.getUnitPrice(), detailDTO.getOrderQty(), detailDTO.getPrice()));
                    if(isDetailAdded){
                        affectedDetailRows++;
                    }else{
                        return false;
                    }
                }
                System.out.println(detailList.size()+"-->"+ affectedDetailRows);
                boolean isOrderDetailSaved = (detailList.size() == affectedDetailRows);
                if(isOrderDetailSaved){
                    int affectedItemRows=0;
                    for(OrderDetailDTO detailDTO : detailList) {
                        Item item=itemDAO.get(connection, detailDTO.getItemCode());
                        item.setQtyOnHand(item.getQtyOnHand()-detailDTO.getOrderQty());
                        boolean isItemUpdated = itemDAO.update(connection, item);
                        if(isItemUpdated){
                            affectedItemRows++;
                        }else{
                            return false;
                        }
                    }
                    System.out.println(detailList.size()+"-->"+ affectedItemRows);
                    boolean isAllItemsUpdated = (detailList.size() == affectedItemRows);
                    if(isAllItemsUpdated){
                        connection.commit();
                        return true;
                    }else{
                        connection.rollback();
                        return false;
                    }
                }else{
                    connection.rollback();
                    return false;
                }
            }else{
                connection.rollback();
                return false;
            }
        }catch(SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }finally{
            try {
                assert connection != null;
                connection.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return false;
    }
}
