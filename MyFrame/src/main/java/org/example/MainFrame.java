package org.example;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

// main Frame of the App
public class MainFrame extends JFrame {

    // variable for connection with database
    private Connection connection = null;

    {
        getConnection();
    }

    // GUI is build with these 3 JPanels
    private final JPanel titlePanel = new JPanel();
    private final JPanel navPanel = new JPanel();
    private final JPanel contentPanel = new JPanel();

    MainFrame(){
        initGUI();
    }

    // connect to database
    private void getConnection(){
        String dbaseURL = "jdbc:postgresql://trumpet.db.elephantsql.com/cipfxjrs";
        String username  = "cipfxjrs";
        String password  = "msppY-QYb7-1JgUShwzQspSx6KrlG_Ps";
        try {
            connection = DriverManager.getConnection(dbaseURL, username, password);
            System.out.println("Successfully Connected to the database!");
        } catch (SQLException e) {
            System.out.println("Could not connect to the database " + e.getMessage());
        }
    }

    // create starting GUI
    private void initGUI(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(800, 600));
        setSize(new Dimension(800, 600));
        setLayout(new BorderLayout());

        titlePanel.setBackground(Color.black);
        titlePanel.setPreferredSize(new Dimension(70,70));
        titlePanel.setLayout(new GridLayout(1,1));
        JLabel titleText = new JLabel("HOTEL", SwingConstants.CENTER);
        titleText.setForeground(Color.white);
        titlePanel.add(titleText);

        navPanel.setBackground(Color.gray);
        navPanel.setPreferredSize(new Dimension(150,150));

        contentPanel.setBackground(Color.darkGray);

        add(navPanel, BorderLayout.WEST);
        add(titlePanel, BorderLayout.NORTH);
        add(contentPanel);

        setVisible(true);
    }
}