package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class RoomsPanel extends JPanel {

    // SQLHelper class responsible for interaction with database
    private SQLHelper sql;

    // reference to MainFrame
    private MainFrame mainFrame;

    // id of the given hotel
    private int hotel_id;

    // JPanel inside of JScrollPane
    private JScrollPane scrollPane;
    private JPanel contentPanel = new JPanel();

    RoomsPanel(SQLHelper sql, MainFrame mainFrame, int id){
        this.sql = sql;
        this.mainFrame = mainFrame;
        this.hotel_id = id;
        initGUI();
    }

    // inner class representing single hotel cell on the displayed list
    private class RoomCell extends JPanel implements MouseListener {

        // room record
        protected Room room;

        // necessary labels providing information
        protected JLabel hotelName;
        protected JLabel roomCat;
        protected JLabel maxGuests;

        RoomCell(Room room) {
            this.room = room;
            this.roomCat = new JLabel(sql.getRoomCatName(room.room_id()));
            this.initGUI();
        }

        // create single cell
        void initGUI(){
            this.add(this.roomCat);
            this.setBackground(Color.blue);
            this.setBorder(BorderFactory.createLineBorder(Color.black));
            this.addMouseListener(this);
        }

        @Override
        public void mouseClicked(MouseEvent e) {

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

        // create cells for all rooms from given hotel
        Room[] tab = sql.getRooms(hotel_id);
        for (Room room : tab) {
            RoomCell cell = new RoomCell(room);
            cell.setPreferredSize(new Dimension(100, 100));
            contentPanel.add(cell);
        }

        scrollPane = new JScrollPane(contentPanel);
        this.add(scrollPane, BorderLayout.CENTER);
    }
}
