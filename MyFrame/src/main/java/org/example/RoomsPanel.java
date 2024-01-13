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

    // JPanel inside of JScrollPane
    private JPanel contentPanel = new JPanel();
    private JScrollPane scrollPane = new JScrollPane(contentPanel);

    // constructor when displaying all the rooms in a certain hotel
    RoomsPanel(SQLHelper sql, MainFrame mainFrame, int id){
        this.sql = sql;
        this.mainFrame = mainFrame;
        initGUI(sql.getRooms(id));
    }

    // constructor when displaying only available rooms
    RoomsPanel(SQLHelper sql, MainFrame mainFrame, RoomRecord[] tab){
        this.sql = sql;
        this.mainFrame = mainFrame;
        initGUI(tab);
    }

    // inner class representing single hotel cell on the displayed list
    private class RoomCell extends JPanel implements MouseListener {

        // room record
        protected RoomRecord roomRecord;

        // necessary labels providing information
        protected JLabel hotelName;
        protected JLabel roomCat;
        protected JLabel maxGuests;

        RoomCell(RoomRecord roomRecord) {
            this.roomRecord = roomRecord;
            this.roomCat = new JLabel(sql.getRoomCatName(roomRecord.room_id()));
            this.initCellGUI();
        }

        // create single cell
        void initCellGUI(){
            this.add(this.roomCat);
            this.setBackground(Color.blue);
            this.setBackground(Color.decode("#417680"));
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
        public void mouseEntered(MouseEvent e) {
            this.setBackground(Color.decode("#2E8695"));
        }
        @Override
        public void mouseExited(MouseEvent e) {
            this.setBackground(Color.decode("#417680"));
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
