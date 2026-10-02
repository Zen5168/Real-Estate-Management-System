package com.mycompany.realestatemanagementsystem;
 
import java.sql.*;
 
public class DBConnection {
 
    // DATABASE INFO
    private static final String DB_URL = "jdbc:mysql://localhost:3306/real_estate";
    private static final String DB_USER = "root";
    private static final String DB_PASS = ""; // EMPTY PASSWORD
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }
}