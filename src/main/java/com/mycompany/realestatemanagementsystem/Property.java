package com.mycompany.realestatemanagementsystem;

public class Property {

    private int propertyId;
    private String location;
    private double price;
    private String status; // AVAILABLE OR SOLD

    // CONSTRUCTOR FOR A PROPERTY LOADED FROM THE DATABASE
    public Property(int propertyId, String location, double price, String status) {
        this.propertyId = propertyId;
        this.location = location;
        this.price = price;
        this.status = status;
    }

    // CONSTRUCTOR FOR A NEW PROPERTY
    public Property(String location, double price, String status) {
        this(0, location, price, status);
    }

    // GETTERS
    public int getPropertyId() {
        return propertyId;
    }

    public String getLocation() {
        return location;
    }

    public double getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }

    // SETTERS
    public void setPropertyId(int propertyId) {
        this.propertyId = propertyId;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // USED WHEN A PROPERTY IS SHOWN IN A COMBO BOX
    @Override
    public String toString() {
        return propertyId + " - " + location;
    }
}