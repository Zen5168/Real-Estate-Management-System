package com.mycompany.realestatemanagementsystem;
 
import java.awt.Font;
import java.awt.event.*;
import java.nio.charset.StandardCharsets;
import javax.swing.*;
import java.sql.*;
import java.security.*;
 
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
 
        setLocationRelativeTo(null); // RE-CENTER AFTER RESIZE
    }
 
    // LOGIN LOGIC
    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
 
        if (username.isEmpty() || password.isEmpty()) { // IF EMPTY
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
 
        // BACK END
        boolean success = loginUser(username, password);
 
        // FRONT END
        if (success) {
            JOptionPane.showMessageDialog(this, "Welcome, " + username + "!", "Login Successful", JOptionPane.INFORMATION_MESSAGE);
            clearFields();

            // OPEN THE MAIN WINDOW
             new MainFrame().setVisible(true);
             dispose();
 
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
 
        if (username.length() > 50) {
            JOptionPane.showMessageDialog(this, "Username must be 50 characters or less", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
 
        if (!password.equals(confirm)) { // IF PASSWORDS DON'T MATCH
            JOptionPane.showMessageDialog(this, "Passwords do not match", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
 
        // BACK END
        boolean success = registerUser(username, password);
 
        // FRONT END
        if (success) {
            JOptionPane.showMessageDialog(this, "Account created! You can now log in.", "Register Successful", JOptionPane.INFORMATION_MESSAGE);
            switchMode(); // GO BACK TO LOGIN SCREEN
        }
    }
 
    private boolean loginUser(String username, String password) {
        String sql = "SELECT 1 FROM users WHERE username = ? AND password = ?";
 
        try (Connection conn = DBConnection.getConnection(); PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
 
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, hashPassword(password));
 
            try (ResultSet rs = preparedStatement.executeQuery()) {
                return rs.next(); // TRUE IF A MATCHING ROW EXISTS
            }
 
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
 
    private boolean registerUser(String username, String password) {
        String checkSql = "SELECT 1 FROM users WHERE username = ?";
        String insertSql = "INSERT INTO users (username, password) VALUES (?, ?)";
 
        try (Connection conn = DBConnection.getConnection()) {
 
            // CHECK IF USERNAME IS ALREADY TAKEN
            try (PreparedStatement check = conn.prepareStatement(checkSql)) {
                check.setString(1, username);
                try (ResultSet resultSet = check.executeQuery()) {
                    if (resultSet.next()) {
                        JOptionPane.showMessageDialog(this, "Username already exists",
                                "Register Failed", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                }
            }
 
            // INSERT THE NEW USER (PASSWORD IS STORED AS A SHA-256 HASH)
            try (PreparedStatement insert = conn.prepareStatement(insertSql)) {
                insert.setString(1, username);
                insert.setString(2, hashPassword(password));
                return insert.executeUpdate() > 0;
            }
 
        } catch (SQLException error) {
            JOptionPane.showMessageDialog(this, "Database error: " + error.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
 
    // TURNS A PASSWORD INTO A SHA-256 HEX STRING
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
 
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b)); // CONVERTS BINARY BYTE INTO HEXADECIMAL FOR READABLE STORAGE
            }
            return sb.toString();
 
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("Could not hash password", ex);
        }
    }
 
    // CLEARS ALL INPUT FIELDS
    private void clearFields() {
        txtUsername.setText("");
        txtPassword.setText("");
        txtConfirm.setText("");
    }
}