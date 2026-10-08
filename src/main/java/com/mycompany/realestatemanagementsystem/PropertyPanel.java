package com.mycompany.realestatemanagementsystem;

import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PropertyPanel extends JPanel implements ActionListener {

    private JLabel lblLocation, lblPrice, lblStatus;
    private JTextField txtLocation, txtPrice, txtSearch;
    private JComboBox<String> cmbStatus, cmbSearchBy;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear, btnSearch, btnShowAll;
    private JTable tblProperties;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;

    private PropertyDAO propertyDAO;
    private ArrayList<Property> propertyList;
    private int selectedId; // 0 MEANS NOTHING IS SELECTED

    PropertyPanel() {

        propertyDAO = new PropertyDAO();
        propertyList = new ArrayList<>();
        selectedId = 0;

        setLayout(null);

        // CREATE LOCATION FIELD
        lblLocation = new JLabel("Location");
        lblLocation.setBounds(20, 15, 250, 20);
        add(lblLocation);

        txtLocation = new JTextField();
        txtLocation.setBounds(20, 38, 250, 30);
        add(txtLocation);

        // CREATE PRICE FIELD
        lblPrice = new JLabel("Price");
        lblPrice.setBounds(290, 15, 150, 20);
        add(lblPrice);

        txtPrice = new JTextField();
        txtPrice.setBounds(290, 38, 150, 30);
        add(txtPrice);

        // CREATE STATUS FIELD
        lblStatus = new JLabel("Status");
        lblStatus.setBounds(460, 15, 150, 20);
        add(lblStatus);

        cmbStatus = new JComboBox<>(new String[]{"AVAILABLE", "SOLD"});
        cmbStatus.setBounds(460, 38, 150, 30);
        add(cmbStatus);

        // CREATE BUTTONS
        btnAdd = new JButton("Add");
        btnAdd.setBounds(20, 80, 100, 30);
        add(btnAdd);

        btnUpdate = new JButton("Update");
        btnUpdate.setBounds(130, 80, 100, 30);
        add(btnUpdate);

        btnDelete = new JButton("Delete");
        btnDelete.setBounds(240, 80, 100, 30);
        add(btnDelete);

        btnClear = new JButton("Clear");
        btnClear.setBounds(350, 80, 100, 30);
        add(btnClear);

        // CREATE SEARCH CONTROLS
        cmbSearchBy = new JComboBox<>(new String[]{"Location", "Price"});
        cmbSearchBy.setBounds(460, 80, 90, 30);
        add(cmbSearchBy);

        txtSearch = new JTextField();
        txtSearch.setBounds(555, 80, 110, 30);
        add(txtSearch);

        btnSearch = new JButton("Search");
        btnSearch.setBounds(670, 80, 80, 30);
        add(btnSearch);

        btnShowAll = new JButton("Show All");
        btnShowAll.setBounds(755, 80, 90, 30);
        add(btnShowAll);

        // CREATE TABLE (CELLS CANNOT BE EDITED DIRECTLY)
        tableModel = new DefaultTableModel(new String[]{"ID", "Location", "Price", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblProperties = new JTable(tableModel);
        tblProperties.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        scrollPane = new JScrollPane(tblProperties);
        scrollPane.setBounds(20, 125, 825, 350);
        add(scrollPane);

        // ADD TO ACTION LISTENER
        btnAdd.addActionListener(this);
        btnUpdate.addActionListener(this);
        btnDelete.addActionListener(this);
        btnClear.addActionListener(this);
        btnSearch.addActionListener(this);
        btnShowAll.addActionListener(this);

        // CLICKING A ROW FILLS THE FORM SO IT CAN BE UPDATED OR DELETED
        tblProperties.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fillFormFromTable();
            }
        });

        loadProperties();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAdd) {
            handleAdd();
        } else if (e.getSource() == btnUpdate) {
            handleUpdate();
        } else if (e.getSource() == btnDelete) {
            handleDelete();
        } else if (e.getSource() == btnClear) {
            clearForm();
        } else if (e.getSource() == btnSearch) {
            handleSearch();
        } else if (e.getSource() == btnShowAll) {
            txtSearch.setText("");
            showTable(propertyList);
        }
    }

    // ADD PROPERTY LOGIC
    private void handleAdd() {
        String location = txtLocation.getText().trim();
        String priceText = txtPrice.getText().trim();
        String status = (String) cmbStatus.getSelectedItem();

        if (location.isEmpty() || priceText.isEmpty()) { // IF EMPTY
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (location.length() > 150) { // MATCHES VARCHAR(150) IN THE PROPERTIES TABLE
            JOptionPane.showMessageDialog(this, "Location must be 150 characters or less", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double price = parsePrice(priceText);
        if (price < 0) { // parsePrice ALREADY SHOWED THE ERROR
            return;
        }

        try {
            // BACK END
            boolean success = propertyDAO.addProperty(new Property(location, price, status));

            // FRONT END
            if (success) {
                JOptionPane.showMessageDialog(this, "Property added!", "Add Successful", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadProperties();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // UPDATE PROPERTY LOGIC
    private void handleUpdate() {
        if (selectedId == 0) { // NOTHING SELECTED
            JOptionPane.showMessageDialog(this, "Select a property from the table first", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String location = txtLocation.getText().trim();
        String priceText = txtPrice.getText().trim();
        String status = (String) cmbStatus.getSelectedItem();

        if (location.isEmpty() || priceText.isEmpty()) { // IF EMPTY
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (location.length() > 150) {
            JOptionPane.showMessageDialog(this, "Location must be 150 characters or less", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double price = parsePrice(priceText);
        if (price < 0) {
            return;
        }

        try {
            boolean success = propertyDAO.updateProperty(new Property(selectedId, location, price, status));

            if (success) {
                JOptionPane.showMessageDialog(this, "Property updated!", "Update Successful", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadProperties();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // DELETE PROPERTY LOGIC
    private void handleDelete() {
        if (selectedId == 0) { // NOTHING SELECTED
            JOptionPane.showMessageDialog(this, "Select a property from the table first", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this, "Delete this property?", "Delete", JOptionPane.YES_NO_OPTION);
        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            boolean success = propertyDAO.deleteProperty(selectedId);

            if (success) {
                JOptionPane.showMessageDialog(this, "Property deleted!", "Delete Successful", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadProperties();
            }
        } catch (SQLIntegrityConstraintViolationException ex) { // PROPERTY IS USED BY A TRANSACTION
            JOptionPane.showMessageDialog(this, "This property has transactions, so it cannot be deleted",
                    "Delete Failed", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // SEARCH LOGIC (USES BINARY SEARCH)
    private void handleSearch() {
        String searchText = txtSearch.getText().trim();
        String searchBy = (String) cmbSearchBy.getSelectedItem();

        if (searchText.isEmpty()) { // IF EMPTY
            JOptionPane.showMessageDialog(this, "Enter something to search for", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ArrayList<Property> results;

        if (searchBy.equals("Price")) {
            double price = parsePrice(searchText);
            if (price < 0) {
                return;
            }
            results = PropertySearch.searchByPrice(propertyList, price);
        } else {
            results = PropertySearch.searchByLocation(propertyList, searchText);
        }

        if (results.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No properties found", "Search", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        showTable(results);
    }

    // CONVERTS TEXT TO A PRICE (RETURNS -1 AND SHOWS AN ERROR IF IT IS NOT VALID)
    private double parsePrice(String text) {
        try {
            double price = Double.parseDouble(text.replace(",", ""));

            if (price <= 0 || Double.isNaN(price) || Double.isInfinite(price)) {
                JOptionPane.showMessageDialog(this, "Price must be greater than 0", "Input Error", JOptionPane.ERROR_MESSAGE);
                return -1;
            }

            return price;

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Price must be a number", "Input Error", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
    }

    // LOADS ALL PROPERTIES FROM THE DATABASE INTO THE LIST AND THE TABLE
    private void loadProperties() {
        try {
            propertyList = propertyDAO.getAllProperties();
            showTable(propertyList);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Could not load properties: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // SHOWS A LIST OF PROPERTIES IN THE TABLE
    private void showTable(ArrayList<Property> list) {
        tableModel.setRowCount(0);

        for (Property property : list) {
            tableModel.addRow(new Object[]{
                property.getPropertyId(),
                property.getLocation(),
                String.format("%,.2f", property.getPrice()),
                property.getStatus()
            });
        }
    }

    // FILLS THE FORM WITH THE ROW THAT WAS CLICKED
    private void fillFormFromTable() {
        int row = tblProperties.getSelectedRow();
        if (row < 0) {
            return;
        }

        int id = (int) tableModel.getValueAt(row, 0);

        for (Property property : propertyList) {
            if (property.getPropertyId() == id) {
                selectedId = id;
                txtLocation.setText(property.getLocation());
                txtPrice.setText(String.valueOf(property.getPrice()));
                cmbStatus.setSelectedItem(property.getStatus());
                return;
            }
        }
    }

    // CLEARS ALL INPUT FIELDS
    private void clearForm() {
        txtLocation.setText("");
        txtPrice.setText("");
        cmbStatus.setSelectedIndex(0);
        selectedId = 0;
        tblProperties.clearSelection();
    }
}