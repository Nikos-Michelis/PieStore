package com.nick.jakartaproject.models.dao;

import com.nick.jakartaproject.form.FormRegister;
import com.nick.jakartaproject.crypto.Crypto;
import com.nick.jakartaproject.models.domain.User;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UserDAO {
    private static User prepareUserObject(ResultSet resultSet) throws SQLException{
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setUserName(resultSet.getString("userName"));
        user.setPassword(resultSet.getString("password"));
        user.setFullName(resultSet.getString("fullName"));
        user.setEmail(resultSet.getString("email"));
        user.setTel(resultSet.getString("tel"));
        user.setStatus(resultSet.getString("status"));
        user.setCode(resultSet.getString("code"));
        return user;
    }
    public static int storeUserUnverified(FormRegister formData){
        try {
            DataSource ds = (DataSource) new InitialContext().lookup("java:/comp/env/jdbc/newpitosdb");
            Connection connection = ds.getConnection();

            String salt = Crypto.salt();
            String hashedPassword = Crypto.hash(formData.getPassword(), salt);

            Random random = new Random();
            String code = String.valueOf(random.nextInt(100000));
            String query = "INSERT INTO user (userName, password, fullName, email, tel, status, code, salt) " +
                    "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, formData.getUserName());
            statement.setString(2, hashedPassword);
            statement.setString(3, formData.getFullName());
            statement.setString(4, formData.getEmail());
            statement.setString(5, formData.getTel());
            statement.setString(6, "unverified");
            statement.setString(7, code);
            statement.setString(8, salt);
            statement.executeUpdate();
            //get generated key
            ResultSet genKeys = statement.getGeneratedKeys();
            genKeys.next();
            int userId = (int) genKeys.getLong(1);
            statement.close();
            genKeys.close();
            connection.close();

            storeUserRole(userId);

            return userId;
        } catch (NamingException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private static void storeUserRole(int userId){
        try {
            DataSource ds = (DataSource) new InitialContext().lookup("java:/comp/env/jdbc/newpitosdb");
            Connection connection = ds.getConnection();

            String userRoleQuery = "INSERT INTO user_role (user_id, role_id) " +
                    "VALUES(?, 1)";
            PreparedStatement statement = connection.prepareStatement(userRoleQuery);
            statement.setInt(1, userId);
            statement.executeUpdate();
            statement.close();
            connection.close();
        } catch (NamingException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static User getUserById(int id){
        DataSource ds = null;
        try {
            ds = (DataSource) new InitialContext().lookup("java:/comp/env/jdbc/newpitosdb");
            Connection connection = ds.getConnection();

            String query = "SELECT * FROM user WHERE id=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            User user = prepareUserObject(resultSet);

            resultSet.close();
            statement.close();
            connection.close();
            System.out.println(user);
            return user;
        } catch (NamingException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static boolean isVerifedUser(String code){
        DataSource ds = null;
        try {
            ds = (DataSource) new InitialContext().lookup("java:/comp/env/jdbc/newpitosdb");
            Connection connection = ds.getConnection();

            String query = "SELECT * FROM user WHERE code=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, code);
            ResultSet resultSet = statement.executeQuery();

            boolean result = false;
            if(resultSet.next()){
                User user = prepareUserObject(resultSet);
                if(user.getStatus().equals("unverified")){
                    String upQuery = "UPDATE user SET status='verified' WHERE id = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(upQuery);
                    preparedStatement.setInt(1, user.getId());
                    preparedStatement.executeUpdate();
                    result = true;
                    preparedStatement.close();
                }
            }
            resultSet.close();
            statement.close();
            connection.close();
            return result;
        } catch (NamingException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static User getUserByUserName(String userName){
        DataSource ds = null;
        try {
            ds = (DataSource) new InitialContext().lookup("java:/comp/env/jdbc/newpitosdb");
            Connection connection = ds.getConnection();

            String query = "SELECT * FROM user WHERE userName=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, userName);
            ResultSet resultSet = statement.executeQuery();
            User user = null;
            if(resultSet.next()) {
                user = prepareUserObject(resultSet);
            }

            resultSet.close();
            statement.close();
            connection.close();
            System.out.println(user);
            return user;
        } catch (NamingException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static User getUserByEmail(String email) {
        try {
            DataSource ds = (DataSource) new InitialContext().lookup("java:/comp/env/jdbc/newpitosdb");
            Connection connection = ds.getConnection();

            String query = "SELECT * FROM user WHERE email=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            User user = null;
            if (resultSet.next())
                user = prepareUserObject(resultSet);

            resultSet.close();
            statement.close();
            connection.close();
            System.out.println(user);
            return user;
        } catch (SQLException | NamingException e) {
            throw new RuntimeException(e);
        }
    }
    // return user if session exists
    public static User getUserBySession(String session) {
        try {
            DataSource ds = (DataSource) new InitialContext().lookup("java:/comp/env/jdbc/newpitosdb");
            Connection connection = ds.getConnection();

            String query = "SELECT * FROM user WHERE session=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, session);
            ResultSet resultSet = statement.executeQuery();
            // user id >0 when object has created or id < 0 when user is not created
            User user = null;
            if (resultSet.next()) {
                user = prepareUserObject(resultSet);
            }

            resultSet.close();
            statement.close();
            connection.close();
            System.out.println("session: " + user);
            return user;
        } catch (SQLException | NamingException e) {
            throw new RuntimeException(e);
        }
    }
    public static List<Integer> getAdminStats(){
        try {
            DataSource ds = (DataSource) new InitialContext().lookup("java:/comp/env/jdbc/newpitosdb");
            Connection connection = ds.getConnection();
            String query = "SELECT COUNT(*) AS cnt FROM user WHERE status = 'unverified'";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int counterUnverified = resultSet.getInt("cnt");
            resultSet.close();
            statement.close();

            query = "SELECT COUNT(*) AS cnt FROM user WHERE status='verified' AND code IS NOT NULL";
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            resultSet.next();
            int counterVerifiedCodes = resultSet.getInt("cnt");
            resultSet.close();
            statement.close();

            List<Integer> adminStats = new ArrayList<>();
            adminStats.add(counterUnverified);
            adminStats.add(counterVerifiedCodes);

            connection.close();

            return adminStats;
        } catch (NamingException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void deleteUnverifiedUsers(){
        try {
            DataSource ds = (DataSource) new InitialContext().lookup("java:/comp/env/jdbc/newpitosdb");
            Connection connection = ds.getConnection();

            String query = "DELETE FROM user WHERE status = 'unverified'";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.executeUpdate();
            statement.close();
            connection.close();

        } catch (NamingException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void updateVerifiedUsersCode(){
        try {
            DataSource ds = (DataSource) new InitialContext().lookup("java:/comp/env/jdbc/newpitosdb");
            Connection connection = ds.getConnection();

            String query = "UPDATE user SET code=NULL WHERE status = 'verified' AND code IS NOT NULL";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.executeUpdate();

            statement.close();
            connection.close();

        } catch (NamingException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static String getSalt(String userName){
        try {
            DataSource ds = (DataSource) new InitialContext().lookup("java:/comp/env/jdbc/newpitosdb");
            Connection connection = ds.getConnection();

            String query = "SELECT salt FROM user WHERE userName=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, userName);
            ResultSet resultSet = statement.executeQuery();
            String salt = null;
            if(resultSet.next()) {
                salt = resultSet.getString("salt");
            }

            resultSet.close();
            statement.close();
            connection.close();
            System.out.println(salt);
            return salt;
        } catch (NamingException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static User userLogin(String userName, String password){
        try {
            DataSource ds = (DataSource) new InitialContext().lookup("java:/comp/env/jdbc/newpitosdb");
            Connection connection = ds.getConnection();
            // prepare password (salt + hash)
            String salt = getSalt(userName);
            String hashedPassword = Crypto.hash(password, salt);
            // get stored username + password
            String query = "SELECT * FROM user WHERE userName=? AND password=? AND status='verified'";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, userName);
            // check if hashedPassword is equal with hashed password in database
            statement.setString(2, hashedPassword);
            ResultSet resultSet = statement.executeQuery();
            User user = null;
            if(resultSet.next()) {
                user = prepareUserObject(resultSet);
            }

            resultSet.close();
            statement.close();
            connection.close();
            System.out.println(user);
            return user;
        } catch (NamingException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void updateUsersResetCode(String email, int code){
        try {
            DataSource ds = (DataSource) new InitialContext().lookup("java:/comp/env/jdbc/newpitosdb");
            Connection connection = ds.getConnection();

            String query = "UPDATE user SET code= ?, status='reset' WHERE email= ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, code);
            statement.setString(2, email);
            statement.executeUpdate();

            statement.close();
            connection.close();

        } catch (NamingException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void updateUsersNewPassword(String password, String code){
        try {
            DataSource ds = (DataSource) new InitialContext().lookup("java:/comp/env/jdbc/newpitosdb");
            Connection connection = ds.getConnection();
            String salt = Crypto.salt();
            String hashedPassword = Crypto.hash(password, salt);

            String query = "UPDATE user SET password= ?, code=NULL, status='verified' WHERE code= ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, hashedPassword);
            statement.setString(2, code);
            statement.executeUpdate();

            statement.close();
            connection.close();

        } catch (NamingException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void storeSession(String userName, String session){
        try {
            DataSource ds = (DataSource) new InitialContext().lookup("java:/comp/env/jdbc/newpitosdb");
            Connection connection = ds.getConnection();
            String query = "UPDATE user SET session= ? WHERE userName= ?";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, session);
            statement.setString(2, userName);
            statement.executeUpdate();
            connection.close();
        } catch (NamingException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static String getRole(String session) {
        try {
            DataSource ds = (DataSource) new InitialContext().lookup("java:/comp/env/jdbc/newpitosdb");
            Connection connection = ds.getConnection();

            String query = " SELECT role.description AS role FROM user  " +
                            " JOIN user_role ON user.id = user_role.user_id" +
                            " JOIN role ON user_role.role_id = role.role_id" +
                            " WHERE user.session = ? ";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, session);
            ResultSet resultSet = statement.executeQuery();

            String role = null;
            if (resultSet.next()) {
                role = resultSet.getString("role");
            }

            resultSet.close();
            statement.close();
            connection.close();
            System.out.println(role);
            return role;
        } catch (SQLException | NamingException e) {
            throw new RuntimeException(e);
        }
    }
    public static boolean logout(String session) {
        try {
            DataSource ds = (DataSource) new InitialContext().lookup("java:/comp/env/jdbc/newpitosdb");
            Connection connection = ds.getConnection();

            String selectQuery = "SELECT userName FROM user WHERE session = ?";
            PreparedStatement statement = connection.prepareStatement(selectQuery);
            statement.setString(1, session);
            ResultSet resultSet = statement.executeQuery();

            String userName = null;
            if(resultSet.next()){
                userName = resultSet.getString("userName");
            }
            resultSet.close();
            statement.close();
            if(userName != null) {
                String deleteQuery = " UPDATE user SET session = NULL WHERE userName= ?";
                statement = connection.prepareStatement(deleteQuery);
                statement.setString(1, userName);
                statement.executeUpdate();
                statement.close();
            }
            connection.close();
            return userName != null;
        } catch (SQLException | NamingException e) {
            throw new RuntimeException(e);
        }
    }
}
