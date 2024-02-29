/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package crud_machineprob1;

/**
 *
 * @author Blade Emperor
 */
import javax.swing.*;
import java.sql.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

public class AdminScreen {
    
    private JFrame adminFrame;
    private JPanel centerPanel;
    private JPanel upperPanel;
    private JButton logout;
    private JTable table;
    private JScrollPane scroll;
    private JLabel title;
    private JLabel lEmail;
    private JLabel lPassword;
    private JLabel lCPassword;
    private ButtonGroup role;
    private JRadioButton adminRadioButton;
    private JRadioButton guestRadioButton;
    private JTextField email;
    private JPasswordField password;
    private JPasswordField cPassword;
    private JLabel lUserRole;
    private JButton add;
    private JButton delete;
    private JButton update;
    private User currentUser;
    
    private JButton clear;
    
    public AdminScreen(User currentUser) {
        this.currentUser = currentUser;
        adminFrame = new JFrame("AdminScreen");
        centerPanel = new JPanel();
        upperPanel = new JPanel();
        logout = new JButton("LOGOUT");
        title = new JLabel("MP 1 (CURRENT ADMIN: " + this.currentUser.getEmail() + ")");
        lEmail = new JLabel("Email:");
        lPassword = new JLabel("Password:");
        lCPassword = new JLabel("Confirm Password:");
        email = new JTextField();
        password = new JPasswordField();
        cPassword = new JPasswordField();
        lUserRole = new JLabel("UserRole:");
        role = new ButtonGroup();
        adminRadioButton = new JRadioButton("Admin");
        guestRadioButton = new JRadioButton("Guest");
        add = new JButton("ADD");
        update = new JButton("UPDATE");
        delete = new JButton("DELETE");
        
        clear = new JButton("CLEAR");
    }
    
    public void startUp() {
        createTable();

        //Frame
        adminFrame.setLayout(new BorderLayout());
        adminFrame.setVisible(true);
        adminFrame.setSize(1450, 600);
        adminFrame.setLocationRelativeTo(null);
        adminFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        adminFrame.setResizable(false);
        adminFrame.add(centerPanel, BorderLayout.CENTER);
        adminFrame.add(upperPanel, BorderLayout.NORTH);

        //Panel
        upperPanel.add(title);
        upperPanel.setBackground(Color.darkGray);
        centerPanel.setLayout(null);
        centerPanel.add(scroll);
        centerPanel.add(logout);
        title.setForeground(Color.white);

        //Email
        centerPanel.add(lEmail);
        centerPanel.add(email);
        lEmail.setBounds(90, 30, 50, 50);
        email.setBounds(90, 75, 346, 30);

        //Password
        centerPanel.add(lPassword);
        centerPanel.add(password);
        lPassword.setBounds(90, 100, 80, 50);
        password.setBounds(90, 145, 350, 30);

        //Confirm Password
        centerPanel.add(lCPassword);
        centerPanel.add(cPassword);
        lCPassword.setBounds(90, 170, 120, 50);
        cPassword.setBounds(90, 215, 350, 30);

        //UserRole
        centerPanel.add(lUserRole);
        role.add(adminRadioButton);
        role.add(guestRadioButton);
        centerPanel.add(adminRadioButton);
        centerPanel.add(guestRadioButton);
        lUserRole.setBounds(90, 252, 350, 30);
        adminRadioButton.setBounds(90, 280, 80, 30);
        guestRadioButton.setBounds(175, 280, 80, 30);

        //Buttons
        centerPanel.add(add);
        add.setBounds(90, 350, 100, 50);
        centerPanel.add(delete);
        delete.setBounds(220, 350, 100, 50);
        centerPanel.add(update);
        update.setBounds(350, 351, 100, 51);
        centerPanel.add(logout);
        logout.setBounds(480, 351, 100, 51);
        
        centerPanel.add(clear);
        clear.setBounds(90, 425, 100, 51);

        //Scroll
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBounds(690, 40, 700, 450);
        scroll.setViewportView(table);
        scroll.getViewport().setBackground(Color.WHITE);
        table.setEnabled(false);
        table.getTableHeader().setReorderingAllowed(false);

        //Add ActionListeners
        logout.addActionListener(
                (e) -> {
                    logout();
                }
        );
        
        add.addActionListener(
                (e) -> {
                    addUser();
                }
        );
        
        delete.addActionListener(
                (e) -> {
                    deleteUser();
                }
        );
        
        update.addActionListener(
                (e) -> {
                    updateUser();
                }
        );
        
        clear.addActionListener(
                (e) -> {
                    clearTextField();
                }
        );
    }

    //For Logout ActionLister
    private void logout() {
        LoginScreen login = new LoginScreen();
        login.startUp();
        adminFrame.dispose();
    }

    //For Delete ActionListerner
    private void deleteUser() {
        DatabaseManager database = new DatabaseManager();
        
        String inpEmail = email.getText();
        String inpPassword = new String(password.getPassword());
        String inpCPassword = new String(cPassword.getPassword());
        String userRole = getUserRole();
        
        if (inpEmail.isEmpty()) {
            JOptionPane.showMessageDialog(centerPanel, "Please fill in the required field.", "DeleteError", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!inpPassword.isEmpty() || !inpCPassword.isEmpty() || !userRole.isEmpty()) {
            JOptionPane.showMessageDialog(centerPanel, "Use Email field only to delete.", "DeleteError", JOptionPane.WARNING_MESSAGE);
            refreshPasswordField();
            role.clearSelection();
            return;
        }
        
        try {
            if (!database.isUserDuplicate(inpEmail)) {
                JOptionPane.showMessageDialog(centerPanel, "Email not found.", "DeleteError", JOptionPane.WARNING_MESSAGE);
                database.closeConnection();
                return;
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdminScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            if (!isCurrentUser(inpEmail)) {
                database.deleteUser(inpEmail);
                refreshTable();
                refreshTextField();
                database.closeConnection();
            }
            if (isCurrentUser(inpEmail)) {
                JOptionPane.showMessageDialog(centerPanel, "Cannot delete current user", "DeleteError", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdminScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //For Add ActionListener
    private void addUser() {
        DatabaseManager database = new DatabaseManager();
        
        String inpEmail = email.getText();
        String inpPassword = new String(password.getPassword());
        String inpCPassword = new String(cPassword.getPassword());
        String userRole = getUserRole();
        
        if (inpEmail.isEmpty() || inpPassword.isEmpty() || inpCPassword.isEmpty() || userRole.isEmpty()) {
            JOptionPane.showMessageDialog(centerPanel, "Please fill in all the fields.", "AddError", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!isValidLength(inpEmail, inpPassword)) {
            JOptionPane.showMessageDialog(centerPanel, "Invalid Email / Password length", "AddError", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!isValidEmail(inpEmail)) {
            JOptionPane.showMessageDialog(centerPanel, "Invalid Email. Please use an email ending with '@gmail.com' or '@yahoo.com'.", "AddError", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            if (!database.isUserDuplicate(inpEmail)) {
                if (inpPassword.equals(inpCPassword)) {
                    database.addUser(inpEmail, inpPassword, userRole);
                    refreshTable();
                    refreshTextField();
                    database.closeConnection();
                } else {
                    JOptionPane.showMessageDialog(centerPanel, "Passwords do not match", "AddError", JOptionPane.WARNING_MESSAGE);
                    refreshPasswordField();
                }
            } else {
                JOptionPane.showMessageDialog(centerPanel, "Email already exists", "AddError", JOptionPane.WARNING_MESSAGE);
                refreshEmailField();
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdminScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //For update ActionListener
    private void updateUser() {
        DatabaseManager database = new DatabaseManager();
        
        String inpEmail = email.getText();
        String inpPassword = new String(password.getPassword());
        String inpCPassword = new String(cPassword.getPassword());
        String userRole = getUserRole();
        
        if (inpEmail.isEmpty()) {
            JOptionPane.showMessageDialog(centerPanel, "Email cannot be empty.", "UpdateError", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!isValidLength(inpEmail, inpPassword)) {
            JOptionPane.showMessageDialog(centerPanel, "Invalid Password length", "UpdateError", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            if (!database.isUserDuplicate(inpEmail)) {
                JOptionPane.showMessageDialog(centerPanel, "Email not found.", "UpdateError", JOptionPane.WARNING_MESSAGE);
                database.closeConnection();
                return;
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdminScreen.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Update(!currentUser)
        if (!isCurrentUser(inpEmail)) {

            // update Password and Role (!currentUser)
            if (!inpEmail.isEmpty() && !inpPassword.isEmpty() && !userRole.isEmpty() && !inpCPassword.isEmpty() && inpPassword.equals(inpCPassword)) {
                try {
                    database.updateUser(inpEmail, inpPassword, userRole);
                    refreshTable();
                    refreshTextField();
                    database.closeConnection();
                    return;
                } catch (SQLException ex) {
                    Logger.getLogger(AdminScreen.class.getName()).log(Level.SEVERE, null, ex);
                }
            } // update password only (!currentUser)
            else if (!inpEmail.isEmpty() && !inpPassword.isEmpty() && !inpCPassword.isEmpty() && inpPassword.equals(inpCPassword)) {
                try {
                    database.updatePasswordUser(inpEmail, inpPassword);
                    refreshTable();
                    refreshTextField();
                    database.closeConnection();
                    return;
                } catch (SQLException ex) {
                    Logger.getLogger(AdminScreen.class.getName()).log(Level.SEVERE, null, ex);
                }
            } //update role only (!currentUser)
            else if (!inpEmail.isEmpty() && !userRole.isEmpty() && inpPassword.equals(inpCPassword)) {
                try {
                    database.updateRoleUser(inpEmail, userRole);
                    refreshTable();
                    refreshTextField();
                    database.closeConnection();
                    return;
                } catch (SQLException ex) {
                    Logger.getLogger(AdminScreen.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        // update password (current user)
        if (isCurrentUser(inpEmail)) {
            if (!userRole.isEmpty()) {
                JOptionPane.showMessageDialog(centerPanel, "Cannot update current user role", "UpdateError", JOptionPane.WARNING_MESSAGE);
                role.clearSelection();
                return;
            }
            if (inpPassword.equals(inpCPassword)) {
                try {
                    database.updatePasswordUser(inpEmail, inpPassword);
                    refreshTable();
                    refreshTextField();
                    database.closeConnection();
                    return;
                } catch (SQLException ex) {
                    Logger.getLogger(AdminScreen.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        if (!inpPassword.equals(inpCPassword)) {
            JOptionPane.showMessageDialog(centerPanel, "Passwords do not match", "UpdateError", JOptionPane.WARNING_MESSAGE);
            refreshPasswordField();
        }
    }
    
    private void createTable() {
        DatabaseManager db = new DatabaseManager();
        
        String[] columnNames = {"Email", "Password", "Role"};
        
        java.util.List<String[]> columnData;
        try {
            columnData = new ArrayList<>(db.getUsersData());
            String[][] data = columnData.toArray(new String[0][]);
            table = new JTable(data, columnNames);
            scroll = new JScrollPane(table);
            
        } catch (SQLException ex) {
            Logger.getLogger(GuestScreen.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void clearTextField() {
        refreshTextField();
    }
    
    private void refreshTable() {
        DatabaseManager db = new DatabaseManager();
        try {
            java.util.List<String[]> columnData = new ArrayList<>(db.getUsersData());
            String[][] data = columnData.toArray(new String[0][]);
            DefaultTableModel model = new DefaultTableModel(data, new String[]{"Email", "Password", "Role"});
            table.setModel(model);
        } catch (SQLException ex) {
            Logger.getLogger(AdminScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void refreshTextField() {
        email.setText("");
        password.setText("");
        cPassword.setText("");
        role.clearSelection();
    }
    
    private void refreshPasswordField() {
        password.setText("");
        cPassword.setText("");
    }
    
    private void refreshEmailField() {
        email.setText("");
    }
    
    private String getUserRole() {
        if (guestRadioButton.isSelected()) {
            return "Guest";
        } else if (adminRadioButton.isSelected()) {
            return "Admin";
        } else {
            return "";
        }
    }
    
    private boolean isValidLength(String email, String password) {
        if (email.length() <= 30 && password.length() <= 20) {
            return true;
        }
        return false;
    }
    
    private boolean isValidEmail(String email) {
        if (email.endsWith("@gmail.com")) {
            return true;
        } else if (email.endsWith("@yahoo.com")) {
            return true;
        } else {
            return false;
        }
    }
    
    private boolean isCurrentUser(String email) {
        if (currentUser.getEmail().equals(email)) {
            return true;
        }
        return false;
    }
    
}
