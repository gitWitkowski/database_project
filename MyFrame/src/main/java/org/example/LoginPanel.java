package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class representing logging panel
 */
public class LoginPanel extends JPanel {

    private SQLHelper sql;
    private MainFrame mainFrame;

    private JLabel loginLabel = new JLabel("Login:");
    private JTextField loginField = new JTextField();

    private JLabel passwordLabel = new JLabel("Haslo:");
    private JPasswordField passwordField = new JPasswordField();

    private JButton loginBtn = new JButton("Zaloguj");

    private ActionListener loginBtnListener = new ActionListener() {
        /**
         * Checks users credentials and if correct, changes view to logged
         * @param e the event to be processed
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if(sql.checkUserCredentials(loginField.getText(), passwordField.getPassword())[0]){
                mainFrame.loginUser();
                if(mainFrame.getIsLogged()){
                    mainFrame.setSuperUser(sql.checkUserCredentials(loginField.getText(), passwordField.getPassword())[1]);
                    mainFrame.setCurrentUser(sql.getGuest(loginField.getText()));
                    mainFrame.changePanel(new UserPanel(sql, mainFrame, mainFrame.getCurrentUser().login()));
                    if(mainFrame.getIsSuperUser())
                        mainFrame.resetBtn.setVisible(true);
                }
            }else{
                JOptionPane.showMessageDialog(null, "Niewlasciwy login lub haslo", "BLAD", JOptionPane.ERROR_MESSAGE);
            }

        }
    };

    /**
     * Class constructor
     * @param sql SQLHelper reference
     * @param mainFrame MainFrame reference
     */
    LoginPanel(SQLHelper sql, MainFrame mainFrame){
        this.sql = sql;
        this.mainFrame = mainFrame;
        initGUI();
    }

    /**
     * Creates GUI
     */
    private void initGUI(){
        this.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(30,30,20,20);


        loginField.setColumns(10);
        passwordField.setColumns(10);
        loginBtn.addActionListener(loginBtnListener);

        gbc.gridx = 0;
        gbc.gridy = 0;

        this.add(loginLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;

        this.add(loginField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;

        this.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;

        this.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;

        this.add(loginBtn, gbc);
    }
}
