package com.nick.jakartaproject.models.dao;

import com.nick.jakartaproject.models.domain.Area;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AreaDAO {

    private static Area prepareAreaObject(ResultSet resultSet) throws SQLException {
        Area area = new Area();
        area.setId(resultSet.getInt("id"));
        area.setDescription(resultSet.getString("description"));
        return area;
    }
    public static Area getAreaById(int id){
        DataSource ds = null;
        try {
            ds = (DataSource) new InitialContext().lookup("java:/comp/env/jdbc/newpitosdb");
            Connection connection = ds.getConnection();


            String query = "SELECT * FROM area WHERE id=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            Area area = prepareAreaObject(resultSet);

            resultSet.close();
            statement.close();
            connection.close();
            System.out.println(area);
            return area;
        } catch (NamingException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Area> getAreas(){
        try {
            DataSource ds = (DataSource) new InitialContext().lookup("java:/comp/env/jdbc/newpitosdb");
            Connection connection = ds.getConnection();

            String query = "SELECT * FROM area";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            List<Area> areasList = new ArrayList<>();
            while (resultSet.next()) {
                Area area = prepareAreaObject(resultSet);
                areasList.add(area);
            }

            resultSet.close();
            statement.close();
            connection.close();
            return areasList;
        } catch (NamingException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
