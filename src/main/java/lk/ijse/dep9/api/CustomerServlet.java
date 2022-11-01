package lk.ijse.dep9.api;

import jakarta.annotation.Resource;
import jakarta.json.Json;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.dep9.api.util.HTTPServlet2;
import lk.ijse.dep9.dto.CustomerDTO;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(name = "customerServlet", urlPatterns = "/customers/*")
public class CustomerServlet extends HTTPServlet2 {

    @Resource(lookup = "java:/comp/env/jdbc/customer_db")
    private DataSource pool;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();


        System.out.println(pathInfo);
        if(pathInfo == null || pathInfo.equals("/")){

        }else{
            getCustomer(request,response,pathInfo);

        }
    }

    private void getCustomer(HttpServletRequest request, HttpServletResponse response,String path) throws IOException {

        try(Connection connection = pool.getConnection()) {
            if(path.matches("^/[A-Fa-f0-9]{8}-([A-Fa-f0-9]{4}-){3}[A-Fa-f0-9]{12}/?$")){
                PreparedStatement stm = connection.prepareStatement("SELECT * FROM Customer WHERE id=?");
                Pattern pattern = Pattern.compile("[A-Fa-f0-9]{8}(-[A-Fa-f0-9]{4}){3}-[A-Fa-f0-9]{12}");
                Matcher matcher = pattern.matcher(path);

                if(matcher.find()){
                    String group = matcher.group(0);
                    stm.setString(1,group);
                    ResultSet rst = stm.executeQuery();
                    if(rst.next()){
                        String id = rst.getString("id");
                        String name = rst.getString("name");
                        String address = rst.getString("address");
                        CustomerDTO customer = new CustomerDTO(id, name, address);


                        Jsonb jsonb = JsonbBuilder.create();
                        String jsonCustomer = jsonb.toJson(customer);

                        response.setContentType("application/json");
                        response.getWriter().println(jsonCustomer);

                    }
                    else{
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    }

                }
            }

            else{
                response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
            }


        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void getAllCustomers(HttpServletResponse response) throws IOException {
        try (Connection connection = pool.getConnection()){
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT * FROM Customer");
            ArrayList<CustomerDTO> customerArrayList = new ArrayList<>();
            while(rst.next()){
                String id = rst.getString("id");
                String name = rst.getString("name");
                String address = rst.getString("address");
                CustomerDTO customerD = new CustomerDTO(id, name, address);
                customerArrayList.add(customerD);
            }
            connection.close();
            Jsonb jsonb = JsonbBuilder.create();
            String json = jsonb.toJson(customerArrayList);
            response.setContentType("application/json");
            response.getWriter().println(json);
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().println("doPost");
    }

    @Override
    protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.getWriter().println("doPatch");
    }

}
