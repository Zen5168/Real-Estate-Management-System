package com.mycompany.realestatemanagementsystem;

import java.awt.Font;
import java.awt.event.*;
import javax.swing.*;

public class LoginPage extends JFrame implements ActionListener {

    private boolean isLoginMode; // TRUE = LOGIN SCREEN, FALSE = REGISTER SCREEN
    private JLabel lblTitle, lblUsername, lblPassword, lblConfirm;
    private JTextField txtUsername;
    private JPasswordField txtPassword, txtConfirm;
    private JButton btnSubmit, btnSwitch;

    LoginPage() {

        isLoginMode = true;

        setTitle("Login");
        setSize(300, 330);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null); // CENTERS THE WINDOW
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // CREATE TITLE
        lblTitle = new JLabel("Login");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setBounds(10, 10, 260, 30);
        add(lblTitle);

        // CREATE USERNAME FIELD
        lblUsername = new JLabel("Username");
        lblUsername.setBounds(20, 55, 240, 20);
        add(lblUsername);
        
        txtUsername = new JTextField();
        txtUsername.setBounds(20, 78, 240, 30);
        add(txtUsername);

        // CREATE PASSWORD FIELD
        lblPassword = new JLabel("Password");
        lblPassword.setBounds(20, 118, 240, 20);
        add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(20, 141, 240, 30);
        add(txtPassword);

        // CREATE CONFIRM PASSWORD FIELD (ONLY VISIBLE IN REGISTER MODE)
        lblConfirm = new JLabel("Confirm Password");
        lblConfirm.setBounds(20, 181, 240, 20);
        lblConfirm.setVisible(false);
        add(lblConfirm);

        txtConfirm = new JPasswordField();
        txtConfirm.setBounds(20, 204, 240, 30);
        txtConfirm.setVisible(false);
        add(txtConfirm);

        // CREATE BUTTONS
        btnSubmit = new JButton("Login");
        btnSubmit.setBounds(20, 190, 240, 35);
        add(btnSubmit);

        btnSwitch = new JButton("No account? Register");
        btnSwitch.setBounds(20, 235, 240, 30);
        add(btnSwitch);

        // ENTER KEY TRIGGERS THE SUBMIT BUTTON
        getRootPane().setDefaultButton(btnSubmit);

        // ADD TO ACTION LISTENER
        btnSubmit.addActionListener(this);
        btnSwitch.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSubmit) {

            if (isLoginMode) {
                handleLogin();
            } else {
                handleRegister();
            }

        } else if (e.getSource() == btnSwitch) {
            switchMode();
        }

    }

    // SWITCHES BETWEEN LOGIN AND REGISTER SCREEN
    private void switchMode() {
        isLoginMode = !isLoginMode;
        clearFields();

        if (isLoginMode) { //LOGIN MODE
            setTitle("Login");
            lblTitle.setText("Login");
            btnSubmit.setText("Login");
            btnSwitch.setText("No account? Register");

            lblConfirm.setVisible(false);
            txtConfirm.setVisible(false);

            btnSubmit.setBounds(20, 190, 240, 35);
            btnSwitch.setBounds(20, 235, 240, 30);
            setSize(300, 330);

        } else { // REGISTER MODE
            setTitle("Register");
            lblTitle.setText("Register");
            btnSubmit.setText("Register");
            btnSwitch.setText("Have an account? Login");

            lblConfirm.setVisible(true);
            txtConfirm.setVisible(true);

            btnSubmit.setBounds(20, 253, 240, 35);
            btnSwitch.setBounds(20, 298, 240, 30);
            setSize(300, 393);
        }
    }

    // LOGIN LOGIC
    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) { // IF EMPTY
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // BACK END (PLACE HOLDER)
        boolean success = loginUser(username, password);

        // FRONT END
        if (success) {
            JOptionPane.showMessageDialog(this, "Welcome, " + username + "!", "Login Successful", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    // REGISTER LOGIC
    private void handleRegister() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        String confirm = new String(txtConfirm.getPassword());

        if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()) { // IF EMPTY
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirm)) { // IF PASSWORDS DON'T MATCH
            JOptionPane.showMessageDialog(this, "Passwords do not match", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // BACK END (PLACE HOLDER)
        boolean success = registerUser(username, password);

        // FRONT END
        if (success) {
            JOptionPane.showMessageDialog(this, "Account created! You can now log in.", "Register Successful", JOptionPane.INFORMATION_MESSAGE);
            switchMode(); // GO BACK TO LOGIN SCREEN
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed", "Register Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean loginUser(String username, String password) {
        // PLACE HOLDER FOR FUTURE USE 
        return true; 
    }

    private boolean registerUser(String username, String password) {
       // PLACE HOLDER FOR FUTURE USE
        return true; 
    }

    // CLEARS ALL INPUT FIELDS
    private void clearFields() {
        txtUsername.setText("");
        txtPassword.setText("");
        txtConfirm.setText("");
    }
}
