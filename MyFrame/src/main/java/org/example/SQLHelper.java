package org.example;

import java.sql.*;
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

    protected Hotel[] getCities(){
        List<Hotel> table = new ArrayList<>();
        try {
            PreparedStatement pst = connection.prepareStatement("SELECT * FROM projekt.hotele", ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = pst.executeQuery();
            while (rs.next())  {
                Hotel temp = new Hotel(rs.getInt("hotel_id"),
                        rs.getString("nazwa"),rs.getString("miasto"),
                        rs.getString("adres"), rs.getDouble("srednia_ocena"));
//                String miasto    = rs.getString("miasto") ;
                table.add(temp);
            }
            rs.close();
            pst.close();    }
        catch(SQLException e)  {
            System.out.println("Blad podczas przetwarzania danych:"+e) ;   }
        return  table.toArray(new Hotel[0]);
    }

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
}
