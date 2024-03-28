package com.nick.jakartaproject.models.dao;

import com.nick.jakartaproject.models.domain.Pie;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PieDAO {

    private static Pie preparePieObject(ResultSet resultSet) throws SQLException{
        Pie pie = new Pie();
        pie.setId(resultSet.getInt("id"));
        pie.setPrice(resultSet.getInt("price"));
        pie.setName(resultSet.getString("name"));
        pie.setFileName(resultSet.getString("fileName"));
        pie.setIngredients(resultSet.getString("ingredients"));
        pie.setDescription(resultSet.getString("description"));
        return pie;
    }
    public static Pie getPieById(int id){
        try {
            DataSource ds = (DataSource) new InitialContext().lookup("java:/comp/env/jdbc/newpitosdb");
            Connection connection = ds.getConnection();


            String query = "SELECT * FROM pie WHERE id=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            Pie pie = null;
            if(resultSet.next()){
                pie = preparePieObject(resultSet);
            }

            resultSet.close();
            statement.close();
            connection.close();
            System.out.println(pie);
            return pie;
        } catch (NamingException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Pie> getPies(){
        try {
            DataSource ds = (DataSource) new InitialContext().lookup("java:/comp/env/jdbc/newpitosdb");
            Connection connection = ds.getConnection();

            String query = "SELECT * FROM pie";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            List<Pie> piesList = new ArrayList<>();
            while (resultSet.next()) {
                Pie pie = preparePieObject(resultSet);
                piesList.add(pie);
            }

            resultSet.close();
            statement.close();
            connection.close();
        return piesList;
        } catch (NamingException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
