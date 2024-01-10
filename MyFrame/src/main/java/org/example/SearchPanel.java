package org.example;

import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

// class representing search panel
public class SearchPanel extends JPanel {

    // connection established in MainFrame
    private Connection connection;

    // label and combo box for cities
    private JLabel cityLabel = new JLabel("Miasto:");
    private JComboBox cityList = new JComboBox();

    // label and date picker for start date
    private JLabel startDateLabel = new JLabel("Poczatek pobytu:");
    private DatePicker startDate = new DatePicker();

    // label and date picker for end date
    private JLabel endDateLabel = new JLabel("Koniec pobytu:");
    private DatePicker endDate = new DatePicker();

    // label and combo box for room type
    private JLabel roomCatLabel = new JLabel("Rodzaj pokoju:");
    private JComboBox roomCatList = new JComboBox();

    // label and spinner for number of guests
    private SpinnerNumberModel model = new SpinnerNumberModel(1, 1, 9.0, 1.0);
    private JLabel numOfGuestLabel = new JLabel("Ilosc gosci:");
    private JSpinner numOfGuestSpinner = new JSpinner(model);


    SearchPanel(Connection connection){
        this.connection = connection;
        this.setLayout(new GridBagLayout());

        numOfGuestSpinner.setPreferredSize(new Dimension(50,30));

        // non-editable TextField in JSpinner
        ((JSpinner.DefaultEditor) numOfGuestSpinner.getEditor()).getTextField().setEditable(false);

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
