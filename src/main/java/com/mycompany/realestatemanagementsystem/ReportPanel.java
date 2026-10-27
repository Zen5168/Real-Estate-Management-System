package com.mycompany.realestatemanagementsystem;

import java.awt.Color;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ReportPanel extends JPanel implements ActionListener {

    private JLabel lblReport, lblSortBy, lblOrder;
    private JComboBox<String> cmbReport, cmbSortBy, cmbOrder;
    private JButton btnGenerate;
    private JTextArea txtSummary;
    private JTable tblReport;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;

    private PropertyDAO propertyDAO;
    private ClientDAO clientDAO;
    private TransactionDAO transactionDAO;

    private ArrayList<Property> propertyList;
    private ArrayList<Transaction> transactionList;
    private HashMap<Integer, Client> clientMap; // KEY = clientId, FOR FAST ACCESS
    private HashMap<Integer, Property> propertyMap; // KEY = propertyId, FOR FAST ACCESS

    ReportPanel() {

        propertyDAO = new PropertyDAO();
        clientDAO = new ClientDAO();
        transactionDAO = new TransactionDAO();
        propertyList = new ArrayList<>();
        transactionList = new ArrayList<>();
        clientMap = new HashMap<>();
        propertyMap = new HashMap<>();

        setLayout(null);

        // CREATE REPORT TYPE FIELD
        lblReport = new JLabel("Report");
        lblReport.setBounds(20, 15, 200, 20);
        add(lblReport);

        cmbReport = new JComboBox<>(new String[]{"Market Analysis", "Sales Report"});
        cmbReport.setBounds(20, 38, 200, 30);
        add(cmbReport);

        // CREATE SORT BY FIELD (THE CHOICES CHANGE WITH THE REPORT)
        lblSortBy = new JLabel("Sort by");
        lblSortBy.setBounds(240, 15, 140, 20);
        add(lblSortBy);

        cmbSortBy = new JComboBox<>();
        cmbSortBy.setBounds(240, 38, 140, 30);
        add(cmbSortBy);

        // CREATE ORDER FIELD
        lblOrder = new JLabel("Order");
        lblOrder.setBounds(400, 15, 120, 20);
        add(lblOrder);

        cmbOrder = new JComboBox<>(new String[]{"Ascending", "Descending"});
        cmbOrder.setBounds(400, 38, 120, 30);
        add(cmbOrder);

        // CREATE BUTTON
        btnGenerate = new JButton("Generate");
        btnGenerate.setBounds(540, 38, 110, 30);
        add(btnGenerate);

        // CREATE SUMMARY BOX
        txtSummary = new JTextArea();
        txtSummary.setEditable(false);
        txtSummary.setBackground(new Color(245, 245, 245));
        txtSummary.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        txtSummary.setBounds(20, 80, 825, 100);
        add(txtSummary);

        // CREATE TABLE (CELLS CANNOT BE EDITED DIRECTLY)
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblReport = new JTable(tableModel);

        scrollPane = new JScrollPane(tblReport);
        scrollPane.setBounds(20, 195, 825, 280);
        add(scrollPane);

        updateSortOptions();

        // ADD TO ACTION LISTENER
        cmbReport.addActionListener(this);
        btnGenerate.addActionListener(this);

        refresh();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cmbReport) {
            updateSortOptions();
            generateReport();
        } else if (e.getSource() == btnGenerate) {
            generateReport();
        }
    }

    // CHANGES THE SORT BY CHOICES TO MATCH THE SELECTED REPORT
    private void updateSortOptions() {
        cmbSortBy.removeAllItems();

        if (cmbReport.getSelectedItem().equals("Market Analysis")) {
            cmbSortBy.addItem("Price");
            cmbSortBy.addItem("Location");
            cmbSortBy.addItem("Status");
            cmbSortBy.addItem("ID");
        } else {
            cmbSortBy.addItem("Date");
            cmbSortBy.addItem("Price");
            cmbSortBy.addItem("Client");
            cmbSortBy.addItem("ID");
        }
    }

    // RELOADS EVERYTHING FROM THE DATABASE (CALLED WHEN THIS TAB IS OPENED)
    public void refresh() {
        try {
            propertyList = propertyDAO.getAllProperties();
            clientMap = clientDAO.getAllClients();
            transactionList = transactionDAO.getAllTransactions();

            // BUILD THE PROPERTY HASH MAP
            propertyMap.clear();
            for (Property property : propertyList) {
                propertyMap.put(property.getPropertyId(), property);
            }

            generateReport();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Could not load data: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // BUILDS THE SELECTED REPORT FROM THE LOADED DATA
    private void generateReport() {
        String sortBy = (String) cmbSortBy.getSelectedItem();
        boolean ascending = cmbOrder.getSelectedItem().equals("Ascending");

        if (sortBy == null) { // SORT CHOICES ARE BEING REBUILT
            return;
        }

        tableModel.setRowCount(0);

        if (cmbReport.getSelectedItem().equals("Market Analysis")) {

            // SORT A COPY, SO THE ORIGINAL LIST STAYS UNTOUCHED
            ArrayList<Property> sorted = new ArrayList<>(propertyList);
            ReportGenerator.sortProperties(sorted, sortBy, ascending);

            txtSummary.setText(ReportGenerator.marketSummary(propertyList));
            tableModel.setColumnIdentifiers(new String[]{"ID", "Location", "Price", "Status"});

            for (Property property : sorted) {
                tableModel.addRow(new Object[]{
                    property.getPropertyId(),
                    property.getLocation(),
                    String.format("%,.2f", property.getPrice()),
                    property.getStatus()
                });
            }

        } else {

            ArrayList<Transaction> sorted = new ArrayList<>(transactionList);
            ReportGenerator.sortTransactions(sorted, propertyMap, clientMap, sortBy, ascending);

            txtSummary.setText(ReportGenerator.salesSummary(transactionList, propertyMap, clientMap));
            tableModel.setColumnIdentifiers(new String[]{"ID", "Property", "Client", "Price", "Date"});

            for (Transaction transaction : sorted) {
                Property property = propertyMap.get(transaction.getPropertyId()); // HASH MAP LOOKUP
                Client client = clientMap.get(transaction.getClientId()); // HASH MAP LOOKUP

                tableModel.addRow(new Object[]{
                    transaction.getTransactionId(),
                    property != null ? property.toString() : "(unknown)",
                    client != null ? client.toString() : "(unknown)",
                    property != null ? String.format("%,.2f", property.getPrice()) : "",
                    transaction.getDate()
                });
            }
        }
    }
}