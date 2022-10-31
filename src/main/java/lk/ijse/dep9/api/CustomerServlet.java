package lk.ijse.dep9.api;

import jakarta.annotation.Resource;
import jakarta.json.Json;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.dep9.dto.CustomerDTO;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

@WebServlet(name = "customerServlet", urlPatterns = "/customers")
public class CustomerServlet extends HttpServlet {

    @Resource(lookup = "java:/comp/env/jdbc/customer_db")
    private DataSource pool;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        System.out.println(pathInfo);
        if(pathInfo == null || pathInfo.equals("/")){
            getAllCustomers(resp);
        }
    }

    private void getAllCustomers(HttpServletResponse response){

        try {
            Connection connection = pool.getConnection();
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT * FROM Customer");
            while(rst.next()){
                String id = rst.getString("id");
                String name = rst.getString("name");
                String address = rst.getString("address");

                ArrayList<CustomerDTO> customerDTOS = new ArrayList<>();
                CustomerDTO customerD = new CustomerDTO(id, name, address);
                customerDTOS.add(customerD);
            }
            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
