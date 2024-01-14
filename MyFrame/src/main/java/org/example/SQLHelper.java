package org.example;

import org.apache.ibatis.type.LocalDateTimeTypeHandler;

import javax.swing.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

// class responsible for connection and interactions with database
public class SQLHelper {
    // variable for connection with database
    private Connection connection = null;

    SQLHelper(String dbaseURL, String username, String password){
        getConnection(dbaseURL, username, password);
    }

    // connect to database
    private void getConnection(String dbaseURL, String username, String password){
        try {
            connection = DriverManager.getConnection(dbaseURL, username, password);
            System.out.println("Successfully Connected to the database!");
        } catch (SQLException e) {
            System.out.println("Could not connect to the database " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Could not connect to the database " + e.getMessage(), "BLAD", JOptionPane.ERROR_MESSAGE);
        }
    }

    // get table of hotel records
    protected HotelRecord[] getHotels(){
        List<HotelRecord> table = new ArrayList<>();
        try {
            PreparedStatement pst = connection.prepareStatement("SELECT * FROM projekt.hotele", ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = pst.executeQuery();
            while (rs.next())  {
                HotelRecord temp = new HotelRecord(rs.getInt("hotel_id"),
                        rs.getString("nazwa"),rs.getString("miasto"),
                        rs.getString("adres"), rs.getDouble("srednia_ocena"));
                table.add(temp);
            }
            rs.close();
            pst.close();    }
        catch(SQLException e)  {
            System.out.println("Blad podczas przetwarzania danych:"+e) ;
            JOptionPane.showMessageDialog(null, e.getMessage(), "BLAD", JOptionPane.ERROR_MESSAGE);
        }
        return  table.toArray(new HotelRecord[0]);
    }

    // get table of rooms records
    protected RoomRecord[] getRooms(int hotel_id){
        List<RoomRecord> table = new ArrayList<>();
        try {
            PreparedStatement pst = connection.prepareStatement("SELECT * FROM projekt.pokoje p where hotel_id = ?", ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            pst.setInt(1, hotel_id);
            ResultSet rs = pst.executeQuery();
            while (rs.next())  {
                RoomRecord temp = new RoomRecord(
                        rs.getInt("pokoj_id"),
                        rs.getInt("hotel_id"),
                        rs.getInt("kategoria_id"),
                        rs.getInt("max_gosci"),
                        rs.getBoolean("obecnie_zajety")
                );
                table.add(temp);
            }
            rs.close();
            pst.close();    }
        catch(SQLException e)  {
            System.out.println("Blad podczas przetwarzania danych:"+e) ;
            JOptionPane.showMessageDialog(null, e.getMessage(), "BLAD", JOptionPane.ERROR_MESSAGE);
        }
        return  table.toArray(new RoomRecord[0]);
    }

    // get table of room categories records
    protected RoomCategoryRecord[] getRoomCat(){
        List<RoomCategoryRecord> table = new ArrayList<>();
        try {
            PreparedStatement pst = connection.prepareStatement("SELECT * FROM projekt.kategorie_pokoi", ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = pst.executeQuery();
            while (rs.next())  {
                RoomCategoryRecord temp = new RoomCategoryRecord(rs.getInt("kategoria_id"),rs.getString("nazwa_kategorii"),rs.getDouble("podstawa_cenowa"));
                table.add(temp);
            }
            rs.close();
            pst.close();    }
        catch(SQLException e)  {
            System.out.println("Blad podczas przetwarzania danych:"+e);
            JOptionPane.showMessageDialog(null, e.getMessage(), "BLAD", JOptionPane.ERROR_MESSAGE);
        }
        return  table.toArray(new RoomCategoryRecord[0]);
    }

    // get name of the category of the room with the given id
    protected String getRoomCatName(int room_id){
        String temp = "";
        try {
            PreparedStatement pst = connection.prepareStatement("SELECT nazwa_kategorii nk FROM projekt.pokoje p join projekt.kategorie_pokoi kp using(kategoria_id) where p.pokoj_id = ?", ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            pst.setInt(1,room_id);
            ResultSet rs = pst.executeQuery();
            while (rs.next())  {
                temp = rs.getString("nk");
            }
            rs.close();
            pst.close();    }
        catch(SQLException e)  {
            System.out.println("Blad podczas przetwarzania danych:"+e);
            JOptionPane.showMessageDialog(null, e.getMessage(), "BLAD", JOptionPane.ERROR_MESSAGE);
        }
        return  temp;
    }

    // get all rooms available for given conditions
    // calls plpgsql function
    protected RoomRecord[] getFreeRooms(int miastoID, LocalDate poczatek, LocalDate koniec, int kategoriaID, int iloscOsob){
        List<RoomRecord> table = new ArrayList<>();
        try {
            CallableStatement cst = connection.prepareCall(
            "{call projekt.wolne_pokoje(?, ?, ?, ?, ?)}"
            );
            cst.setInt(1,miastoID);
            cst.setObject(2,poczatek);
            cst.setObject(3,koniec);
            cst.setInt(4,kategoriaID);
            cst.setInt(5,iloscOsob);

            ResultSet rs ;
            rs = cst.executeQuery();
            while (rs.next())  {
                RoomRecord temp = new RoomRecord(
                        rs.getInt("pokoj_id"),
                        rs.getInt("hotel_id"),
                        rs.getInt("kategoria_id"),
                        rs.getInt("max_gosci"),
                        false
                );
                table.add(temp);
            }
            rs.close();
            cst.close();

        }
        catch(SQLException e)  {
            System.out.println("Blad podczas przetwarzania danych:"+e) ;
            JOptionPane.showMessageDialog(null, e.getMessage(), "BLAD", JOptionPane.ERROR_MESSAGE);
        }
        return  table.toArray(new RoomRecord[0]);
    }

    protected List<LocalDate[]> getAllReservations(int room_id){
        List<LocalDate[]> table = new ArrayList<>();
        try {
            PreparedStatement pst = connection.prepareStatement("select poczatek_pobytu, koniec_pobytu  from projekt.rezerwacje r where pokoj_id = ?", ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            pst.setInt(1,room_id);
            ResultSet rs = pst.executeQuery();
            while (rs.next())  {
                LocalDate start = rs.getDate("poczatek_pobytu").toLocalDate();
                LocalDate end = rs.getDate("koniec_pobytu").toLocalDate();
                table.add(new LocalDate[]{start, end});
            }
            rs.close();
            pst.close();    }
        catch(SQLException e)  {
            System.out.println("Blad podczas przetwarzania danych:"+e);
            JOptionPane.showMessageDialog(null, e.getMessage(), "BLAD", JOptionPane.ERROR_MESSAGE);
        }
        return table;
    }

    protected boolean[] checkUserCredentials(String login, char[] password){
        try {
            CallableStatement cst = connection.prepareCall("{call projekt.autoryzuj(?, ?)}");
            cst.setString(1, login);
            StringBuilder pass = new StringBuilder();
            for(var c : password)
                pass.append(String.valueOf(c));

            cst.setString(2, pass.toString());

            ResultSet rs ;
            rs = cst.executeQuery();
            if (rs.next())  {
                return new boolean[]{
                        rs.getBoolean(1), rs.getBoolean(2)
                };
            }
            rs.close();
            cst.close();
        }
        catch(SQLException e)  {
            System.out.println("Blad podczas przetwarzania danych:"+e);
            JOptionPane.showMessageDialog(null, e.getMessage(), "BLAD", JOptionPane.ERROR_MESSAGE);
        }
        return new boolean[]{false,false};
    }

    protected int registerUser(String login, char[] password, String mail, String fname, String lname, String pesel, String phone){
        try {
            CallableStatement cst = connection.prepareCall("{call projekt.dodaj_uzytkownika(?, ?, ?, ?, ?, ?, ?)}");
            StringBuilder pass = new StringBuilder();
            for(var c : password)
                pass.append(String.valueOf(c));

            cst.setString(1, login);
            cst.setString(2, pass.toString());
            cst.setString(3, mail);
            cst.setString(4, fname);
            cst.setString(5, lname);
            cst.setString(6, pesel);
            cst.setString(7, phone);
            
            ResultSet rs ;
            rs = cst.executeQuery();
            if (rs.next())  {
                return rs.getInt(1);
            }
            rs.close();
            cst.close();
        }
        catch(SQLException e)  {
            System.out.println("Blad podczas przetwarzania danych:"+e);
            JOptionPane.showMessageDialog(null, e.getMessage().split("#")[0], "BLAD", JOptionPane.ERROR_MESSAGE);
        }
        return 3;
    }

    protected GuestRecord getGuest(String login){
        try {
            PreparedStatement pst = connection.prepareStatement("select * from projekt.goscie r where login = ?", ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            pst.setString(1,login);
            ResultSet rs = pst.executeQuery();
            if (rs.next())  {
                return new GuestRecord(
                        rs.getInt("gosc_id"),
                        rs.getString("login"),
                        rs.getString("imie"),
                        rs.getString("nazwisko"),
                        rs.getString("pesel"),
                        rs.getString("nr_telefonu")
                );
            }
            rs.close();
            pst.close();    }
        catch(SQLException e)  {
            System.out.println("Blad podczas przetwarzania danych:"+e);
            JOptionPane.showMessageDialog(null, e.getMessage(), "BLAD", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }
}