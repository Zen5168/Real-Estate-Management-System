package com.mycompany.realestatemanagementsystem;

import java.sql.*;

public class DBConnection {

    // DATABASE INFO
    private static final String DB_SERVER_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_URL = DB_SERVER_URL + "real_estate";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    private static boolean initialized = false;

    public static Connection getConnection() throws SQLException {

        // SETS UP THE DATABASE THE FIRST TIME ANYTHING ASKS FOR A CONNECTION
        if (!initialized) {
            initializeDatabase();
        }

        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    // CREATES THE DATABASE, THE TABLES, AND THE ADMIN USER IF THEY DO NOT EXIST YET
    public static void initializeDatabase() throws SQLException {

        // CONNECTS TO THE SERVER FIRST, BECAUSE THE DATABASE MAY NOT EXIST YET
        try (Connection conn = DriverManager.getConnection(DB_SERVER_URL, DB_USER, DB_PASS); Statement statement = conn.createStatement()) {

            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS real_estate");
            statement.executeUpdate("USE real_estate");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS users ("
                    + "userId INT AUTO_INCREMENT PRIMARY KEY, "
                    + "username VARCHAR(50) NOT NULL UNIQUE, "
                    + "password VARCHAR(255) NOT NULL)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS properties ("
                    + "propertyId INT AUTO_INCREMENT PRIMARY KEY, "
                    + "location VARCHAR(150) NOT NULL, "
                    + "price DOUBLE NOT NULL, "
                    + "status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE')");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS clients ("
                    + "clientId INT AUTO_INCREMENT PRIMARY KEY, "
                    + "name VARCHAR(100) NOT NULL, "
                    + "contactInfo VARCHAR(100))");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS transactions ("
                    + "transactionId INT AUTO_INCREMENT PRIMARY KEY, "
                    + "propertyId INT NOT NULL, "
                    + "clientId INT NOT NULL, "
                    + "`date` DATE NOT NULL, "
                    + "FOREIGN KEY (propertyId) REFERENCES properties(propertyId), "
                    + "FOREIGN KEY (clientId) REFERENCES clients(clientId))");

            // THE ADMIN IS THE ONLY STARTING DATA (PASSWORD IS STORED AS A SHA-256 HASH)
            // USERNAME = admin
            // PASSWORD = admin
            statement.executeUpdate("INSERT IGNORE INTO users (username, password) VALUES ('admin', "
                    + "'8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918')");

            initialized = true; // SO THE SET UP ONLY RUNS ONCE PER RUN
        }
    }
}