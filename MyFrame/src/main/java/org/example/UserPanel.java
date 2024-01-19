package org.example;

import javax.swing.*;
import java.awt.*;

/**
 * Class representing user panel
 */
public class UserPanel extends JPanel {

    // SQLHelper class responsible for interaction with database
    private SQLHelper sql;
    private MainFrame mainFrame;

    private GuestRecord guest;

    private JTable reservationsTable;
    private JTable billsTable;
    private JTable bestClientsTable;

    /**
     * Class constructor
     * @param sql SQLHelper reference
     * @param mainFrame MainFrame reference
     * @param login user login
     */
    UserPanel(SQLHelper sql, MainFrame mainFrame, String login){
        this.sql =  sql;
        this.mainFrame = mainFrame;
        this.guest = sql.getGuest(login);
        initGUI();
    }

    /**
     * Creates GUI
     */
    private void initGUI(){
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        JLabel helloLabel = new JLabel("Witaj, " + (mainFrame.getIsSuperUser() ? "ADMIN" : guest));
        helloLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        this.add(helloLabel);

        String[] columnNamesReservations = {
                "L.p.",
                "ID rezerwacji",
                "ID gościa",
                "Kategoria pokoju",
                "Nazwa hotelu",
                "Miasto",
                "Zameldowanie",
                "Wymeldowanie",
                "Liczba gosci"
        };

        String[] columnNamesBills = {
                "ID rezerwacji",
                "ID gościa",
                "Nazwa hotelu",
                "Forma platnosci",
                "Suma kosztow",
                "Podstawa cenowa",
                "Liczba nocy"
        };

        String[] columnNamesBestClients = {
                "Imie",
                "Nazwisko",
                "Calkowita suma wydanych pieniedzy",
                "Calkowita ilosc pobytow",
        };

        Object[][] dataReservations;
        Object[][] dataBills;
        Object[][] dataBestClients;
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

        if(mainFrame.getIsSuperUser()){
            this.add(new JLabel("NAJLEPSI (co najmniej 4 pobyty i co najmniej wydane 2000zł) KLIENCI:"));
            dataBestClients = sql.getTableBestClientsContent();
            bestClientsTable = new JTable(dataBestClients, columnNamesBestClients);
            bestClientsTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
            bestClientsTable.setFillsViewportHeight(true);
            bestClientsTable.setDefaultEditor(Object.class, null);
            bestClientsTable.getTableHeader().setReorderingAllowed(false);
            this.add(new JScrollPane(bestClientsTable));
        }
    }
}