package bo.custom.impl;

import bo.custom.CustomerBO;
import dao.DAOFactory;
import dao.custom.CustomerDAO;
import dto.CustomerDTO;
import entity.Customer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerBOImpl implements CustomerBO {
    private final CustomerDAO customerDAO=(CustomerDAO)DAOFactory.getInstance().getDAO(DAOFactory.DAOType.CUSTOMER);

    @Override
    public CustomerDTO getCustomer(Connection connection, String id) {
        try {
            Customer customer = customerDAO.get(connection, id);
            if (customer!=null) {
                return new CustomerDTO(customer.getCustomerId(),customer.getName(),customer.getAddress(),customer.getContact());
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean addCustomer(Connection connection, CustomerDTO dto) {
        try {
            return customerDAO.add(connection, new Customer(dto.getCustomerId(),dto.getName(),dto.getAddress(),dto.getContact()));
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateCustomer(Connection connection, CustomerDTO dto) {
        try {
            return customerDAO.update(connection, new Customer(dto.getCustomerId(),dto.getName(),dto.getAddress(),dto.getContact()));
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteCustomer(Connection connection, String id) {
        try {
            return customerDAO.delete(connection, id);
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public ArrayList<CustomerDTO> getAllCustomers(Connection connection) {
        ArrayList<CustomerDTO> customers=new ArrayList<>();
        try {
            ArrayList<Customer> customerList=customerDAO.getAll(connection);
            for(Customer customer : customerList) {
                customers.add(
                    new CustomerDTO(customer.getCustomerId(),customer.getName(),customer.getAddress(),customer.getContact())
                );
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return customers;
    }

    @Override
    public String getCustomerId(Connection connection) {
        try {
            return customerDAO.getId(connection);
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return "";
    }

}
