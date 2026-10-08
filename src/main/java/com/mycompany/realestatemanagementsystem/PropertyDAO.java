package com.mycompany.realestatemanagementsystem;

import java.sql.*;
import java.util.ArrayList;

public class PropertyDAO {

    // GETS ALL PROPERTIES FROM THE DATABASE
    public ArrayList<Property> getAllProperties() throws SQLException {
        ArrayList<Property> list = new ArrayList<>();
        String sql = "SELECT propertyId, location, price, status FROM properties ORDER BY propertyId";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement preparedStatement = conn.prepareStatement(sql); ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                list.add(new Property(
                        rs.getInt("propertyId"),
                        rs.getString("location"),
                        rs.getDouble("price"),
                        rs.getString("status")));
            }
        }

        return list;
    }

    // ADDS A NEW PROPERTY
    public boolean addProperty(Property property) throws SQLException {
        String sql = "INSERT INTO properties (location, price, status) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setString(1, property.getLocation());
            preparedStatement.setDouble(2, property.getPrice());
            preparedStatement.setString(3, property.getStatus());

            return preparedStatement.executeUpdate() > 0;
        }
    }

    // UPDATES AN EXISTING PROPERTY
    public boolean updateProperty(Property property) throws SQLException {
        String sql = "UPDATE properties SET location = ?, price = ?, status = ? WHERE propertyId = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setString(1, property.getLocation());
            preparedStatement.setDouble(2, property.getPrice());
            preparedStatement.setString(3, property.getStatus());
            preparedStatement.setInt(4, property.getPropertyId());

            return preparedStatement.executeUpdate() > 0;
        }
    }

    // DELETES A PROPERTY (FAILS IF THE PROPERTY ALREADY HAS TRANSACTIONS)
    public boolean deleteProperty(int propertyId) throws SQLException {
        String sql = "DELETE FROM properties WHERE propertyId = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setInt(1, propertyId);

            return preparedStatement.executeUpdate() > 0;
        }
    }
}