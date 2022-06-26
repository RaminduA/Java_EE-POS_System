package controller.servlet;

import bo.BOFactory;
import bo.custom.ItemBO;
import dto.ItemDTO;

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
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

@WebServlet(urlPatterns = "/item")
public class ItemServlet extends HttpServlet {
    ItemBO itemBO = (ItemBO)BOFactory.getInstance().getBO(BOFactory.BOType.ITEM);

    @Resource(name = "java:comp/env/jdbc/pool")
    DataSource dataSource;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();

        try {
            Connection connection = dataSource.getConnection();

            DecimalFormat df = new DecimalFormat("0.00");
            df.setRoundingMode(RoundingMode.DOWN);

            String option = req.getParameter("option");

            switch (option){

                case "GET-ALL":
                    ArrayList<ItemDTO> allItems = itemBO.getAllItems(connection);
                    JsonArrayBuilder responseData = Json.createArrayBuilder();

                    for (ItemDTO item : allItems) {
                        JsonObjectBuilder jsonItem = Json.createObjectBuilder();
                        jsonItem.add("code",item.getItemCode());
                        jsonItem.add("name",item.getName());
                        jsonItem.add("unit_price",df.format(item.getUnitPrice()));
                        jsonItem.add("quantity",item.getQtyOnHand());

                        responseData.add(jsonItem.build());
                    }

                    JsonObjectBuilder jsonResponse = Json.createObjectBuilder();
                    resp.setStatus(HttpServletResponse.SC_OK);
                    jsonResponse.add("status",resp.getStatus());
                    jsonResponse.add("message","Done");
                    jsonResponse.add("data",responseData.build());

                    writer.print(jsonResponse.build());

                    break;

                case "SEARCH":
                    String itemCode = req.getParameter("code");
                    ItemDTO itemDTO = itemBO.getItem(connection,itemCode);

                    if (itemDTO!=null) {
                        JsonObjectBuilder respData = Json.createObjectBuilder();
                        respData.add("code",itemDTO.getItemCode());
                        respData.add("name",itemDTO.getName());
                        respData.add("unit_price",df.format(itemDTO.getUnitPrice()));
                        respData.add("quantity",itemDTO.getQtyOnHand());

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
                        jsonResp.add("message",itemCode+" is not an existing Item Code");
                        jsonResp.add("data","");

                        writer.print(jsonResp.build());
                    }

                    break;

            }

            connection.close();

        }catch (SQLException e) {
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

            ItemDTO itemDTO = new ItemDTO(
                    reqData.getString("code"),
                    reqData.getString("name"),
                    Double.parseDouble(reqData.getString("unit_price")),
                    Integer.parseInt(reqData.getString("quantity"))
            );
            boolean isItemAdded = itemBO.addItem(connection, itemDTO);

            JsonObjectBuilder jsonResp = Json.createObjectBuilder();
            resp.setStatus(HttpServletResponse.SC_OK);

            if(isItemAdded) {
                jsonResp.add("status",resp.getStatus());
                jsonResp.add("message","Item Added Successfully !!!");
            } else{
                jsonResp.add("status",HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                jsonResp.add("message","Item Saving Failed !!!");
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

            ItemDTO itemDTO = new ItemDTO(
                    reqData.getString("code"),
                    reqData.getString("name"),
                    Double.parseDouble(reqData.getString("unit_price")),
                    Integer.parseInt(reqData.getString("quantity"))
            );
            boolean isItemUpdated = itemBO.updateItem(connection, itemDTO);

            JsonObjectBuilder jsonResp = Json.createObjectBuilder();
            resp.setStatus(HttpServletResponse.SC_OK);

            if(isItemUpdated) {
                jsonResp.add("status",resp.getStatus());
                jsonResp.add("message","Item Updated Successfully !!!");
            } else{
                jsonResp.add("status",HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                jsonResp.add("message","Item Updating Failed !!!");
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
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();

        try {
            Connection connection = dataSource.getConnection();

            JsonReader reader = Json.createReader(req.getReader());
            JsonObject jsonReq = reader.readObject();
            JsonObject reqData = jsonReq.getJsonObject("data");

            String itemCode = reqData.getString("code");
            boolean isItemDeleted = itemBO.deleteItem(connection, itemCode);

            JsonObjectBuilder jsonResp = Json.createObjectBuilder();
            resp.setStatus(HttpServletResponse.SC_OK);

            if(isItemDeleted) {
                jsonResp.add("status",resp.getStatus());
                jsonResp.add("message","Item Deleted Successfully !!!");
            } else{
                jsonResp.add("status",HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                jsonResp.add("message","Item Deleting Failed !!!");
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
}
