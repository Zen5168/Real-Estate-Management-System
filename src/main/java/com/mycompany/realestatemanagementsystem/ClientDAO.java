package com.mycompany.realestatemanagementsystem;

import java.sql.*;
import java.util.*;

public class ClientDAO {

    // GETS ALL CLIENTS AND RETURNS THEM IN A HASH MAP (KEY = clientId, VALUE = Client)
    public HashMap<Integer, Client> getAllClients() throws SQLException {
        HashMap<Integer, Client> map = new HashMap<>();
        String sql = "SELECT clientId, name, contactInfo FROM clients";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement preparedStatement = conn.prepareStatement(sql); ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                Client client = new Client(
                        rs.getInt("clientId"),
                        rs.getString("name"),
                        rs.getString("contactInfo"));
                map.put(client.getClientId(), client);
            }
        }

        loadPropertiesOwned(map);
        return map;
    }

    // FILLS EACH CLIENT'S propertiesOwned LIST FROM THE TRANSACTIONS TABLE
    private void loadPropertiesOwned(HashMap<Integer, Client> map) throws SQLException {
        String sql = "SELECT t.clientId, p.propertyId, p.location, p.price, p.status "
                + "FROM transactions t JOIN properties p ON t.propertyId = p.propertyId";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement preparedStatement = conn.prepareStatement(sql); ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                Client client = map.get(rs.getInt("clientId")); // HASH MAP LOOKUP
                if (client != null) {
                    client.addProperty(new Property(
                            rs.getInt("propertyId"),
                            rs.getString("location"),
                            rs.getDouble("price"),
                            rs.getString("status")));
                }
            }
        }
    }

    // ADDS A NEW CLIENT
    public boolean addClient(Client client) throws SQLException {
        String sql = "INSERT INTO clients (name, contactInfo) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setString(1, client.getName());
            preparedStatement.setString(2, client.getContactInfo());

            return preparedStatement.executeUpdate() > 0;
        }
    }

    // UPDATES AN EXISTING CLIENT
    public boolean updateClient(Client client) throws SQLException {
        String sql = "UPDATE clients SET name = ?, contactInfo = ? WHERE clientId = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setString(1, client.getName());
            preparedStatement.setString(2, client.getContactInfo());
            preparedStatement.setInt(3, client.getClientId());

            return preparedStatement.executeUpdate() > 0;
        }
    }

    // DELETES A CLIENT (FAILS IF THE CLIENT ALREADY HAS TRANSACTIONS)
    public boolean deleteClient(int clientId) throws SQLException {
        String sql = "DELETE FROM clients WHERE clientId = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setInt(1, clientId);

            return preparedStatement.executeUpdate() > 0;
        }
    }
}