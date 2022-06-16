package bo.custom.impl;

import bo.custom.ManageOrderBO;
import dao.DAOFactory;
import dao.custom.CustomerDAO;
import dao.custom.ItemDAO;
import dao.custom.OrderDAO;
import dao.custom.OrderDetailDAO;
import dto.OrderDTO;
import dto.OrderDetailDTO;
import db.DatabaseConnection;
import entity.Item;
import entity.Order;
import entity.OrderDetail;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class ManageOrderBOImpl implements ManageOrderBO {
    private final CustomerDAO customerDAO=(CustomerDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.CUSTOMER);
    private final ItemDAO itemDAO=(ItemDAO)DAOFactory.getInstance().getDAO(DAOFactory.DAOType.ITEM);
    private final OrderDAO orderDAO=(OrderDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.ORDER);
    private final OrderDetailDAO detailDAO=(OrderDetailDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.ORDERDETAIL);

    @Override
    public OrderDTO getOrder(Connection connection, String id) {
        try {
            Order order=orderDAO.get(id);
            ArrayList<OrderDetail> details=detailDAO.getAll(connection,id);
            ArrayList<OrderDetailDTO> detailList=new ArrayList<>();
            for(OrderDetail detail : details) {
                detailList.add(new OrderDetailDTO(
                        detail.getOrderId(),
                        detail.getItemCode(),
                        detail.getOrderQty(),
                        detail.getDiscount(),
                        detail.getPrice()
                ));
            }
            return new OrderDTO(order.getOrderId(),order.getCustomerId(),order.getOrderDate(),order.getOrderTime(),order.getCost(),detailList);
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<OrderDTO> getAllOrders(Connection connection) {
        ArrayList<OrderDTO> orders=new ArrayList<>();
        try {
            ArrayList<Order> orderList=orderDAO.getAll();
            for (Order order : orderList) {
                ArrayList<OrderDetail> details=detailDAO.getAll(connection, order.getOrderId());
                ArrayList<OrderDetailDTO> detailList=new ArrayList<>();
                for(OrderDetail detail : details) {
                    detailList.add(new OrderDetailDTO(
                            detail.getOrderId(),
                            detail.getItemCode(),
                            detail.getOrderQty(),
                            detail.getDiscount(),
                            detail.getPrice()
                    ));
                }
                orders.add(new OrderDTO(order.getOrderId(),order.getCustomerId(),order.getOrderDate(),order.getOrderTime(),order.getCost(),detailList));
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return orders;
    }

    @Override
    public OrderDetailDTO getOrderDetail(Connection connection, String orderId, String itemCode) {
        try {
            OrderDetail detail=detailDAO.get(connection,orderId,itemCode);
            return new OrderDetailDTO(detail.getOrderId(),detail.getItemCode(),detail.getOrderQty(),detail.getDiscount(),detail.getPrice());
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean updateOrder(OrderDTO dto) {
        try {
            return orderDAO.update(new Order(dto.getOrderId(),dto.getCustomerId(),dto.getOrderDate(),dto.getOrderTime(),dto.getCost()));
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteOrder(String id) {
        Connection connection=null;
        try{
            connection= DatabaseConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            Order order = orderDAO.get(id);
            ArrayList<OrderDetail> detailList=detailDAO.getAll(connection,id);
            boolean isOrderDeleted = orderDAO.delete(id);
            if(isOrderDeleted){
                int affectedItems =0;
                for(OrderDetail detail : detailList) {
                    Item item=itemDAO.get(detail.getItemCode());
                    item.setQtyOnHand(item.getQtyOnHand()+detail.getOrderQty());
                    boolean isUpdated = itemDAO.update(item);
                    if(isUpdated){
                        affectedItems++;
                    }else{
                        return false;
                    }
                }
                System.out.println(detailList.size()+"-->"+ affectedItems);
                boolean isItemUpdated = (detailList.size()== affectedItems);
                if(isItemUpdated){
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

    @Override
    public boolean updateOrderDetail(OrderDTO dto, OrderDetailDTO detailDTO) {
        return false;
    }

    @Override
    public boolean deleteOrderDetail(Connection con, OrderDetailDTO detailDTO) {
        Connection connection=null;
        try{
            connection=con;
            connection.setAutoCommit(false);
            Order order=orderDAO.get(connection,detailDTO.getOrderId());
            double newCost=order.getCost()-detailDTO.getPrice();
            order.setCost(newCost);
            boolean isOrderUpdated = orderDAO.update(order);
            if(isOrderUpdated){
                boolean isOrderDetailDeleted = detailDAO.delete(connection,detailDTO.getOrderId(),detailDTO.getItemCode());
                if(isOrderDetailDeleted){
                    Item item=itemDAO.get(detailDTO.getItemCode());
                    item.setQtyOnHand(item.getQtyOnHand()+detailDTO.getOrderQty());
                    boolean isItemUpdated = itemDAO.update(item);
                    if(isItemUpdated){
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
