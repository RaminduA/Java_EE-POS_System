package controller.servlet;

import bo.BOFactory;
import bo.custom.CustomerBO;
import dto.CustomerDTO;

import javax.annotation.Resource;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
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

                    break;

                case "SEARCH":
                    JsonObject reqData = jsonReq.getJsonObject("data");
                    String customerId = reqData.getString("customerId");
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Connection connection = dataSource.getConnection();

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Connection connection = dataSource.getConnection();

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
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
