package com.mycompany.realestatemanagementsystem;

import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class TransactionPanel extends JPanel implements ActionListener {

    private JLabel lblProperty, lblClient, lblDate;
    private JComboBox<Property> cmbProperty;
    private JComboBox<Client> cmbClient;
    private JTextField txtDate;
    private JButton btnRecord, btnCancel, btnRefresh;
    private JTable tblTransactions;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;

    private TransactionDAO transactionDAO;
    private PropertyDAO propertyDAO;
    private ClientDAO clientDAO;
    private HashMap<Integer, Client> clientMap; // KEY = clientId, FOR FAST ACCESS
    private HashMap<Integer, Property> propertyMap; // KEY = propertyId, FOR FAST ACCESS
    private ArrayList<Transaction> transactionList;

    TransactionPanel() {

        transactionDAO = new TransactionDAO();
        propertyDAO = new PropertyDAO();
        clientDAO = new ClientDAO();
        clientMap = new HashMap<>();
        propertyMap = new HashMap<>();
        transactionList = new ArrayList<>();

        setLayout(null);

        // CREATE PROPERTY FIELD (ONLY AVAILABLE PROPERTIES ARE LISTED)
        lblProperty = new JLabel("Property (available only)");
        lblProperty.setBounds(20, 15, 300, 20);
        add(lblProperty);

        cmbProperty = new JComboBox<>();
        cmbProperty.setBounds(20, 38, 300, 30);
        add(cmbProperty);

        // CREATE CLIENT FIELD
        lblClient = new JLabel("Client");
        lblClient.setBounds(340, 15, 250, 20);
        add(lblClient);

        cmbClient = new JComboBox<>();
        cmbClient.setBounds(340, 38, 250, 30);
        add(cmbClient);

        // CREATE DATE FIELD (STARTS AS TODAY)
        lblDate = new JLabel("Date (yyyy-mm-dd)");
        lblDate.setBounds(610, 15, 150, 20);
        add(lblDate);

        txtDate = new JTextField(LocalDate.now().toString());
        txtDate.setBounds(610, 38, 150, 30);
        add(txtDate);

        // CREATE BUTTONS
        btnRecord = new JButton("Record Sale");
        btnRecord.setBounds(20, 80, 130, 30);
        add(btnRecord);

        btnCancel = new JButton("Cancel Sale");
        btnCancel.setBounds(160, 80, 130, 30);
        add(btnCancel);

        btnRefresh = new JButton("Refresh");
        btnRefresh.setBounds(300, 80, 100, 30);
        add(btnRefresh);

        // CREATE TABLE (CELLS CANNOT BE EDITED DIRECTLY)
        tableModel = new DefaultTableModel(new String[]{"ID", "Property", "Client", "Price", "Date"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblTransactions = new JTable(tableModel);
        tblTransactions.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        scrollPane = new JScrollPane(tblTransactions);
        scrollPane.setBounds(20, 125, 825, 350);
        add(scrollPane);

        // ADD TO ACTION LISTENER
        btnRecord.addActionListener(this);
        btnCancel.addActionListener(this);
        btnRefresh.addActionListener(this);

        refresh();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnRecord) {
            handleRecord();
        } else if (e.getSource() == btnCancel) {
            handleCancel();
        } else if (e.getSource() == btnRefresh) {
            refresh();
        }
    }

    // RECORD SALE LOGIC
    private void handleRecord() {
        Property property = (Property) cmbProperty.getSelectedItem();
        Client client = (Client) cmbClient.getSelectedItem();
        String dateText = txtDate.getText().trim();

        if (property == null) { // NO AVAILABLE PROPERTIES
            JOptionPane.showMessageDialog(this, "There are no available properties. Add a property first.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (client == null) { // NO CLIENTS
            JOptionPane.showMessageDialog(this, "There are no clients. Add a client first.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (dateText.isEmpty()) { // IF EMPTY
            JOptionPane.showMessageDialog(this, "Please enter a date", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        java.sql.Date date;
        try {
            date = java.sql.Date.valueOf(LocalDate.parse(dateText)); // VALIDATES REAL CALENDAR DATES
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Date must be a real date in yyyy-mm-dd format", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // BACK END
            boolean success = transactionDAO.addTransaction(new Transaction(property.getPropertyId(), client.getClientId(), date));

            // FRONT END
            if (success) {
                JOptionPane.showMessageDialog(this, "Sale recorded! The property is now SOLD.", "Sale Recorded", JOptionPane.INFORMATION_MESSAGE);
                txtDate.setText(LocalDate.now().toString());
            } else {
                JOptionPane.showMessageDialog(this, "That property is no longer available", "Sale Failed", JOptionPane.ERROR_MESSAGE);
            }

            refresh();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // CANCEL SALE LOGIC
    private void handleCancel() {
        int row = tblTransactions.getSelectedRow();

        if (row < 0) { // NOTHING SELECTED
            JOptionPane.showMessageDialog(this, "Select a transaction from the table first", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int transactionId = (int) tableModel.getValueAt(row, 0);

        int choice = JOptionPane.showConfirmDialog(this, "Cancel this sale? The property will be set back to AVAILABLE.", "Cancel Sale", JOptionPane.YES_NO_OPTION);
        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            boolean success = transactionDAO.deleteTransaction(transactionId);

            if (success) {
                JOptionPane.showMessageDialog(this, "Sale cancelled. The property is AVAILABLE again.", "Sale Cancelled", JOptionPane.INFORMATION_MESSAGE);
            }

            refresh();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // RELOADS EVERYTHING FROM THE DATABASE (CALLED WHEN THIS TAB IS OPENED)
    public void refresh() {
        try {
            ArrayList<Property> properties = propertyDAO.getAllProperties();
            clientMap = clientDAO.getAllClients();
            transactionList = transactionDAO.getAllTransactions();

            // BUILD THE PROPERTY HASH MAP
            propertyMap.clear();
            for (Property property : properties) {
                propertyMap.put(property.getPropertyId(), property);
            }

            // FILL THE PROPERTY DROP DOWN WITH AVAILABLE PROPERTIES ONLY
            cmbProperty.removeAllItems();
            for (Property property : properties) {
                if (property.getStatus().equals("AVAILABLE")) {
                    cmbProperty.addItem(property);
                }
            }

            // FILL THE CLIENT DROP DOWN (SORTED BY ID, BECAUSE A HASH MAP HAS NO ORDER)
            ArrayList<Client> clients = new ArrayList<>(clientMap.values());
            Collections.sort(clients, new Comparator<Client>() {
                @Override
                public int compare(Client a, Client b) {
                    return Integer.compare(a.getClientId(), b.getClientId());
                }
            });

            cmbClient.removeAllItems();
            for (Client client : clients) {
                cmbClient.addItem(client);
            }

            showTable();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Could not load data: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // SHOWS ALL TRANSACTIONS IN THE TABLE
    private void showTable() {
        tableModel.setRowCount(0);

        for (Transaction transaction : transactionList) {
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
