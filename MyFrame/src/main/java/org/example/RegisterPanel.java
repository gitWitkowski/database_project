package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

/**
 * Class represetning register panel
 */
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
        /**
         * Register new user
         * @param e the event to be processed
         */
        @Override
        public void actionPerformed(ActionEvent e) {
//            System.out.println("Rejestruj");
//            System.out.println(loginField.getText());
//            System.out.println(passwordField.getText());
//            System.out.println(emailField.getText());
//            System.out.println(fnameField.getText());
//            System.out.println(lnameField.getText());
//            System.out.println(peselField.getText());
//            System.out.println(phoneField.getText());
            if(fnameField.getText().equals("") ||
                    lnameField.getText().equals("") ||
                    peselField.getText().equals("") ||
                    phoneField.getText().equals("")
            ){
                JOptionPane.showMessageDialog(null, "Uzupelnij wszystkie pola!", "BLAD", JOptionPane.ERROR_MESSAGE);
            }else{
                if(Arrays.equals(passwordField.getPassword(), passwordField2.getPassword())){
                    int response = sql.registerUser(
                            loginField.getText(),
                            passwordField.getPassword(),
                            emailField.getText(),
                            fnameField.getText(),
                            lnameField.getText(),
                            peselField.getText(),
                            phoneField.getText()
                    );
                    switch (response){
                        case 0:
                            System.out.println("dodano uzytkownika");
                            JOptionPane.showMessageDialog(null, "Zarejestrowano nowego uzytkownika", "SUKCES", JOptionPane.PLAIN_MESSAGE);
                            mainFrame.changePanel(new LoginPanel(sql,mainFrame));
                            break;

                        case 1:
                            JOptionPane.showMessageDialog(null, "Istnieje juz uzytkownik posiadajacy taki login", "BLAD", JOptionPane.ERROR_MESSAGE);
                            break;

                        case 2:
                            JOptionPane.showMessageDialog(null, "Istnieje juz uzytkownik o podanym adresie email", "BLAD", JOptionPane.ERROR_MESSAGE);
                            break;

                        case 3:
//                        JOptionPane.showMessageDialog(null, "Wystapil blad przy dodawaniu rekordu", "BLAD", JOptionPane.ERROR_MESSAGE);
                            break;

                        default:
                            JOptionPane.showMessageDialog(null, "Wystapil nieznany problem", "BLAD", JOptionPane.ERROR_MESSAGE);
                            break;
                    }
                }
                else
                    JOptionPane.showMessageDialog(null, "Podane hasla nie sa identyczne", "BLAD", JOptionPane.ERROR_MESSAGE);
            }
        }
    };

    /**
     * Class constructor
     * @param sql SQLHelper reference
     * @param mainFrame MainFrame reference
     */
    RegisterPanel(SQLHelper sql, MainFrame mainFrame){
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
