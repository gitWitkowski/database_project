package org.example;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.optionalusertools.DateVetoPolicy;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Class representing room offer panel
 */
public class RoomOfferPanel extends JPanel {

    // SQLHelper class responsible for interaction with database
    private SQLHelper sql;

    // reference to MainFrame
    private MainFrame mainFrame;

    private int roomId;

    // label and date picker for start date
    private JLabel startDateLabel = new JLabel("Poczatek pobytu:");
    private DatePickerSettings datePickerSettings = new DatePickerSettings();
    private DatePicker startDate = new DatePicker(datePickerSettings);

    // label and date picker for end date
    private JLabel endDateLabel = new JLabel("Koniec pobytu:");
    private DatePickerSettings datePickerSettings2 = new DatePickerSettings();
    private DatePicker endDate = new DatePicker(datePickerSettings2);

    // label and spinner for number of guests
    private SpinnerNumberModel model = new SpinnerNumberModel(1, 1, 9, 1);
    private JLabel numOfGuestLabel = new JLabel("Ilosc gosci:");
    private JSpinner numOfGuestSpinner = new JSpinner(model);

    // button for confirming reservation
    private JButton makeReservationBtn = new JButton("DOKONAJ REZERWACJI");

    private ActionListener makeReservationListener = new ActionListener() {
        /**
         * Makes reservation with set parameters
         * @param e the event to be processed
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            // to do
            // add record to database
            if(mainFrame.getIsLogged()){
                System.out.println(roomId);
                System.out.println(mainFrame.getCurrentUser().guestId());
                if(startDate.getDate() != null && endDate.getDate() != null) {
                    boolean addedRecord = sql.makeReservation(roomId, mainFrame.getCurrentUser().guestId(), startDate.getDate(),
                            endDate.getDate(), (Integer) numOfGuestSpinner.getValue());
                    if(addedRecord){
                        JOptionPane.showMessageDialog(null, "Dokonano rezerwacji", "Uwaga", JOptionPane.INFORMATION_MESSAGE);
                        mainFrame.changePanel(new UserPanel(sql, mainFrame, mainFrame.getCurrentUser().login()));
                    }else
                        JOptionPane.showMessageDialog(null, "Rezerwacja nie powiodla sie", "Uwaga", JOptionPane.INFORMATION_MESSAGE);
                }else{
                    JOptionPane.showMessageDialog(null, "Data nie może być pusta", "Uwaga", JOptionPane.INFORMATION_MESSAGE);
                }
            }else{
                JOptionPane.showMessageDialog(null, "Aby dokonac rezerwacji, zaloguj sie lub zarejestruj", "Uwaga", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    };

    RoomOfferPanel(SQLHelper sql, MainFrame mainFrame, int room_id){
        this.sql = sql;
        this.mainFrame = mainFrame;
        this.roomId = room_id;
        initGUI();
    }

    /**
     * Class constructor
     * @param sql SQLHelper reference
     * @param mainFrame MainFrame reference
     * @param room_id room ID
     * @param start star date
     * @param end end date
     * @param numOfGuests number of guests
     */
    RoomOfferPanel(SQLHelper sql, MainFrame mainFrame, int room_id, LocalDate start, LocalDate end, int numOfGuests){
        this.sql = sql;
        this.mainFrame = mainFrame;
        this.roomId = room_id;
        if(start!=null && end!= null){
            this.startDate.setDate(start);
            this.endDate.setDate(end);
            this.numOfGuestSpinner.setValue(numOfGuests);
        }
        initGUI();
    }

    /**
     * Create GUI
     */
    private void initGUI(){
        this.add(startDateLabel);
        this.add(startDate);

        this.add(endDateLabel);
        this.add(endDate);

        this.add(numOfGuestLabel);
        this.add(numOfGuestSpinner);

        this.add(makeReservationBtn);
        makeReservationBtn.addActionListener(makeReservationListener);

        disableReservedDateRanges();
    }

    /**
     * Disable all dates in DatePicker for which reservations have been already made
     */
    private void disableReservedDateRanges(){
        DateVetoPolicy alreadyReservedPolicy = new DateVetoPolicy() {
            List<LocalDate[]> alreadyReservedDates =  sql.getAllReservations(roomId);
            @Override
            public boolean isDateAllowed(LocalDate localDate) {
                for(var pair : alreadyReservedDates){
                    // pair = { start, end }
                    LocalDate start = pair[0];
                    LocalDate end = pair[1];
                    if(localDate.isAfter(start) && localDate.isBefore(end))
                        return false;
                    if(localDate.equals(start) || localDate.equals(end))
                        return false;
                }
                if(localDate.isBefore(LocalDate.now()))
                    return false;
                return true;
            }
        };
        datePickerSettings.setVetoPolicy(alreadyReservedPolicy);
        datePickerSettings2.setVetoPolicy(alreadyReservedPolicy);
    }
}
