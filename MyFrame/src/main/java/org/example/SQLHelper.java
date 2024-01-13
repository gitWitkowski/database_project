package org.example;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
        }
    }

    // get table of hotel records
    protected Hotel[] getHotels(){
        List<Hotel> table = new ArrayList<>();
        try {
            PreparedStatement pst = connection.prepareStatement("SELECT * FROM projekt.hotele", ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = pst.executeQuery();
            while (rs.next())  {
                Hotel temp = new Hotel(rs.getInt("hotel_id"),
                        rs.getString("nazwa"),rs.getString("miasto"),
                        rs.getString("adres"), rs.getDouble("srednia_ocena"));
                table.add(temp);
            }
            rs.close();
            pst.close();    }
        catch(SQLException e)  {
            System.out.println("Blad podczas przetwarzania danych:"+e) ;   }
        return  table.toArray(new Hotel[0]);
    }

    // get table of rooms records
    protected Room[] getRooms(int hotel_id){
        List<Room> table = new ArrayList<>();
        try {
            PreparedStatement pst = connection.prepareStatement("SELECT * FROM projekt.pokoje p where hotel_id = ?", ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            pst.setInt(1, hotel_id);
            ResultSet rs = pst.executeQuery();
            while (rs.next())  {
                Room temp = new Room(
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
            System.out.println("Blad podczas przetwarzania danych:"+e) ;   }
        return  table.toArray(new Room[0]);
    }

    // get table of room categories records
    protected RoomCat[] getRoomCat(){
        List<RoomCat> table = new ArrayList<>();
        try {
            PreparedStatement pst = connection.prepareStatement("SELECT * FROM projekt.kategorie_pokoi", ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = pst.executeQuery();
            while (rs.next())  {
                RoomCat temp = new RoomCat(rs.getInt("kategoria_id"),rs.getString("nazwa_kategorii"),rs.getDouble("podstawa_cenowa"));
                table.add(temp);
            }
            rs.close();
            pst.close();    }
        catch(SQLException e)  {
            System.out.println("Blad podczas przetwarzania danych:"+e) ;   }
        return  table.toArray(new RoomCat[0]);
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
            System.out.println("Blad podczas przetwarzania danych:"+e) ;   }
        return  temp;
    }

    // get all rooms available for given conditions
    // calls plpgsql function
    protected Room[] getFreeRooms(int miastoID, LocalDate poczatek, LocalDate koniec, int kategoriaID, int iloscOsob){
        List<Room> table = new ArrayList<>();
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
                Room temp = new Room(
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
            System.out.println("Blad podczas przetwarzania danych:"+e) ;   }
        return  table.toArray(new Room[0]);
    }
}