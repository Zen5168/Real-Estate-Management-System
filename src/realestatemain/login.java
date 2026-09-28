/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package realestatemain;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class login extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTextField textField;
    private JPasswordField passwordField;
    private JButton btnNewButton;
    private final JPanel contentPane;

    /**
     * Launch the application.
     * @param args
     */
    
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                login frame = new login();
                frame.setVisible(true);
            } catch (Exception e) {
            }
        });
    }

    /**
     * Create the frame. //////////
     */
    
    public login() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(450, 190, 1014, 620);
        setResizable(false);

        contentPane = new JPanel();
        contentPane.setBackground(new Color(239, 246, 255));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Login");
        lblNewLabel.setForeground(new Color(15, 23, 42));
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 46));
        lblNewLabel.setBounds(423, 13, 273, 93);
        contentPane.add(lblNewLabel);

        textField = new JTextField();
        textField.setFont(new Font("Tahoma", Font.PLAIN, 32));
        textField.setBounds(481, 170, 281, 68);
        contentPane.add(textField);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Tahoma", Font.PLAIN, 32));
        passwordField.setBounds(481, 286, 281, 68);
        contentPane.add(passwordField);

        JLabel lblUsername = new JLabel("Username");
        lblUsername.setForeground(Color.BLACK);
        lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 31));
        lblUsername.setBounds(250, 166, 193, 52);
        contentPane.add(lblUsername);

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setForeground(Color.BLACK);
        lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 31));
        lblPassword.setBounds(250, 286, 193, 52);
        contentPane.add(lblPassword);

        /**
     * Log in Button. //////////
     */
        
        btnNewButton = new JButton("Login");
        btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 22));
        btnNewButton.setBackground(new Color(37, 99, 235));
        btnNewButton.setForeground(Color.WHITE);
        btnNewButton.setFocusPainted(false);
        btnNewButton.setBorderPainted(false);
        btnNewButton.setBounds(545, 392, 162, 73);

        btnNewButton.addActionListener((ActionEvent e) -> {

            String userName = textField.getText();
            String password = new String(passwordField.getPassword());

            if (userName.isEmpty() || password.isEmpty()) {

                JOptionPane.showMessageDialog(
                    btnNewButton,
                    "Please enter your username and password."
                );

            } else {

                RealEstateMain ah = new RealEstateMain(userName);
                ah.setTitle("Welcome");
                ah.setVisible(true);

                dispose();

                JOptionPane.showMessageDialog(
                    btnNewButton,
                    "You have successfully logged in."
                );
            }
        });

        contentPane.add(btnNewButton);

        /**
     * Register Button. //////////
     */
        
        JButton btnRegister = new JButton("Register");
        btnRegister.setFont(new Font("Tahoma", Font.BOLD, 22));
        btnRegister.setBackground(new Color(37, 99, 235));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFocusPainted(false);
        btnRegister.setBorderPainted(false);
        btnRegister.setBounds(545, 480, 162, 73);

        btnRegister.addActionListener((ActionEvent e) -> {

            register registerFrame = new register();
            registerFrame.setVisible(true);
            dispose();
        });

        contentPane.add(btnRegister);
    }
}
