package com.mycompany.realestatemanagementsystem;

import java.util.ArrayList;

public class Client {

    private int clientId;
    private String name;
    private String contactInfo;
    private ArrayList<Property> propertiesOwned; // FILLED FROM THE TRANSACTIONS TABLE

    // CONSTRUCTOR FOR A CLIENT LOADED FROM THE DATABASE 
    public Client(int clientId, String name, String contactInfo) {
        this.clientId = clientId;
        this.name = name;
        this.contactInfo = contactInfo;
        this.propertiesOwned = new ArrayList<>();
    }

    // CONSTRUCTOR FOR A NEW CLIENT
    public Client(String name, String contactInfo) {
        this(0, name, contactInfo);
    }

    // GETTERS
    public int getClientId() {
        return clientId;
    }

    public String getName() {
        return name;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public ArrayList<Property> getPropertiesOwned() {
        return propertiesOwned;
    }

    // SETTERS
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    // ADDS A PROPERTY TO THIS CLIENT'S OWNED LIST
    public void addProperty(Property property) {
        propertiesOwned.add(property);
    }

    // USED WHEN A CLIENT IS SHOWN IN A COMBO BOX
    @Override
    public String toString() {
        return clientId + " - " + name;
    }
}