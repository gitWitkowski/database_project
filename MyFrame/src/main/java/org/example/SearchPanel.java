package org.example;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.optionalusertools.DateChangeListener;
import com.github.lgooddatepicker.optionalusertools.DateHighlightPolicy;
import com.github.lgooddatepicker.optionalusertools.DateVetoPolicy;
import com.github.lgooddatepicker.zinternaltools.DateChangeEvent;
import com.github.lgooddatepicker.zinternaltools.HighlightInformation;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

// class representing search panel
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
    private JComboBox<String> roomCatList;

    // label and spinner for number of guests
    private SpinnerNumberModel model = new SpinnerNumberModel(1, 1, 9.0, 1.0);
    private JLabel numOfGuestLabel = new JLabel("Ilosc gosci:");
    private JSpinner numOfGuestSpinner = new JSpinner(model);

    /*
    *  experimental
    */
    private DateChangeListener dtchng = new DateChangeListener() {
        @Override
        public void dateChanged(DateChangeEvent dateChangeEvent) {
            updateVetoPolicy();
        }
    };

    /*
     *  experimental
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

    SearchPanel(SQLHelper sql, MainFrame mainFrame){
        this.sql = sql;
        this.mainFrame = mainFrame;
        initGUI();
    }

    // init GUI for the search panel
    private void initGUI(){
        cityList = new JComboBox(sql.getHotels());
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
            @Override
            public boolean isDateAllowed(LocalDate localDate) {
                if(localDate.isBefore(LocalDate.now()))
                    return false;
                return true;
            }
        });

        // constraints for elements in grid
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(30,30,20,20);

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
    }
}
