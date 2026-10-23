package com.mycompany.realestatemanagementsystem;

import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ClientPanel extends JPanel implements ActionListener {

    private JLabel lblName, lblContact;
    private JTextField txtName, txtContact, txtSearch;
    private JComboBox<String> cmbSearchBy;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear, btnSearch, btnShowAll;
    private JTable tblClients;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;

    private ClientDAO clientDAO;
    private HashMap<Integer, Client> clientMap; // KEY = clientId, FOR FAST ACCESS
    private int selectedId; // 0 MEANS NOTHING IS SELECTED

    ClientPanel() {

        clientDAO = new ClientDAO();
        clientMap = new HashMap<>();
        selectedId = 0;

        setLayout(null);

        // CREATE NAME FIELD
        lblName = new JLabel("Name");
        lblName.setBounds(20, 15, 250, 20);
        add(lblName);

        txtName = new JTextField();
        txtName.setBounds(20, 38, 250, 30);
        add(txtName);

        // CREATE CONTACT INFO FIELD
        lblContact = new JLabel("Contact Info");
        lblContact.setBounds(290, 15, 250, 20);
        add(lblContact);

        txtContact = new JTextField();
        txtContact.setBounds(290, 38, 250, 30);
        add(txtContact);

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
        cmbSearchBy = new JComboBox<>(new String[]{"Name", "ID"});
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
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Contact Info", "Properties Owned"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblClients = new JTable(tableModel);
        tblClients.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        scrollPane = new JScrollPane(tblClients);
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
        tblClients.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fillFormFromTable();
            }
        });

        loadClients();
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
            showTable(new ArrayList<>(clientMap.values()));
        }
    }

    // ADD LOGIC
    private void handleAdd() {
        String name = txtName.getText().trim();
        String contact = txtContact.getText().trim();

        if (name.isEmpty() || contact.isEmpty()) { // IF EMPTY
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (name.length() > 100 || contact.length() > 100) { // MATCHES VARCHAR(100) IN THE CLIENTS TABLE
            JOptionPane.showMessageDialog(this, "Name and contact info must be 100 characters or less", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // BACK END
            boolean success = clientDAO.addClient(new Client(name, contact));

            // FRONT END
            if (success) {
                JOptionPane.showMessageDialog(this, "Client added!", "Add Successful", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadClients();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // UPDATE LOGIC
    private void handleUpdate() {
        if (selectedId == 0) { // NOTHING SELECTED
            JOptionPane.showMessageDialog(this, "Select a client from the table first", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String name = txtName.getText().trim();
        String contact = txtContact.getText().trim();

        if (name.isEmpty() || contact.isEmpty()) { // IF EMPTY
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (name.length() > 100 || contact.length() > 100) {
            JOptionPane.showMessageDialog(this, "Name and contact info must be 100 characters or less", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            boolean success = clientDAO.updateClient(new Client(selectedId, name, contact));

            if (success) {
                JOptionPane.showMessageDialog(this, "Client updated!", "Update Successful", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadClients();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // DELETE LOGIC
    private void handleDelete() {
        if (selectedId == 0) { // NOTHING SELECTED
            JOptionPane.showMessageDialog(this, "Select a client from the table first", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this, "Delete this client?", "Delete", JOptionPane.YES_NO_OPTION);
        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            boolean success = clientDAO.deleteClient(selectedId);

            if (success) {
                JOptionPane.showMessageDialog(this, "Client deleted!", "Delete Successful", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadClients();
            }
        } catch (SQLIntegrityConstraintViolationException ex) { // CLIENT IS USED BY A TRANSACTION
            JOptionPane.showMessageDialog(this, "This client has transactions, so it cannot be deleted",
                    "Delete Failed", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // SEARCH LOGIC
    private void handleSearch() {
        String searchText = txtSearch.getText().trim();
        String searchBy = (String) cmbSearchBy.getSelectedItem();

        if (searchText.isEmpty()) { // IF EMPTY
            JOptionPane.showMessageDialog(this, "Enter something to search for", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ArrayList<Client> results = new ArrayList<>();

        if (searchBy.equals("ID")) {
            try {
                int id = Integer.parseInt(searchText);
                Client client = clientMap.get(id); // HASH MAP LOOKUP, NO LOOP NEEDED

                if (client != null) {
                    results.add(client);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID must be a whole number", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            // A HASH MAP IS ONLY FAST BY KEY, SO SEARCHING BY NAME LOOPS THROUGH THE VALUES
            for (Client client : clientMap.values()) {
                if (client.getName().toLowerCase().contains(searchText.toLowerCase())) {
                    results.add(client);
                }
            }
        }

        if (results.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No clients found", "Search", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        showTable(results);
    }

    // LOADS ALL CLIENTS FROM THE DATABASE INTO THE HASH MAP AND THE TABLE
    private void loadClients() {
        try {
            clientMap = clientDAO.getAllClients();
            showTable(new ArrayList<>(clientMap.values()));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Could not load clients: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // RELOADS THE TABLE FROM THE DATABASE (CALLED WHEN THIS TAB IS OPENED)
    public void refresh() {
        loadClients();
    }

    // SHOWS A LIST OF CLIENTS IN THE TABLE (SORTED BY ID, BECAUSE A HASH MAP HAS NO ORDER)
    private void showTable(ArrayList<Client> list) {
        Collections.sort(list, new Comparator<Client>() {
            @Override
            public int compare(Client a, Client b) {
                return Integer.compare(a.getClientId(), b.getClientId());
            }
        });

        tableModel.setRowCount(0);

        for (Client client : list) {
            tableModel.addRow(new Object[]{
                client.getClientId(),
                client.getName(),
                client.getContactInfo(),
                client.getPropertiesOwned().size()
            });
        }
    }

    // FILLS THE FORM WITH THE ROW THAT WAS CLICKED
    private void fillFormFromTable() {
        int row = tblClients.getSelectedRow();
        if (row < 0) {
            return;
        }

        int id = (int) tableModel.getValueAt(row, 0);
        Client client = clientMap.get(id); // HASH MAP LOOKUP

        if (client != null) {
            selectedId = id;
            txtName.setText(client.getName());
            txtContact.setText(client.getContactInfo());
        }
    }

    // CLEARS ALL INPUT FIELDS
    private void clearForm() {
        txtName.setText("");
        txtContact.setText("");
        selectedId = 0;
        tblClients.clearSelection();
    }
}
