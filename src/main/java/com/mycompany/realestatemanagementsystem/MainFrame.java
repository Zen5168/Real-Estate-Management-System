package com.mycompany.realestatemanagementsystem;

import java.awt.Font;
import java.awt.event.*;
import javax.swing.*;

public class MainFrame extends JFrame implements ActionListener {

    private JLabel lblTitle;
    private JButton btnLogout;
    private JTabbedPane tabbedPane;
    private JPanel pnlProperties, pnlClients, pnlTransactions, pnlReports;

    MainFrame() {

        setTitle("Real Estate Management System");
        setSize(900, 620);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null); // CENTERS THE WINDOW
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // CREATE TITLE
        lblTitle = new JLabel("Real Estate Management System");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setBounds(20, 10, 500, 30);
        add(lblTitle);

        // CREATE LOGOUT BUTTON
        btnLogout = new JButton("Logout");
        btnLogout.setBounds(770, 12, 100, 30);
        add(btnLogout);

        // CREATE THE PANEL FOR EACH TAB
        pnlProperties = new PropertyPanel();

        pnlClients = new JPanel();
        pnlClients.setLayout(null);

        pnlTransactions = new JPanel();
        pnlTransactions.setLayout(null);

        pnlReports = new JPanel();
        pnlReports.setLayout(null);

        JLabel lblClientsHolder = new JLabel("Clients tab");
        lblClientsHolder.setBounds(20, 20, 300, 25);
        pnlClients.add(lblClientsHolder);

        JLabel lblTransactionsHolder = new JLabel("Transactions tab");
        lblTransactionsHolder.setBounds(20, 20, 300, 25);
        pnlTransactions.add(lblTransactionsHolder);

        JLabel lblReportsHolder = new JLabel("Reports tab");
        lblReportsHolder.setBounds(20, 20, 300, 25);
        pnlReports.add(lblReportsHolder);

        // CREATE THE TABS
        tabbedPane = new JTabbedPane();
        tabbedPane.setBounds(10, 55, 865, 520);
        tabbedPane.addTab("Properties", pnlProperties);
        tabbedPane.addTab("Clients", pnlClients);
        tabbedPane.addTab("Transactions", pnlTransactions);
        tabbedPane.addTab("Reports", pnlReports);
        add(tabbedPane);

        // ADD TO ACTION LISTENER
        btnLogout.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnLogout) {
            handleLogout();
        }
    }

    // GOES BACK TO THE LOGIN SCREEN
    private void handleLogout() {
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out?", "Logout", JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            new LoginPage().setVisible(true);
            dispose();
        }
    }
}