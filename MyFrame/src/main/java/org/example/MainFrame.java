package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

/**
 * Main Frame of the App
 */
public class MainFrame extends JFrame {

    // SQLHelper class responsible for interaction with database
    private final SQLHelper sql;

    // reference to itself
    private final MainFrame that = this;

    private boolean isLogged = false;
    private boolean isSuperUser = false;

    private GuestRecord currentUser;

    /**
     * Updates current user
     * @param gr current user to be set
     */
    protected void setCurrentUser(GuestRecord gr){
        this.currentUser = gr;
    }

    /**
     * Getter for curent user
     */
    protected GuestRecord getCurrentUser(){
        return currentUser;
    }

    // GUI is build with these 3 JPanels
    private final JPanel titlePanel = new JPanel();
    private final JPanel navPanel = new JPanel();
    protected final JPanel contentPanel = new JPanel();

    // navPanel buttons
    private JButton loginBtn = new JButton("LOGOWANIE");
    private JButton registerBtn = new JButton("REJESTRACJA");
    private JButton logoutBtn = new JButton("WYLOGUJ");
    private JButton searchBtn = new JButton("ZAREZERWUJ TERMIN");
    private JButton viewBtn = new JButton("PRZEGLADAJ OFERTY");
    private JButton userPanelBtn = new JButton("PANEL UZYTKOWNIKA");
    protected JButton resetBtn = new JButton("RESET BAZY");

    // event handlers for buttons
    private ActionListener loginBtnListener = new ActionListener() {
        /**
         * Renders login panel
         * @param e the event to be processed
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("login button");
            changePanel(new LoginPanel(sql, that));
        }
    };

    private ActionListener registerBtnListener = new ActionListener() {
        /**
         * Renders register panel
         * @param e the event to be processed
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("register button");
            changePanel(new RegisterPanel(sql, that));
        }
    };

    private ActionListener logoutBtnListener = new ActionListener() {
        /**
         * Logouts current user
         * @param e the event to be processed
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            logoutUser();
            if(!isLogged){
                JOptionPane.showMessageDialog(null, "Uzytkownik wylogowany poprawnie", "Uwaga", JOptionPane.INFORMATION_MESSAGE);
                changePanel(new LoginPanel(sql, that));
            }
        }
    };

    private ActionListener searchBtnListener = new ActionListener() {
        /**
         * Renders search panel
         * @param e the event to be processed
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("search button");
            changePanel(new SearchPanel(sql, that));
        }
    };

    private ActionListener viewBtnListener = new ActionListener() {
        /**
         * Renders hotels panel
         * @param e the event to be processed
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("offers button");
            changePanel(new HotelsPanel(sql, that));
        }
    };

    private ActionListener userPanelBtnListener = new ActionListener() {
        /**
         * Renders user panel
         * @param e the event to be processed
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if(isSuperUser)
                changePanel(new UserPanel(sql, that,"admin"));
            else
                changePanel(new UserPanel(sql, that, currentUser.login()));
        }
    };

    private ActionListener resetBtnListener = new ActionListener() {
        /**
         * Resets database
         * @param e the event to be processed
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("RESET");
            sql.resetDB();
        }
    };

    /**
     * Changes content of the content panel
     * @param panel panel to be rendered in content panel
     */
    protected void changePanel(JPanel panel){
        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.repaint();
        contentPanel.validate();
    }

    /**
     * Class constructor
     */
    MainFrame(){
        sql = new SQLHelper(
                "jdbc:postgresql://trumpet.db.elephantsql.com/cipfxjrs",
                "cipfxjrs",
                "msppY-QYb7-1JgUShwzQspSx6KrlG_Ps");
        initGUI();
        setUpActionListeners();
    }

    /**
     * Creates starting GUI
     */
    private void initGUI(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(800, 600));
        setSize(new Dimension(800, 600));
        setLayout(new BorderLayout());

        // titlePanel
        titlePanel.setBackground(Color.black);
        titlePanel.setPreferredSize(new Dimension(70,70));
        titlePanel.setLayout(new GridLayout(1,1));
        JLabel titleText = new JLabel("SERWIS ZARZĄDZANIA REZERWACJAMI HOTELOWYMI", SwingConstants.CENTER);
        titleText.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 25));
        titleText.setForeground(Color.white);
        titlePanel.add(titleText);

        // navPanel
        navPanel.setBackground(Color.gray);
        navPanel.setPreferredSize(new Dimension(200,150));
        navPanel.add(loginBtn);
        navPanel.add(registerBtn);
        navPanel.add(logoutBtn);
        navPanel.add(searchBtn);
        navPanel.add(viewBtn);
        navPanel.add(userPanelBtn);
        navPanel.add(resetBtn);

        logoutBtn.setVisible(false);
        userPanelBtn.setVisible(false);
        resetBtn.setVisible(false);

        // contentPanel
        contentPanel.setLayout(new BorderLayout());

        add(navPanel, BorderLayout.WEST);
        add(titlePanel, BorderLayout.NORTH);
        add(contentPanel);

        changePanel(new LoginPanel(sql, this));

        setVisible(true);
    }

    /**
     * Sets all action listeners
     */
    private void setUpActionListeners(){
        loginBtn.addActionListener(loginBtnListener);
        registerBtn.addActionListener(registerBtnListener);
        searchBtn.addActionListener(searchBtnListener);
        viewBtn.addActionListener(viewBtnListener);
        logoutBtn.addActionListener(logoutBtnListener);
        userPanelBtn.addActionListener(userPanelBtnListener);
        resetBtn.addActionListener(resetBtnListener);
    }

    /**
     * Logs out current user
     */
    private void logoutUser(){
        if(getIsLogged()){
            loginBtn.setVisible(true);
            registerBtn.setVisible(true);
            logoutBtn.setVisible(false);
            userPanelBtn.setVisible(false);
            resetBtn.setVisible(false);
            isLogged = false;
        }
    }

    /**
     * Logs in new user
     */
    protected void loginUser(){
        if(!getIsLogged()){
            loginBtn.setVisible(false);
            registerBtn.setVisible(false);
            logoutBtn.setVisible(true);
            userPanelBtn.setVisible(true);
            isLogged = true;
        }
    }

    /**
     * Check if user is logged
     */
    protected boolean getIsLogged(){
        return isLogged;
    }

    /**
     * Setter for isLogged
     * @param val sets whether user is logged
     */
    protected void setLogged(boolean val){
        isLogged = val;
    }

    /**
     * Checks if logged user is super user
     */
    protected boolean getIsSuperUser(){
        return isSuperUser;
    }

    /**
     * Setter for super user
     * @param val sets whether user is superuser
     */
    protected void setSuperUser(boolean val){
        isSuperUser = val;
    }
}