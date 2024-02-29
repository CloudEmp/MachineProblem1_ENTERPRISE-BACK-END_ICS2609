/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package crud_machineprob1;

/**
 *
 * @author Blade Emperor
 */
import java.awt.BorderLayout;
import javax.swing.*;
import java.sql.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GuestScreen {

    private JFrame guestFrame;
    private JPanel centerPanel;
    private JPanel upperPanel;
    private JButton logout;
    private JTable table;
    private JScrollPane scroll;
    private JLabel title;

    public GuestScreen() {
        guestFrame = new JFrame("GuestScreen");
        centerPanel = new JPanel();
        upperPanel = new JPanel();
        logout = new JButton("Logout");
        title = new JLabel("MP 1 (Guest)");

    }

    public void startUp() {
        createTable();

        guestFrame.setLayout(new BorderLayout());
        guestFrame.setVisible(true);
        guestFrame.setSize(920, 600);
        guestFrame.setLocationRelativeTo(null);
        guestFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        guestFrame.setResizable(false);
        centerPanel.setLayout(null);

        guestFrame.add(centerPanel, BorderLayout.CENTER);
        guestFrame.add(upperPanel, BorderLayout.NORTH);
        centerPanel.add(scroll);
        centerPanel.add(logout);
        upperPanel.add(title);

        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBounds(58, 40, 800, 350);
        scroll.setViewportView(table);
        scroll.getViewport().setBackground(Color.WHITE);
        logout.setBounds(320, 430, 250, 50);
        upperPanel.setBackground(Color.darkGray);
        title.setForeground(Color.white);
        table.getTableHeader().setReorderingAllowed(false); // makes header unmovable

        table.setEnabled(false);

        //Add ActionListeners
        logout.addActionListener(
                (e) -> {
                    logout();
                }
        );
    }

    private void createTable() {
        DatabaseManager db = new DatabaseManager();

        String[] columnNames = {"Email", "Password", "Role"};

        List<String[]> columnData;
        try {
            columnData = new ArrayList<>(db.getUsersData());
            String[][] data = columnData.toArray(new String[0][]);
            table = new JTable(data, columnNames);
            
            scroll = new JScrollPane(table);
        } catch (SQLException ex) {
            Logger.getLogger(GuestScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void logout() {
        LoginScreen login = new LoginScreen();
        login.startUp();
        guestFrame.dispose();
    }

}
