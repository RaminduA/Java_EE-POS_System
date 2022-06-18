package bo.custom.impl;

import bo.custom.ManageOrderBO;
import dao.DAOFactory;
import dao.custom.CustomerDAO;
import dao.custom.ItemDAO;
import dao.custom.OrderDAO;
import dao.custom.OrderDetailDAO;
import dto.OrderDTO;
import dto.OrderDetailDTO;
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
            Order order=orderDAO.get(connection, id);
            ArrayList<OrderDetail> details=detailDAO.getAll(connection,id);
            ArrayList<OrderDetailDTO> detailList=new ArrayList<>();
            for(OrderDetail detail : details) {
                detailList.add(new OrderDetailDTO(
                        detail.getOrderId(),
                        detail.getItemCode(),
                        detail.getUnitPrice(),
                        detail.getOrderQty(),
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
            ArrayList<Order> orderList=orderDAO.getAll(connection);
            for (Order order : orderList) {
                ArrayList<OrderDetail> details=detailDAO.getAll(connection, order.getOrderId());
                ArrayList<OrderDetailDTO> detailList=new ArrayList<>();
                for(OrderDetail detail : details) {
                    detailList.add(new OrderDetailDTO(
                            detail.getOrderId(),
                            detail.getItemCode(),
                            detail.getUnitPrice(),
                            detail.getOrderQty(),
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
            return new OrderDetailDTO(detail.getOrderId(),detail.getItemCode(),detail.getUnitPrice(),detail.getOrderQty(),detail.getPrice());
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean updateOrder(Connection connection, OrderDTO dto) {
        try {
            return orderDAO.update(connection, new Order(dto.getOrderId(),dto.getCustomerId(),dto.getOrderDate(),dto.getOrderTime(),dto.getCost()));
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteOrder(Connection con, String id) {
        Connection connection=null;
        try{
            connection=con;
            connection.setAutoCommit(false);
            Order order = orderDAO.get(connection, id);
            ArrayList<OrderDetail> detailList=detailDAO.getAll(connection,id);
            boolean isOrderDeleted = orderDAO.delete(connection, id);
            if(isOrderDeleted){
                int affectedItemRows =0;
                for(OrderDetail detail : detailList) {
                    Item item=itemDAO.get(connection, detail.getItemCode());
                    item.setQtyOnHand(item.getQtyOnHand()+detail.getOrderQty());
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
    public boolean updateOrderDetail(Connection connection, OrderDTO dto, OrderDetailDTO detailDTO) {
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
            boolean isOrderUpdated = orderDAO.update(connection, order);
            if(isOrderUpdated){
                boolean isOrderDetailDeleted = detailDAO.delete(connection,detailDTO.getOrderId(),detailDTO.getItemCode());
                if(isOrderDetailDeleted){
                    Item item=itemDAO.get(connection, detailDTO.getItemCode());
                    item.setQtyOnHand(item.getQtyOnHand()+detailDTO.getOrderQty());
                    boolean isItemUpdated = itemDAO.update(connection, item);
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
