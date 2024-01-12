package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

// panel displaying all hotels from database
public class HotelsPanel extends JPanel {

    // SQLHelper class responsible for interaction with database
    private SQLHelper sql;
    // reference to MainFrame
    private MainFrame mainFrame;

    // JPanel inside of JScrollPane
    private JPanel contentPanel = new JPanel();
    private JScrollPane scrollPane = new JScrollPane(contentPanel);

    HotelsPanel(SQLHelper sql, MainFrame mainFrame){
        this.sql = sql;
        this.mainFrame = mainFrame;
        initGUI();
    }

    // inner class representing single hotel cell on the displayed list
    private class HotelCell extends JPanel implements MouseListener {

        // hotel record
        protected Hotel hotel;

        // necessary labels providing information
        protected JLabel hotelName;
        protected JLabel city;
        protected JLabel rating;

        HotelCell(Hotel hotel) {
            this.hotel = hotel;
            this.hotelName = new JLabel(hotel.name());
            this.city = new JLabel(hotel.city());
            this.rating = new JLabel(""+hotel.rating());
            this.initGUI();
        }

        // create single cell
        void initGUI(){
            this.add(hotelName);
            this.add(city);
            this.add(rating);
            this.setBackground(Color.PINK);
            this.setBorder(BorderFactory.createLineBorder(Color.black));
            this.addMouseListener(this);
        }

        // clicking on a cell results in opening panel with rooms from a given hotel
        @Override
        public void mouseClicked(MouseEvent e) {
            mainFrame.changePanel(new RoomsPanel(sql, mainFrame, hotel.hotel_id()));
        }
        @Override
        public void mousePressed(MouseEvent e) {}
        @Override
        public void mouseReleased(MouseEvent e) {}
        @Override
        public void mouseEntered(MouseEvent e) {}
        @Override
        public void mouseExited(MouseEvent e) {}
    }

    // create GUI
    private void initGUI(){
        this.setLayout(new BorderLayout());
        contentPanel.setLayout(new BoxLayout(contentPanel,BoxLayout.Y_AXIS));

        // create cells for all hotels from database
        Hotel[] tab = sql.getHotels();
        for (Hotel hotel : tab) {
            HotelCell cell = new HotelCell(hotel);
            cell.setPreferredSize(new Dimension(100, 100));
            contentPanel.add(cell);
        }

        this.add(scrollPane, BorderLayout.CENTER);
    }
}
