package controller.servlet;

import bo.BOFactory;
import bo.custom.CustomerBO;
import dto.CustomerDTO;

import javax.annotation.Resource;
import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(urlPatterns = "/customer")
public class CustomerServlet extends HttpServlet {
    CustomerBO customerBO = (CustomerBO)BOFactory.getInstance().getBO(BOFactory.BOType.CUSTOMER);

    @Resource(name = "java:comp/env/jdbc/pool")
    DataSource dataSource;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();

        try {
            Connection connection = dataSource.getConnection();

            JsonReader reader = Json.createReader(req.getReader());
            JsonObject jsonReq = reader.readObject();
            String option = jsonReq.getString("option");

            switch (option){

                case "GET-ALL":
                    ArrayList<CustomerDTO> allCustomers = customerBO.getAllCustomers(connection);
                    JsonArrayBuilder responseData = Json.createArrayBuilder();

                    for (CustomerDTO customer : allCustomers) {
                        JsonObjectBuilder jsonCustomer = Json.createObjectBuilder();
                        jsonCustomer.add("id",customer.getCustomerId());
                        jsonCustomer.add("name",customer.getName());
                        jsonCustomer.add("address",customer.getAddress());
                        jsonCustomer.add("contact",customer.getContact());

                        responseData.add(jsonCustomer.build());
                    }

                    JsonObjectBuilder jsonResponse = Json.createObjectBuilder();
                    resp.setStatus(HttpServletResponse.SC_OK);
                    jsonResponse.add("status",resp.getStatus());
                    jsonResponse.add("message","Done");
                    jsonResponse.add("data",responseData.build());

                    writer.print(jsonResponse.build());

                    break;

                case "SEARCH":
                    JsonObject reqData = jsonReq.getJsonObject("data");
                    String customerId = reqData.getString("id");
                    CustomerDTO customerDTO = customerBO.getCustomer(connection,customerId);

                    if (customerDTO!=null) {
                        JsonObjectBuilder respData = Json.createObjectBuilder();
                        respData.add("id",customerDTO.getCustomerId());
                        respData.add("name",customerDTO.getName());
                        respData.add("address",customerDTO.getAddress());
                        respData.add("contact",customerDTO.getContact());

                        JsonObjectBuilder jsonResp = Json.createObjectBuilder();
                        resp.setStatus(HttpServletResponse.SC_OK);
                        jsonResp.add("status",resp.getStatus());
                        jsonResp.add("message","Done");
                        jsonResp.add("data",respData.build());

                        writer.print(jsonResp.build());
                    }else{
                        JsonObjectBuilder jsonResp = Json.createObjectBuilder();
                        resp.setStatus(HttpServletResponse.SC_OK);
                        jsonResp.add("status",HttpServletResponse.SC_NOT_FOUND);
                        jsonResp.add("message",customerId+" is not an existing Customer ID");
                        jsonResp.add("data","");

                        writer.print(jsonResp.build());
                    }

                    break;

            }

            connection.close();

        }catch (SQLException e){
            e.printStackTrace();

            JsonObjectBuilder jsonError = Json.createObjectBuilder();
            resp.setStatus(HttpServletResponse.SC_OK);
            jsonError.add("status",HttpServletResponse.SC_BAD_REQUEST);
            jsonError.add("message","Error");
            jsonError.add("data",e.getLocalizedMessage());

            writer.print(jsonError.build());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();

        try {
            Connection connection = dataSource.getConnection();

            JsonReader reader = Json.createReader(req.getReader());
            JsonObject jsonReq = reader.readObject();
            JsonObject reqData = jsonReq.getJsonObject("data");

            CustomerDTO customerDTO = new CustomerDTO(
                    reqData.getString("id"),
                    reqData.getString("name"),
                    reqData.getString("address"),
                    reqData.getString("contact")
            );
            boolean isCustomerAdded = customerBO.addCustomer(connection, customerDTO);

            JsonObjectBuilder jsonResp = Json.createObjectBuilder();
            resp.setStatus(HttpServletResponse.SC_OK);

            if(isCustomerAdded) {
                jsonResp.add("status",resp.getStatus());
                jsonResp.add("message","Customer Added Successfully !!!");
            }else{
                jsonResp.add("status",HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                jsonResp.add("message","Customer Saving Failed !!!");
            }
            jsonResp.add("data","");
            writer.print(jsonResp.build());

            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();

            JsonObjectBuilder jsonError = Json.createObjectBuilder();
            resp.setStatus(HttpServletResponse.SC_OK);
            jsonError.add("status",HttpServletResponse.SC_BAD_REQUEST);
            jsonError.add("message","Error");
            jsonError.add("data",e.getLocalizedMessage());

            writer.print(jsonError.build());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();

        try {
            Connection connection = dataSource.getConnection();

            JsonReader reader = Json.createReader(req.getReader());
            JsonObject jsonReq = reader.readObject();
            JsonObject reqData = jsonReq.getJsonObject("data");

            CustomerDTO customerDTO = new CustomerDTO(
                    reqData.getString("id"),
                    reqData.getString("name"),
                    reqData.getString("address"),
                    reqData.getString("contact")
            );
            boolean isCustomerUpdated = customerBO.updateCustomer(connection, customerDTO);

            JsonObjectBuilder jsonResp = Json.createObjectBuilder();
            resp.setStatus(HttpServletResponse.SC_OK);

            if(isCustomerUpdated) {
                jsonResp.add("status",resp.getStatus());
                jsonResp.add("message","Customer Updated Successfully !!!");
            }else{
                jsonResp.add("status",HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                jsonResp.add("message","Customer Updating Failed !!!");
            }
            jsonResp.add("data","");
            writer.print(jsonResp.build());

            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();

            JsonObjectBuilder jsonError = Json.createObjectBuilder();
            resp.setStatus(HttpServletResponse.SC_OK);
            jsonError.add("status",HttpServletResponse.SC_BAD_REQUEST);
            jsonError.add("message","Error");
            jsonError.add("data",e.getLocalizedMessage());

            writer.print(jsonError.build());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Connection connection = dataSource.getConnection();

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
