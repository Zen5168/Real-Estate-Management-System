package com.mycompany.realestatemanagementsystem;

import java.sql.*;
import java.util.*;

public class TransactionDAO {

    // GETS ALL TRANSACTIONS FROM THE DATABASE
    public ArrayList<Transaction> getAllTransactions() throws SQLException {
        ArrayList<Transaction> list = new ArrayList<>();
        String sql = "SELECT transactionId, propertyId, clientId, `date` FROM transactions ORDER BY transactionId";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement preparedStatement = conn.prepareStatement(sql); ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                list.add(new Transaction(
                        rs.getInt("transactionId"),
                        rs.getInt("propertyId"),
                        rs.getInt("clientId"),
                        rs.getDate("date")));
            }
        }

        return list;
    }

    // RECORDS A SALE: MARKS THE PROPERTY AS SOLD AND SAVES THE TRANSACTION
    // BOTH STEPS HAPPEN TOGETHER, SO IF ONE FAILS NOTHING IS SAVED
    // RETURNS FALSE IF THE PROPERTY IS NO LONGER AVAILABLE
    public boolean addTransaction(Transaction transaction) throws SQLException {
        String markSoldSql = "UPDATE properties SET status = 'SOLD' WHERE propertyId = ? AND status = 'AVAILABLE'";
        String insertSql = "INSERT INTO transactions (propertyId, clientId, `date`) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement markSold = conn.prepareStatement(markSoldSql); PreparedStatement insert = conn.prepareStatement(insertSql)) {

                markSold.setInt(1, transaction.getPropertyId());

                if (markSold.executeUpdate() == 0) { // PROPERTY IS ALREADY SOLD OR DOES NOT EXIST
                    conn.rollback();
                    return false;
                }

                insert.setInt(1, transaction.getPropertyId());
                insert.setInt(2, transaction.getClientId());
                insert.setDate(3, transaction.getDate());
                insert.executeUpdate();

                conn.commit();
                return true;

            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            }
        }
    }

    // CANCELS A SALE: DELETES THE TRANSACTION AND SETS THE PROPERTY BACK TO AVAILABLE
    public boolean deleteTransaction(int transactionId) throws SQLException {
        String findSql = "SELECT propertyId FROM transactions WHERE transactionId = ?";
        String deleteSql = "DELETE FROM transactions WHERE transactionId = ?";
        String markAvailableSql = "UPDATE properties SET status = 'AVAILABLE' WHERE propertyId = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement find = conn.prepareStatement(findSql); PreparedStatement delete = conn.prepareStatement(deleteSql); PreparedStatement markAvailable = conn.prepareStatement(markAvailableSql)) {

                // FIND WHICH PROPERTY THIS TRANSACTION BELONGS TO
                find.setInt(1, transactionId);
                int propertyId;

                try (ResultSet rs = find.executeQuery()) {
                    if (!rs.next()) { // TRANSACTION DOES NOT EXIST
                        conn.rollback();
                        return false;
                    }
                    propertyId = rs.getInt("propertyId");
                }

                delete.setInt(1, transactionId);
                delete.executeUpdate();

                markAvailable.setInt(1, propertyId);
                markAvailable.executeUpdate();

                conn.commit();
                return true;

            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            }
        }
    }
}
