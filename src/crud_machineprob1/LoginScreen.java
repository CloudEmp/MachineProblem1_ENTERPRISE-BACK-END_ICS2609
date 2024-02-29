/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package crud_machineprob1;

/**
 *
 * @author Blade Emperor
 */
import java.awt.*;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class LoginScreen {

    private static final int maxAtempts = 3;
    private static int attemptCounter = 0;
    private JFrame loginFrame;
    private JPanel centerPanel;
    private JLabel userLabel;
    private JLabel passLabel;
    private JTextField username;
    private JPasswordField password;
    private JButton loginB;

    public LoginScreen() {
        loginFrame = new JFrame("Login");
        centerPanel = new JPanel();
        userLabel = new JLabel("Username:");
        passLabel = new JLabel("Password:");
        username = new JTextField(30);
        password = new JPasswordField(20);
        loginB = new JButton("Login");
    }

    public void startUp() {
        //Containers
        loginFrame.setLayout(new BorderLayout());
        loginFrame.setVisible(true);
        loginFrame.setSize(500, 300);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setResizable(false);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        centerPanel.setLayout(null);

        //Add to Containers
        loginFrame.add(centerPanel, BorderLayout.CENTER);
        centerPanel.add(userLabel);
        centerPanel.add(passLabel);
        centerPanel.add(username);
        centerPanel.add(password);
        centerPanel.add(loginB);

        //Component Positions
        userLabel.setBounds(120, 80, 79, 36);
        username.setBounds(190, 90, 180, 20);
        passLabel.setBounds(120, 110, 79, 36);
        password.setBounds(190, 120, 180, 20);
        loginB.setBounds(190, 150, 90, 20);

        //Add ActionListeners
        loginB.addActionListener(
                (e) -> {
                    loginResult();
                }
        );
    }

    public void loginResult() {
        String user = username.getText();
        String pass = new String(password.getPassword());

        try {
            DatabaseManager database = new DatabaseManager();
            User currentUser = database.loginAuthenticate(user, pass);
            database.closeConnection();

            if (currentUser != null) {
                String userRole = currentUser.getUserRole();

                if (userRole.equals("Admin")) {
                    loginFrame.dispose();
                    AdminScreen admin = new AdminScreen(currentUser);
                    admin.startUp();
                    attemptCounter = 0;
                } else {
                    loginFrame.dispose();
                    GuestScreen guest = new GuestScreen();
                    guest.startUp();
                    attemptCounter = 0;
                }
            } else {
                attemptCounter++;
                if (attemptCounter != maxAtempts) {
                    JOptionPane.showMessageDialog(loginFrame, "Incorrect Username / Password", "Error Screen", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "Sorry you have reached the limit of 3 tries, good bye!", "Error Screen", JOptionPane.ERROR_MESSAGE);
                    loginFrame.dispose();
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        LoginScreen login = new LoginScreen();
        login.startUp();
    }

}
