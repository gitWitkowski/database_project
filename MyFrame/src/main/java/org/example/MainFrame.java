package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    // navPanel buttons
    private JButton loginBtn = new JButton("ZALOGUJ");
    private JButton registerBtn = new JButton("ZAREJESTRUJ");
    private JButton searchBtn = new JButton("SZUKAJ");

    // event handlers for buttons
    ActionListener loginBtnListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("login button");
        }
    };

    ActionListener registerBtnListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("register button");
        }
    };

    ActionListener searchBtnListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("search button");
            contentPanel.setLayout(new BorderLayout());
            contentPanel.add(new SearchPanel(connection), BorderLayout.CENTER);

            contentPanel.repaint();
            contentPanel.validate();
        }
    };

    MainFrame(){
        initGUI();
        setUpActionListeners();
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

        // titlePanel
        titlePanel.setBackground(Color.black);
        titlePanel.setPreferredSize(new Dimension(70,70));
        titlePanel.setLayout(new GridLayout(1,1));
        JLabel titleText = new JLabel("HOTEL", SwingConstants.CENTER);
        titleText.setForeground(Color.white);
        titlePanel.add(titleText);

        // navPanel
        navPanel.setBackground(Color.gray);
        navPanel.setPreferredSize(new Dimension(150,150));
        navPanel.add(loginBtn);
        navPanel.add(registerBtn);
        navPanel.add(searchBtn);

        // contentPanel
//        contentPanel.setBackground(Color.darkGray);

        add(navPanel, BorderLayout.WEST);
        add(titlePanel, BorderLayout.NORTH);
        add(contentPanel);

        setVisible(true);
    }

    private void setUpActionListeners(){
        loginBtn.addActionListener(loginBtnListener);
        registerBtn.addActionListener(registerBtnListener);
        searchBtn.addActionListener(searchBtnListener);
    }
}