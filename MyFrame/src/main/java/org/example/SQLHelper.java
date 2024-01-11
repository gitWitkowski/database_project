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

    protected String[] getCities(){
        List<String> table = new ArrayList<>();
        try {
            PreparedStatement pst = connection.prepareStatement("SELECT miasto FROM projekt.hotele", ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = pst.executeQuery();
            while (rs.next())  {
                String miasto    = rs.getString("miasto") ;
                table.add(miasto);
            }
            rs.close();
            pst.close();    }
        catch(SQLException e)  {
            System.out.println("Blad podczas przetwarzania danych:"+e) ;   }
        return  table.toArray(new String[0]);
    }

    protected String[] getRoomCat(){
        List<String> table = new ArrayList<>();
        try {
            PreparedStatement pst = connection.prepareStatement("SELECT nazwa_kategorii FROM projekt.kategorie_pokoi", ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = pst.executeQuery();
            while (rs.next())  {
                String kategoria = rs.getString("nazwa_kategorii") ;
                table.add(kategoria);
            }
            rs.close();
            pst.close();    }
        catch(SQLException e)  {
            System.out.println("Blad podczas przetwarzania danych:"+e) ;   }
        return  table.toArray(new String[0]);
    }
}
