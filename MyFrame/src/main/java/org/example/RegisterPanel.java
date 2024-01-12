package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterPanel extends JPanel {

    private SQLHelper sql;
    private MainFrame mainFrame;

    private JLabel loginLabel = new JLabel("Login:");
    private JTextField loginField = new JTextField();

    private JLabel emailLabel = new JLabel("Adres email:");
    private JTextField emailField = new JTextField();

    private JLabel fnameLabel = new JLabel("Imie:");
    private JTextField fnameField = new JTextField();

    private JLabel lnameLabel = new JLabel("Nazwisko:");
    private JTextField lnameField = new JTextField();

    private JLabel peselLabel = new JLabel("PESEL:");
    private JTextField peselField = new JTextField();

    private JLabel phoneLabel = new JLabel("Nr telefonu:");
    private JTextField phoneField = new JTextField();

    private JLabel passwordLabel = new JLabel("Haslo:");
    private JPasswordField passwordField = new JPasswordField();

    private JLabel passwordLabel2 = new JLabel("Powtorz haslo:");
    private JPasswordField passwordField2 = new JPasswordField();

    private JButton registerBtn = new JButton("Zarejestruj konto");

    private ActionListener registerBtnListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Rejestruj");
        }
    };

    RegisterPanel(SQLHelper sql, MainFrame mainFrame){
        this.sql = sql;
        this.mainFrame = mainFrame;
        initGUI();
    }

    private void initGUI(){
        this.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);

        loginField.setColumns(10);
        Font f = new Font(Font.SANS_SERIF, Font.PLAIN, 30);
        loginLabel.setFont(f);
        emailField.setColumns(10);
        passwordField.setColumns(10);
        passwordField2.setColumns(10);
        peselField.setColumns(10);
        lnameField.setColumns(10);
        fnameField.setColumns(10);
        phoneField.setColumns(10);
        registerBtn.addActionListener(registerBtnListener);

        gbc.gridx = 0;
        gbc.gridy = 0;

        this.add(loginLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;

        this.add(loginField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;

        this.add(emailLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;

        this.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;

        this.add(fnameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;

        this.add(fnameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;

        this.add(lnameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;

        this.add(lnameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;

        this.add(peselLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;

        this.add(peselField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;

        this.add(phoneLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;

        this.add(phoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;

        this.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;

        this.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;

        this.add(passwordLabel2, gbc);

        gbc.gridx = 1;
        gbc.gridy = 7;

        this.add(passwordField2, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;

        this.add(registerBtn, gbc);
    }
}
