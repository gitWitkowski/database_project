package org.example;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.optionalusertools.DateChangeListener;
import com.github.lgooddatepicker.optionalusertools.DateVetoPolicy;
import com.github.lgooddatepicker.zinternaltools.DateChangeEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

/**
 * Class representing search panel
 */
public class SearchPanel extends JPanel {

    // SQLHelper class responsible for interaction with database
    private SQLHelper sql;
    private MainFrame mainFrame;

    // label and combo box for cities
    private JLabel cityLabel = new JLabel("Miasto:");
    private JComboBox<String> cityList;

    // label and date picker for start date
    private JLabel startDateLabel = new JLabel("Poczatek pobytu:");
    private DatePickerSettings datePickerSettings = new DatePickerSettings();
    private DatePicker startDate = new DatePicker(datePickerSettings);

    // label and date picker for end date
    private JLabel endDateLabel = new JLabel("Koniec pobytu:");
    private DatePickerSettings datePickerSettings2 = new DatePickerSettings();
    private DatePicker endDate = new DatePicker(datePickerSettings2);

    // label and combo box for room type
    private JLabel roomCatLabel = new JLabel("Rodzaj pokoju:");
    private JComboBox<RoomCategoryRecord> roomCatList;

    // label and spinner for number of guests
    private SpinnerNumberModel model = new SpinnerNumberModel(1, 1, 9.0, 1.0);
    private JLabel numOfGuestLabel = new JLabel("Ilosc gosci:");
    private JSpinner numOfGuestSpinner = new JSpinner(model);

    // button for searching offers
    private JButton btnSearch = new JButton("Szukaj ofert");

    // action listener for btnSearch
    private ActionListener btnSearchListener = new ActionListener() {
        /**
         * Searches for available rooms with given conditions
         * @param e the event to be processed
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            start = startDate.getDate();
            end = endDate.getDate();
            city = String.valueOf(cityList.getSelectedItem());
            cat_id = ((RoomCategoryRecord)roomCatList.getSelectedItem()).id();
            numOfPeople = ((Double)numOfGuestSpinner.getValue()).intValue();
//            System.out.println(start);
//            System.out.println(end);
//            System.out.println(city);
//            System.out.println(cat_id);
//            System.out.println(numOfPeople);
            RoomRecord[] tab = sql.getFreeRooms(city, start, end,cat_id,numOfPeople );
            mainFrame.changePanel(new RoomsPanel(sql, mainFrame, tab, start, end, numOfPeople));
        }
    };

    // variables storing searching details
    private LocalDate start = null;
    private LocalDate end = null;
    private String city = "";
    private int cat_id;
    private  int numOfPeople;

    /*
    *  experimental
    */
    private DateChangeListener dtchng = new DateChangeListener() {
        /**
         * Updates DatePicker veto policy
         * @param dateChangeEvent
         */
        @Override
        public void dateChanged(DateChangeEvent dateChangeEvent) {
            updateVetoPolicy();
        }
    };

    /**
     * DatePicker veto policy
     */
    private void updateVetoPolicy(){
        datePickerSettings2.setVetoPolicy(new DateVetoPolicy() {
            LocalDate temp = startDate.getDate();
            @Override
            public boolean isDateAllowed(LocalDate localDate) {
                if(temp==null) return true;
                if(localDate.isBefore(temp.plusDays(1)))
                    return false;
                return true;
            }
        });
    }

    /**
     * Class constructor
     * @param sql SQLHelper reference
     * @param mainFrame MainFrame reference
     */
    SearchPanel(SQLHelper sql, MainFrame mainFrame){
        this.sql = sql;
        this.mainFrame = mainFrame;
        initGUI();
    }

    /**
     * Creates GUI for the search panel
     */
    private void initGUI(){
        cityList = new JComboBox(sql.getCities());
        roomCatList = new JComboBox(sql.getRoomCat());

        this.setLayout(new GridBagLayout());

        numOfGuestSpinner.setPreferredSize(new Dimension(50,30));

        // non-editable TextField in JSpinner
        ((JSpinner.DefaultEditor) numOfGuestSpinner.getEditor()).getTextField().setEditable(false);

        /*
         *  experimental
         */
        startDate.addDateChangeListener(dtchng);

        datePickerSettings.setVetoPolicy(new DateVetoPolicy() {
            /**
             * Allow only future dates
             * @param localDate date from calendar
             * @return
             */
            @Override
            public boolean isDateAllowed(LocalDate localDate) {
                if(localDate.isBefore(LocalDate.now()))
                    return false;
                return true;
            }
        });

        // constraints for elements in grid
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);

        // elements and their position on grid
        gbc.gridx = 0;
        gbc.gridy = 0;

        this.add(cityLabel,gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;

        this.add(cityList,gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;

        this.add(startDateLabel,gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;

        this.add(startDate, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;

        this.add(endDateLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;

        this.add(endDate, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;

        this.add(roomCatLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;

        this.add(roomCatList, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;

        this.add(numOfGuestLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;

        this.add(numOfGuestSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;

        this.add(btnSearch, gbc);

        btnSearch.addActionListener(btnSearchListener);
    }
}
