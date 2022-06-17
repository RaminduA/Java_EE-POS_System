package bo.custom;

import bo.SuperBO;
import dto.CustomerDTO;

import java.sql.Connection;
import java.util.ArrayList;

public interface CustomerBO extends SuperBO {
    boolean addCustomer(Connection con, CustomerDTO dto);
    boolean updateCustomer(Connection con, CustomerDTO dto);
    boolean deleteCustomer(Connection con, String id);
    ArrayList<CustomerDTO> getAllCustomers(Connection con);
    String getCustomerId(Connection con);
}
