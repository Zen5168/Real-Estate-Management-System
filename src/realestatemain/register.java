/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package realestatemain;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class register extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel contentPane;
    private JTextField firstname;
    private JTextField lastname;
    private JTextField email;
    private JTextField username;
    private JTextField mob;
    private JPasswordField passwordField;
    private JButton btnNewButton;

    /**
     * Launch the application.
     * @param args
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                register frame = new register();
                frame.setVisible(true);
            } catch (Exception e) {
            }
        });
    }

    /**
     * Create the frame.
     */
    public register() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(450, 190, 1014, 597);
        setResizable(false);

        contentPane = new JPanel();
        contentPane.setBackground(new Color(240, 244, 248));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewUserRegister = new JLabel("New Register");
        lblNewUserRegister.setFont(new Font("Tahoma", Font.PLAIN, 42));
        lblNewUserRegister.setBounds(362, 52, 325, 50);
        contentPane.add(lblNewUserRegister);

        JLabel lblName = new JLabel("First name");
        lblName.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lblName.setBounds(58, 152, 99, 43);
        contentPane.add(lblName);

        firstname = new JTextField();
        firstname.setFont(new Font("Tahoma", Font.PLAIN, 32));
        firstname.setBounds(214, 151, 228, 50);
        contentPane.add(firstname);

        JLabel lblNewLabel = new JLabel("Last name");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lblNewLabel.setBounds(58, 243, 110, 29);
        contentPane.add(lblNewLabel);

        lastname = new JTextField();
        lastname.setFont(new Font("Tahoma", Font.PLAIN, 32));
        lastname.setBounds(214, 235, 228, 50);
        contentPane.add(lastname);

        JLabel lblEmailAddress = new JLabel("Email address");
        lblEmailAddress.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lblEmailAddress.setBounds(58, 324, 124, 36);
        contentPane.add(lblEmailAddress);

        email = new JTextField();
        email.setFont(new Font("Tahoma", Font.PLAIN, 32));
        email.setBounds(214, 320, 228, 50);
        contentPane.add(email);

        JLabel lblUsername = new JLabel("Username");
        lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lblUsername.setBounds(542, 159, 99, 29);
        contentPane.add(lblUsername);

        username = new JTextField();
        username.setFont(new Font("Tahoma", Font.PLAIN, 32));
        username.setBounds(707, 151, 228, 50);
        contentPane.add(username);

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lblPassword.setBounds(542, 245, 99, 24);
        contentPane.add(lblPassword);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Tahoma", Font.PLAIN, 32));
        passwordField.setBounds(707, 235, 228, 50);
        contentPane.add(passwordField);

        JLabel lblMobileNumber = new JLabel("Mobile number");
        lblMobileNumber.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lblMobileNumber.setBounds(542, 329, 139, 26);
        contentPane.add(lblMobileNumber);

        mob = new JTextField();
        mob.setFont(new Font("Tahoma", Font.PLAIN, 32));
        mob.setBounds(707, 320, 228, 50);
        contentPane.add(mob);

        // Register button

        btnNewButton = new JButton("Register");
        btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 22));
        btnNewButton.setBackground(new Color(37, 99, 235));
        btnNewButton.setForeground(Color.WHITE);
        btnNewButton.setFocusPainted(false);
        btnNewButton.setBorderPainted(false);
        btnNewButton.setBounds(280, 447, 220, 65);

        btnNewButton.addActionListener((ActionEvent e) -> {

            String firstName = firstname.getText();
            String lastName = lastname.getText();
            String emailId = email.getText();
            String userName = username.getText();
            String mobileNumber = mob.getText();
            String password = new String(passwordField.getPassword());

            // Basic validation

            if (firstName.isEmpty() ||
                lastName.isEmpty() ||
                emailId.isEmpty() ||
                userName.isEmpty() ||
                mobileNumber.isEmpty() ||
                password.isEmpty()) {

                JOptionPane.showMessageDialog(
                    btnNewButton,
                    "Please fill in all fields."
                );
                return;
            }

            if (mobileNumber.length() != 11) {
                JOptionPane.showMessageDialog(
                    btnNewButton,
                    "Your sim number must contain 11 digits."
                );
                return;
            }

            // Message success register dialogue

            JOptionPane.showMessageDialog(
                btnNewButton,
                "Welcome, " + firstName +
                "! Your account has been successfully created."
            );
        });

        // Back to Login button

        contentPane.add(btnNewButton);

        JButton btnBackToLogin = new JButton("Back to Login");
        btnBackToLogin.setFont(new Font("Tahoma", Font.BOLD, 18));
        btnBackToLogin.setBackground(new Color(37, 99, 235));
        btnBackToLogin.setForeground(Color.WHITE);
        btnBackToLogin.setFocusPainted(false);
        btnBackToLogin.setBorderPainted(false);
        btnBackToLogin.setBounds(520, 447, 220, 65);

        btnBackToLogin.addActionListener((ActionEvent e) -> {
            login loginFrame = new login();
            loginFrame.setVisible(true);
            dispose();
        });

        contentPane.add(btnBackToLogin);
    }
}
