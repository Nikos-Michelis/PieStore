package com.nick.jakartaproject.models.dao;

import com.nick.jakartaproject.form.FormOrder;
import com.nick.jakartaproject.models.domain.User;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDAO {
        public static class RecentOrderHistoryItem {
            private int orderId;
            private String stamp;
            private Map<String, Integer> orderItems = new HashMap<>();

            public RecentOrderHistoryItem() {
            }

            public RecentOrderHistoryItem(int orderId, String stamp, Map<String, Integer> orderItems) {
                this.orderId = orderId;
                this.stamp = stamp;
                this.orderItems = orderItems;
            }

            public int getOrderId() {
                return orderId;
            }

            public void setOrderId(int orderId) {
                this.orderId = orderId;
            }

            public String getStamp() {
                return stamp;
            }

            public void setStamp(String stamp) {
                this.stamp = stamp;
            }

            public Map<String, Integer> getOrderItems() {
                return orderItems;
            }

            public void setOrderItems(Map<String, Integer> orderItems) {
                this.orderItems = orderItems;
            }

            @Override
            public String toString() {
                return "RecentOrderHistoryItem{" +
                        "orderId=" + orderId +
                        ", stamp='" + stamp + '\'' +
                        ", orderItems=" + orderItems +
                        '}';
            }
        }
    public static void storeOrder(FormOrder formOrder, User user){
        try {
            DataSource ds = (DataSource) new InitialContext().lookup("java:/comp/env/jdbc/newpitosdb");
            Connection connection = ds.getConnection();


            String query = "INSERT INTO newpitosdb.orders (fullName, address, area_id, email, tel, comments, offer, payment, stamp, user_id) " +
                            "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, formOrder.getFullName());
            statement.setString(2, formOrder.getAddress());
            statement.setInt(3, formOrder.getAreaId());
            statement.setString(4, formOrder.getEmail());
            statement.setString(5, formOrder.getTel());
            statement.setString(6, formOrder.getComments());
            statement.setBoolean(7, formOrder.getOffer());
            statement.setString(8, formOrder.getPayment());
            statement.setTimestamp(9, java.sql.Timestamp.valueOf(formOrder.getTimestamp()));
            statement.setInt(10, user.getId());
            statement.executeUpdate();
            // get generated key
            ResultSet genKeys = statement.getGeneratedKeys();
            genKeys.next();
            int orderId = (int) genKeys.getLong(1);
            statement.close();
            genKeys.close();
            connection.close();

            for(var item: formOrder.getOrderItems()){
                item.setOrderId(orderId);
                OrderItemDAO.storeOrderItem(item);
            }


        } catch (NamingException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static List<RecentOrderHistoryItem> recentOrdersOfUser(User user, int n) {
        try {
            DataSource ds = (DataSource) new InitialContext().lookup("java:/comp/env/jdbc/newpitosdb");
            Connection connection = ds.getConnection();

            String query = " SELECT o.id as orderId, o.stamp as stamp, p.name as pie, oi.quantity as quantity" +
                            " FROM order_item oi" +
                            "    JOIN pie p ON p.id = oi.pie_id" +
                            "    JOIN orders o ON o.id = oi.order_id" +
                            "    JOIN user u ON u.id = o.user_id" +
                            " WHERE u.id = ?" +
                            " ORDER BY stamp DESC, p.id" +
                            " LIMIT ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, user.getId());
            statement.setInt(2, 4*n);
            ResultSet resultSet = statement.executeQuery();

            List<RecentOrderHistoryItem> orders = new ArrayList<>();
            LocalDateTime previous = null;
            while (resultSet.next()) {
                if (Integer.parseInt(resultSet.getString("quantity"))>0) {
                    LocalDateTime stamp = resultSet.getTimestamp("stamp").toLocalDateTime();
                    if (!stamp.equals(previous)) {
                        orders.add(new RecentOrderHistoryItem(resultSet.getInt("orderId"),
                                stamp.format(DateTimeFormatter.ofPattern("dd/MM/yyyy(HH:mm:ss)")),
                                new HashMap<>()
                        ));
                        orders.get(orders.size() - 1).getOrderItems().put(resultSet.getString("pie"), resultSet.getInt("quantity"));
                        previous = stamp;
                    } else {
                        orders.get(orders.size() - 1).getOrderItems().put(resultSet.getString("pie"), resultSet.getInt("quantity"));
                    }
                }
            }

            resultSet.close();
            statement.close();
            connection.close();
            return orders;
        } catch (SQLException | NamingException e) {
            throw new RuntimeException(e);
        }
    }
}
