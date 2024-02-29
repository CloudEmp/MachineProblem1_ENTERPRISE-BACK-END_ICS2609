/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package crud_machineprob1;

/**
 *
 * @author Blade Emperor
 */
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.*;

public class DatabaseManager {

    private static final String DRIVER = "org.apache.derby.jdbc.ClientDriver";
    private static final String DBURL = "jdbc:derby://localhost:1527/LoginDB";
    private static final String DBUSERNAME = "app";
    private static final String DBPASSWORD = "app";
    private Connection con;
    private String query;
    private PreparedStatement ps;
    private ResultSet rs;

    public DatabaseManager() {
        try {
            //Establish Connection
            con = DriverManager.getConnection(DBURL, DBUSERNAME, DBPASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            if (rs != null) {
                rs.close();
            }

            if (ps != null) {
                ps.close();
            }

            con.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    public User loginAuthenticate(String username, String password) throws SQLException {
        query = "SELECT * FROM USERS WHERE Email = ? AND Password = ?";
        ps = con.prepareStatement(query);
        ps.setString(1, username);
        ps.setString(2, password);
        rs = ps.executeQuery();

        while (rs.next()) {
            User currentUser = new User();

            currentUser.setEmail(rs.getString("Email").trim());
            currentUser.setPassword(rs.getString("Password").trim());
            currentUser.setUserRole(rs.getString("UserRole").trim());

            return currentUser;
        }
        return null;
    }

    public void deleteUser(String email) throws SQLException {
        query = "DELETE FROM USERS WHERE Email = ?";
        ps = con.prepareStatement(query);
        ps.setString(1, email);
        ps.executeUpdate();
    }

    public void addUser(String email, String password, String userRole) throws SQLException {
        query = "INSERT INTO USERS (Email, Password, UserRole) VALUES (?,?,?)";
        ps = con.prepareStatement(query);
        ps.setString(1, email);
        ps.setString(2, password);
        ps.setString(3, userRole);
        ps.executeUpdate();

    }

    public void updateUser(String email, String password, String userRole) throws SQLException {
        query = "UPDATE USERS SET Password =?, userRole=? WHERE Email = ?";
        ps = con.prepareStatement(query);
        ps.setString(1, password);
        ps.setString(2, userRole);
        ps.setString(3, email);
        ps.executeUpdate();

    }

    public void updateRoleUser(String email, String userRole) throws SQLException {
        query = "UPDATE USERS SET userRole=? WHERE Email=?";
        ps = con.prepareStatement(query);
        ps.setString(1, userRole);
        ps.setString(2, email);
        ps.executeUpdate();
    }

    public void updatePasswordUser(String email, String password) throws SQLException {
        query = "UPDATE USERS SET Password=? WHERE Email=?";
        ps = con.prepareStatement(query);
        ps.setString(1, password);
        ps.setString(2, email);
        ps.executeUpdate();
    }

    public boolean isUserDuplicate(String email) throws SQLException {
        query = "SELECT * FROM USERS WHERE Email =?";
        ps = con.prepareStatement(query);
        ps.setString(1, email);
        rs = ps.executeQuery();

        return rs.next();
    }

    public List<String[]> getUsersData() throws SQLException {
        List<String[]> dataList = new ArrayList<>();
        query = "SELECT Email, Password, UserRole FROM USERS ORDER BY Email";
        ps = con.prepareStatement(query);
        rs = ps.executeQuery();

        while (rs.next()) {
            String[] rowData = new String[3];
            rowData[0] = rs.getString("Email").trim();
            rowData[1] = rs.getString("Password").trim();
            rowData[2] = rs.getString("UserRole").trim();
            dataList.add(rowData);
        }
        return dataList;
    }

}
