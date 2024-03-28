package com.nick.jakartaproject.models.dao;

import com.nick.jakartaproject.models.domain.OrderItem;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;

public class OrderItemDAO {

    public static void storeOrderItem(OrderItem orderItem){
        try {
            DataSource ds = (DataSource) new InitialContext().lookup("java:/comp/env/jdbc/newpitosdb");
            Connection connection = ds.getConnection();
            System.out.println("- OrderItem List: " + orderItem);

            String query = "INSERT INTO order_item(order_id, pie_id, quantity) VALUES(?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            System.out.println("get orderid: " + orderItem.getOrderId() );
            statement.setInt(1, orderItem.getOrderId());
            System.out.println("get pieId: " + orderItem.getPieId());
            statement.setInt(2, orderItem.getPieId());
            System.out.println("quantity: " + orderItem.getQuantity());
            statement.setInt(3, orderItem.getQuantity());
            statement.executeUpdate();

            statement.close();
            connection.close();

        } catch (NamingException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
