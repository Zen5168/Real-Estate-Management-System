package com.mycompany.realestatemanagementsystem;

import java.awt.Font;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class MainFrame extends JFrame implements ActionListener {

    private JLabel lblTitle;
    private JButton btnLogout;
    private JTabbedPane tabbedPane;
    private PropertyPanel pnlProperties;
    private ClientPanel pnlClients;
    private TransactionPanel pnlTransactions;
    private ReportPanel pnlReports;

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

        // CREATE THE PANEL FOR EACH TAB (EACH ONE USES NULL LAYOUT TOO)
        pnlProperties = new PropertyPanel(); // PROPERTIES TAB HAS ITS OWN CLASS

        pnlClients = new ClientPanel(); // CLIENTS TAB HAS ITS OWN CLASS

        pnlTransactions = new TransactionPanel(); // TRANSACTIONS TAB HAS ITS OWN CLASS

        pnlReports = new ReportPanel(); // REPORTS TAB HAS ITS OWN CLASS

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

        // RELOAD A TAB'S DATA EVERY TIME IT IS OPENED, SO IT NEVER SHOWS OLD DATA
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int selected = tabbedPane.getSelectedIndex();

                if (selected == 0) {
                    pnlProperties.refresh();
                } else if (selected == 1) {
                    pnlClients.refresh();
                } else if (selected == 2) {
                    pnlTransactions.refresh();
                } else if (selected == 3) {
                    pnlReports.refresh();
                }
            }
        });

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