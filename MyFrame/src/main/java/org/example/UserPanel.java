package org.example;

import javax.swing.*;
import java.awt.*;

public class UserPanel extends JPanel {

    // SQLHelper class responsible for interaction with database
    private SQLHelper sql;
    private MainFrame mainFrame;

    private GuestRecord guest;

    UserPanel(SQLHelper sql, MainFrame mainFrame, String login){
        this.sql =  sql;
        this.mainFrame = mainFrame;
        this.guest = sql.getGuest(login);
        initGUI();
    }

    private void initGUI(){
        this.setLayout(new BorderLayout());
        if(mainFrame.getIsSuperUser())
            this.add(new JLabel("Witaj ADMIN"), BorderLayout.NORTH);
        else
            this.add(new JLabel("Witaj, " + guest), BorderLayout.NORTH);
    }

}
