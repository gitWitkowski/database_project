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
        protected HotelRecord hotelRecord;

        // necessary labels providing information
        protected JLabel hotelName;
        protected JLabel city;
        protected JLabel rating;
        protected JLabel address;
        protected JPanel box = new JPanel();

        HotelCell(HotelRecord hotelRecord) {
            this.hotelRecord = hotelRecord;
            this.hotelName = new JLabel(hotelRecord.name());
            this.city = new JLabel(hotelRecord.city());
            this.rating = new JLabel(""+ hotelRecord.rating());
            this.address = new JLabel(hotelRecord.address());
            this.initCellGUI();
        }

        // create single cell
        void initCellGUI(){
            this.add(box);
            box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
            box.add(hotelName);
            box.add(city);
            box.add(address);
//            this.add(rating);
            this.setBackground(Color.decode("#417680"));
            box.setBackground(Color.decode("#417680"));
            this.setBorder(BorderFactory.createLineBorder(Color.black));
            this.addMouseListener(this);
        }

        // clicking on a cell results in opening panel with rooms from a given hotel
        @Override
        public void mouseClicked(MouseEvent e) {
            mainFrame.changePanel(new RoomsPanel(sql, mainFrame, hotelRecord.hotel_id()));
        }
        @Override
        public void mousePressed(MouseEvent e) {}
        @Override
        public void mouseReleased(MouseEvent e) {}
        @Override
        public void mouseEntered(MouseEvent e) {
            this.setBackground(Color.decode("#2E8695"));
            box.setBackground(Color.decode("#2E8695"));
        }
        @Override
        public void mouseExited(MouseEvent e) {
            this.setBackground(Color.decode("#417680"));
            box.setBackground(Color.decode("#417680"));
        }
    }

    // create GUI
    private void initGUI(){
        this.setLayout(new BorderLayout());
        contentPanel.setLayout(new BoxLayout(contentPanel,BoxLayout.Y_AXIS));

        // create cells for all hotels from database
        HotelRecord[] tab = sql.getHotels();
        for (HotelRecord hotelRecord : tab) {
            HotelCell cell = new HotelCell(hotelRecord);
            cell.setPreferredSize(new Dimension(100, 200));
            contentPanel.add(cell);
        }

        scrollPane.getVerticalScrollBar().setUnitIncrement(15);
        this.add(scrollPane, BorderLayout.CENTER);
    }
}
