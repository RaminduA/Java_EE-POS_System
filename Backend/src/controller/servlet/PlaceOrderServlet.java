package controller.servlet;

import bo.BOFactory;
import bo.custom.PlaceOrderBO;
import dto.CustomerDTO;
import dto.ItemDTO;
import dto.OrderDTO;
import dto.OrderDetailDTO;

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

@WebServlet(urlPatterns = "/place-order")
public class PlaceOrderServlet extends HttpServlet {
    PlaceOrderBO placeOrderBO = (PlaceOrderBO)BOFactory.getInstance().getBO(BOFactory.BOType.PLACEORDER);

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

                case "GET-ALL-CUSTOMER-IDS":
                    writer.print(getAllCustomerIds(resp,connection));
                    break;

                case "GET-ALL-ITEM-CODES":
                    writer.print(getAllItemCodes(resp,connection));
                    break;

                case "GET-ORDER-ID":
                    writer.print(getOrderId(resp,connection));
                    break;

                case "GET-CUSTOMER":
                    writer.print(getCustomer(resp,connection,jsonReq.getJsonObject("data")));
                    break;

                case "GET-ITEM":

                    break;
            }

            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
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

            ArrayList<OrderDetailDTO> detailDTOs = new ArrayList<>();
            JsonArray detailList = reqData.getJsonArray("detail-list");

            for (JsonValue jsonValue : detailList) {
                JsonObject jsonDetail = jsonValue.asJsonObject();
                detailDTOs.add(new OrderDetailDTO(
                        jsonDetail.getString("order-id"),
                        jsonDetail.getString("item-code"),
                        Double.parseDouble(jsonDetail.get("unit-price").toString()),
                        jsonDetail.getInt("quantity"),
                        Double.parseDouble(jsonDetail.get("price").toString())
                ));
            }

            OrderDTO orderDTO = new OrderDTO(
                    reqData.getString("order-id"),
                    reqData.getString("customer-id"),
                    reqData.getString("date"),
                    reqData.getString("time"),
                    Double.parseDouble(reqData.get("cost").toString()),
                    detailDTOs
            );
            boolean isOrderPlaced = placeOrderBO.placeOrder(connection, orderDTO);

            JsonObjectBuilder jsonResp = Json.createObjectBuilder();
            resp.setStatus(HttpServletResponse.SC_OK);

            if(isOrderPlaced) {
                jsonResp.add("status",resp.getStatus());
                jsonResp.add("message","Order Placed Successfully !!!");
            } else{
                jsonResp.add("status",HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                jsonResp.add("message","Placing Order Failed !!!");
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

    private JsonObject getAllCustomerIds(HttpServletResponse resp, Connection connection) {
        ArrayList<String> allCustomerIds = placeOrderBO.getAllCustomerIds(connection);
        JsonArrayBuilder respData = Json.createArrayBuilder();

        for (String customerId : allCustomerIds) {
            JsonObjectBuilder jsonCID = Json.createObjectBuilder();
            jsonCID.add("id",customerId);

            respData.add(jsonCID.build());
        }

        JsonObjectBuilder jsonResp = Json.createObjectBuilder();
        resp.setStatus(HttpServletResponse.SC_OK);
        jsonResp.add("status",resp.getStatus());
        jsonResp.add("message","Done");
        jsonResp.add("data",respData.build());

        return jsonResp.build();
    }

    private JsonObject getAllItemCodes(HttpServletResponse resp, Connection connection) {
        ArrayList<String> allItemCodes = placeOrderBO.getAllItemCodes(connection);
        JsonArrayBuilder respData = Json.createArrayBuilder();

        for (String itemCode : allItemCodes) {
            JsonObjectBuilder jsonIC = Json.createObjectBuilder();
            jsonIC.add("code",itemCode);

            respData.add(jsonIC.build());
        }

        JsonObjectBuilder jsonResp = Json.createObjectBuilder();
        resp.setStatus(HttpServletResponse.SC_OK);
        jsonResp.add("status",resp.getStatus());
        jsonResp.add("message","Done");
        jsonResp.add("data",respData.build());

        return jsonResp.build();
    }

    private JsonObject getOrderId(HttpServletResponse resp, Connection connection) {
        String orderId = placeOrderBO.getOrderId(connection);
        JsonObjectBuilder respData = Json.createObjectBuilder();
        respData.add("id",orderId);

        JsonObjectBuilder jsonResp = Json.createObjectBuilder();
        resp.setStatus(HttpServletResponse.SC_OK);
        jsonResp.add("status",resp.getStatus());
        jsonResp.add("message","Done");
        jsonResp.add("data",respData.build());

        return jsonResp.build();
    }

    private JsonObject getCustomer(HttpServletResponse resp, Connection connection, JsonObject reqData) {
        String customerId = reqData.getString("id");
        CustomerDTO customerDTO = placeOrderBO.getCustomer(connection, customerId);

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

        return jsonResp.build();
    }
}
