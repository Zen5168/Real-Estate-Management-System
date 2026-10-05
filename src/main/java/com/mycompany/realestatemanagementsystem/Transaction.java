package com.mycompany.realestatemanagementsystem;

import java.sql.Date;

public class Transaction {

    private int transactionId;
    private int propertyId;
    private int clientId;
    private Date date;

    // CONSTRUCTOR FOR A TRANSACTION LOADED FROM THE DATABASE
    public Transaction(int transactionId, int propertyId, int clientId, Date date) {
        this.transactionId = transactionId;
        this.propertyId = propertyId;
        this.clientId = clientId;
        this.date = date;
    }

    // CONSTRUCTOR FOR A NEW TRANSACTION
    public Transaction(int propertyId, int clientId, Date date) {
        this(0, propertyId, clientId, date);
    }

    // GETTERS
    public int getTransactionId() {
        return transactionId;
    }

    public int getPropertyId() {
        return propertyId;
    }

    public int getClientId() {
        return clientId;
    }

    public Date getDate() {
        return date;
    }

    // SETTERS
    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public void setPropertyId(int propertyId) {
        this.propertyId = propertyId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}