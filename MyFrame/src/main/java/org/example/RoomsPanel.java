package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalDate;

public class RoomsPanel extends JPanel {

    // SQLHelper class responsible for interaction with database
    private SQLHelper sql;

    // reference to MainFrame
    private MainFrame mainFrame;

    // JPanel inside of JScrollPane
    private JPanel contentPanel = new JPanel();
    private JScrollPane scrollPane = new JScrollPane(contentPanel);

    // start and end date, number of guests to pass to RoomOfferPanel
    private LocalDate start = null;
    private LocalDate end = null;
    private int numOfGuests;

    // constructor when displaying all the rooms in a certain hotel
    RoomsPanel(SQLHelper sql, MainFrame mainFrame, int id){
        this.sql = sql;
        this.mainFrame = mainFrame;
        initGUI(sql.getRooms(id));
    }

    // constructor when displaying only available rooms
    RoomsPanel(SQLHelper sql, MainFrame mainFrame, RoomRecord[] tab, LocalDate start, LocalDate end, int numOfGuests){
        this.sql = sql;
        this.mainFrame = mainFrame;
        this.start = start;
        this.end = end;
        this.numOfGuests = numOfGuests;
        initGUI(tab);
    }

    // inner class representing single hotel cell on the displayed list
    private class RoomCell extends JPanel implements MouseListener {

        // room record
        protected RoomRecord roomRecord;

        // necessary labels providing information
        protected JLabel cityName;
        protected JLabel hotelName;
        protected JLabel roomCat;
        protected JLabel maxGuests;
        protected JLabel price;
        protected JPanel box = new JPanel();

        RoomCell(RoomRecord roomRecord) {
            this.roomRecord = roomRecord;
            this.roomCat = new JLabel(sql.getRoomCatName(roomRecord.room_id()));
            this.maxGuests = new JLabel("Maksymalna ilość gości: " + String.valueOf(roomRecord.maxGuests()));
            this.cityName = new JLabel(sql.getCityName(roomRecord.hotel_id()));
            this.hotelName = new JLabel(roomRecord.name());
            this.price = new JLabel("Cena za noc: " + sql.getCatPrice(roomRecord.cat_id()) + " zł");
            this.initCellGUI();
        }

        // create single cell
        void initCellGUI(){
            this.add(box);
            box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
            box.add(this.cityName);
            box.add(this.hotelName);
            box.add(this.roomCat);
            box.add(this.price);
            box.add(this.maxGuests);
            box.setBackground(Color.decode("#417680"));
            this.setBackground(Color.decode("#417680"));
            this.setBorder(BorderFactory.createLineBorder(Color.black));
            this.addMouseListener(this);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            mainFrame.changePanel(new RoomOfferPanel(sql, mainFrame, roomRecord.room_id(), start, end, numOfGuests));
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
    private void initGUI(RoomRecord[] table){
        this.setLayout(new BorderLayout());
        contentPanel.setLayout(new BoxLayout(contentPanel,BoxLayout.Y_AXIS));

        // create cells for all rooms from given hotel
        for (RoomRecord roomRecord : table) {
            RoomCell cell = new RoomCell(roomRecord);
            cell.setPreferredSize(new Dimension(100, 200));
            contentPanel.add(cell);
        }

        scrollPane.getVerticalScrollBar().setUnitIncrement(15);
        this.add(scrollPane, BorderLayout.CENTER);
    }
}
