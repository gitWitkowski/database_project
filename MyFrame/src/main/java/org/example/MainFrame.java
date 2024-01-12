package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

// main Frame of the App
public class MainFrame extends JFrame {

    // SQLHelper class responsible for interaction with database
    private final SQLHelper sql;

    // reference to itself
    private final MainFrame that = this;

    // GUI is build with these 3 JPanels
    private final JPanel titlePanel = new JPanel();
    private final JPanel navPanel = new JPanel();
    protected final JPanel contentPanel = new JPanel();

    // navPanel buttons
    private JButton loginBtn = new JButton("LOGOWANIE");
    private JButton registerBtn = new JButton("REJESTRACJA");
    private JButton searchBtn = new JButton("ZAREZERWUJ TERMIN");
    private JButton viewBtn = new JButton("PRZEGLADAJ OFERTY");

    // event handlers for buttons
    private ActionListener loginBtnListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("login button");
            changePanel(new LoginPanel(sql, that));
        }
    };

    private ActionListener registerBtnListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("register button");
            changePanel(new RegisterPanel(sql, that));
        }
    };

    private ActionListener searchBtnListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("search button");
            changePanel(new SearchPanel(sql, that));
        }
    };

    private ActionListener viewBtnListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("offers button");
            changePanel(new HotelsPanel(sql, that));
        }
    };

    protected void changePanel(JPanel panel){
        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.repaint();
        contentPanel.validate();
    }

    MainFrame(){
        sql = new SQLHelper(
                "jdbc:postgresql://trumpet.db.elephantsql.com/cipfxjrs",
                "cipfxjrs",
                "msppY-QYb7-1JgUShwzQspSx6KrlG_Ps");
        initGUI();
        setUpActionListeners();
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
        navPanel.setPreferredSize(new Dimension(200,150));
        navPanel.add(loginBtn);
        navPanel.add(registerBtn);
        navPanel.add(searchBtn);
        navPanel.add(viewBtn);

        // contentPanel
        contentPanel.setLayout(new BorderLayout());

        add(navPanel, BorderLayout.WEST);
        add(titlePanel, BorderLayout.NORTH);
        add(contentPanel);

        setVisible(true);
    }

    private void setUpActionListeners(){
        loginBtn.addActionListener(loginBtnListener);
        registerBtn.addActionListener(registerBtnListener);
        searchBtn.addActionListener(searchBtnListener);
        viewBtn.addActionListener(viewBtnListener);
    }
}