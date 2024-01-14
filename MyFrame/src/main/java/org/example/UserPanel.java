package org.example;

import javax.swing.*;
import java.awt.*;

public class UserPanel extends JPanel {

    // SQLHelper class responsible for interaction with database
    private SQLHelper sql;
    private MainFrame mainFrame;

    private GuestRecord guest;

    private JTable reservationsTable;
    private JTable billsTable;

    UserPanel(SQLHelper sql, MainFrame mainFrame, String login){
        this.sql =  sql;
        this.mainFrame = mainFrame;
        this.guest = sql.getGuest(login);
        initGUI();
    }

    private void initGUI(){
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        if(mainFrame.getIsSuperUser())
            this.add(new JLabel("Witaj ADMIN"));
        else
            this.add(new JLabel("Witaj, " + guest));

        String[] columnNamesReservations = {
                "L.p.",
                "ID rezerwacji",
                "Kategoria pokoju",
                "Nazwa hotelu",
                "Miasto",
                "Zameldowanie",
                "Wymeldowanie",
                "Liczba gosci"
        };

        String[] columnNamesBills = {
                "ID rezerwacji",
                "Nazwa hotelu",
                "Forma platnosci",
                "Suma kosztow",
                "Podstawa cenowa",
                "Liczba nocy"
        };

        Object[][] dataReservations;
        Object[][] dataBills;
        if(mainFrame.getIsSuperUser()){
            dataReservations = sql.getTableReservationsContent();
            dataBills = sql.getTableBillsContent();
        }else{
            dataReservations = sql.getTableReservationsContent(guest.guestId());
            dataBills = sql.getTableBillsContent(guest.guestId());
        }

        reservationsTable = new JTable(dataReservations, columnNamesReservations);
        reservationsTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
        reservationsTable.setFillsViewportHeight(true);
        reservationsTable.setDefaultEditor(Object.class, null);
        reservationsTable.getTableHeader().setReorderingAllowed(false);
        this.add(new JLabel("REZERWACJE:"));
        this.add(new JScrollPane(reservationsTable));

        this.add(new JLabel("RACHUNKI:"));
        billsTable = new JTable(dataBills, columnNamesBills);
        billsTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
        billsTable.setFillsViewportHeight(true);
        billsTable.setDefaultEditor(Object.class, null);
        billsTable.getTableHeader().setReorderingAllowed(false);
        this.add(new JScrollPane(billsTable));
    }
}