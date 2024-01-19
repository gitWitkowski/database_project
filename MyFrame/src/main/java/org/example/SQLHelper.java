package org.example;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.swing.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for connection and interactions with database
 */
public class SQLHelper {
    // variable for connection with database
    private Connection connection = null;

    /**
     * Class constructor
     * @param dbaseURL database URL
     * @param username database username
     * @param password database password
     */
    SQLHelper(String dbaseURL, String username, String password){
        getConnection(dbaseURL, username, password);
    }

    /**
     * Connect to database
     * @param dbaseURL database URL
     * @param username database username
     * @param password database password
     */
    private void getConnection(String dbaseURL, String username, String password){
        try {
            connection = DriverManager.getConnection(dbaseURL, username, password);
            System.out.println("Successfully Connected to the database!");
        } catch (SQLException e) {
            System.out.println("Could not connect to the database " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Could not connect to the database " + e.getMessage(), "BLAD", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Get table of hotel records
     */
    protected HotelRecord[] getHotels(){
        List<HotelRecord> table = new ArrayList<>();
        try {
            PreparedStatement pst = connection.prepareStatement("SELECT distinct * FROM projekt.hotele", ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
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

    /**
     * Get table of cities
     */
    protected String[] getCities(){
        List<String> table = new ArrayList<>();
        try {
            PreparedStatement pst = connection.prepareStatement("SELECT distinct miasto FROM projekt.hotele", ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = pst.executeQuery();
            while (rs.next())  {
                table.add(rs.getString("miasto"));
            }
            rs.close();
            pst.close();    }
        catch(SQLException e)  {
            System.out.println("Blad podczas przetwarzania danych:"+e) ;
            JOptionPane.showMessageDialog(null, e.getMessage(), "BLAD", JOptionPane.ERROR_MESSAGE);
        }
        return  table.toArray(new String[0]);
    }

    /**
     * Get table of rooms records
     * @param hotel_id hotel ID
     */
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
                        rs.getString("nazwa")
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

    /**
     * Get table of room categories records
     */
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

    /**
     * Get name of the category of the room with the given id
     * @param room_id room ID
     */
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

    /**
     * Get name of the city of the hotel with the given id
     * @param hotel_id hotel ID
     */
    protected String getCityName(int hotel_id){
        String temp = "";
        try {
            PreparedStatement pst = connection.prepareStatement("SELECT miasto FROM projekt.hotele h where h.hotel_id = ?", ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            pst.setInt(1,hotel_id);
            ResultSet rs = pst.executeQuery();
            while (rs.next())  {
                temp = rs.getString("miasto");
            }
            rs.close();
            pst.close();    }
        catch(SQLException e)  {
            System.out.println("Blad podczas przetwarzania danych:"+e);
            JOptionPane.showMessageDialog(null, e.getMessage(), "BLAD", JOptionPane.ERROR_MESSAGE);
        }
        return  temp;
    }

    /**
     * Get price of the room category
     * @param cat_id room category ID
     */
    protected Integer getCatPrice(int cat_id){
        try {
            PreparedStatement pst = connection.prepareStatement("SELECT podstawa_cenowa FROM projekt.kategorie_pokoi kp where kp.kategoria_id = ?", ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            pst.setInt(1,cat_id);
            ResultSet rs = pst.executeQuery();
            if (rs.next())  {
                return rs.getInt("podstawa_cenowa");
            }
            rs.close();
            pst.close();    }
        catch(SQLException e)  {
            System.out.println("Blad podczas przetwarzania danych:"+e);
            JOptionPane.showMessageDialog(null, e.getMessage(), "BLAD", JOptionPane.ERROR_MESSAGE);
        }
        return  null;
    }

    /**
     * Get all rooms available for given conditions, calls plpgsql function
     * @param miasto city name
     * @param poczatek start date
     * @param koniec end date
     * @param kategoriaID room category ID
     * @param iloscOsob number of guests
     */
    protected RoomRecord[] getFreeRooms(String miasto, LocalDate poczatek, LocalDate koniec, int kategoriaID, int iloscOsob){
        List<RoomRecord> table = new ArrayList<>();
        try {
            CallableStatement cst = connection.prepareCall(
            "{call funkcje.wolne_pokoje(?, ?, ?, ?, ?)}"
            );
            cst.setString(1,miasto);
            cst.setObject(2,poczatek);
            cst.setObject(3,koniec);
            cst.setInt(4,kategoriaID);
            cst.setInt(5,iloscOsob);

            ResultSet rs ;
            rs = cst.executeQuery();
            while (rs.next())  {
                RoomRecord temp = new RoomRecord(
                        rs.getInt("pokoj_id"),
                        rs.getInt("hotelID"),
                        rs.getInt("kategoria_id"),
                        rs.getInt("max_gosci"),
                        rs.getString("nazwa")
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

    /**
     * Get all reservations done for specific room
     * @param room_id room ID
     */
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

    /**
     * Check user credentials
     * @param login users login
     * @param password users password
     */
    protected boolean[] checkUserCredentials(String login, char[] password){
        try {
            CallableStatement cst = connection.prepareCall("{call funkcje.autoryzuj(?, ?)}");
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

    /**
     * Register new user
     * @param login users login
     * @param password users password
     * @param mail users email
     * @param fname users first name
     * @param lname users last name
     * @param pesel users PESEL
     * @param phone users phone number
     */
    protected int registerUser(String login, char[] password, String mail, String fname, String lname, String pesel, String phone){
        try {
            CallableStatement cst = connection.prepareCall("{call funkcje.dodaj_uzytkownika(?, ?, ?, ?, ?, ?, ?)}");
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

    /**
     * Get user record by login
     * @param login users login
     */
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

    /**
     * Make reservation
     * @param roomID room ID
     * @param guestID guest ID
     * @param start start date
     * @param end end date
     * @param numOfGuests number of guests
     */
    protected boolean makeReservation(int roomID,int guestID, LocalDate start, LocalDate end, int numOfGuests){
        try {
            CallableStatement cst = connection.prepareCall("{call funkcje.dodaj_rezerwacje(?, ?, ?, ?, ?)}");

            cst.setInt(1, roomID);
            cst.setInt(2, guestID);
            cst.setDate(3, java.sql.Date.valueOf(start));
            cst.setDate(4, java.sql.Date.valueOf(end));
            cst.setInt(5, numOfGuests);

            ResultSet rs ;
            rs = cst.executeQuery();
            if (rs.next())  {
                return rs.getBoolean(1);
            }
            rs.close();
            cst.close();
        }
        catch(SQLException e)  {
            System.out.println("Blad podczas przetwarzania danych:"+e);
            JOptionPane.showMessageDialog(null, e.getMessage().split("#")[0], "BLAD", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    /**
     * Get array of reservations for specific user
     * @param userId user ID
     */
    protected Object[][] getTableReservationsContent(int userId){
        List<Object[]> table = new ArrayList<>();
        try {
            PreparedStatement pst = connection.prepareStatement("select row_number() over(ORDER by poczatek_pobytu) as numer_wiersza, * FROM projekt.rezerwacje_goscia where gosc_id=?", ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            pst.setInt(1,userId);
            ResultSet rs = pst.executeQuery();
            while (rs.next())  {
                table.add(new Object[]{
                        rs.getString("numer_wiersza"),
                        rs.getString("gosc_id"),
                        rs.getString("rezerwacja_id"),
                        rs.getString("nazwa_kategorii"),
                        rs.getString("nazwa"),
                        rs.getString("miasto"),
                        rs.getDate("poczatek_pobytu").toLocalDate().toString(),
                        rs.getDate("koniec_pobytu").toLocalDate().toString(),
                        String.valueOf(rs.getInt("liczba_gosci"))
                });
            }
            rs.close();
            pst.close();    }
        catch(SQLException e)  {
            System.out.println("Blad podczas przetwarzania danych:"+e);
            JOptionPane.showMessageDialog(null, e.getMessage(), "BLAD", JOptionPane.ERROR_MESSAGE);
        }

        Object[][] returnArray = new Object[table.size()][];
        for(int i=0; i<table.size(); i++){
            returnArray[i] = new Object[9];
            for(int j=0; j<9; j++){
                returnArray[i][j] = table.get(i)[j];
            }
        }
        return returnArray;
    }

    /**
     * Get array of reservations of all users
     */
    protected Object[][] getTableReservationsContent(){
        List<Object[]> table = new ArrayList<>();
        try {
            PreparedStatement pst = connection.prepareStatement("select row_number() over(ORDER by poczatek_pobytu) as numer_wiersza, * FROM projekt.rezerwacje_goscia", ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = pst.executeQuery();
            while (rs.next())  {
                table.add(new Object[]{
                        rs.getString("numer_wiersza"),
                        rs.getString("rezerwacja_id"),
                        rs.getString("gosc_id"),
                        rs.getString("nazwa_kategorii"),
                        rs.getString("nazwa"),
                        rs.getString("miasto"),
                        rs.getDate("poczatek_pobytu").toLocalDate().toString(),
                        rs.getDate("koniec_pobytu").toLocalDate().toString(),
                        String.valueOf(rs.getInt("liczba_gosci"))
                });
            }
            rs.close();
            pst.close();    }
        catch(SQLException e)  {
            System.out.println("Blad podczas przetwarzania danych:"+e);
            JOptionPane.showMessageDialog(null, e.getMessage(), "BLAD", JOptionPane.ERROR_MESSAGE);
        }

        Object[][] returnArray = new Object[table.size()][];
        for(int i=0; i<table.size(); i++){
            returnArray[i] = new Object[9];
            for(int j=0; j<9; j++){
                returnArray[i][j] = table.get(i)[j];
            }
        }
        return returnArray;
    }

    /**
     * Get array of bills of specific user
     * @param userId user ID
     */
    protected Object[][] getTableBillsContent(int userId){
        List<Object[]> table = new ArrayList<>();
        try {
            PreparedStatement pst = connection.prepareStatement("select * FROM projekt.rachunki_goscia where gosc_id=?", ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            pst.setInt(1,userId);
            ResultSet rs = pst.executeQuery();
            while (rs.next())  {
                table.add(new Object[]{
                        rs.getString("rezerwacja_id"),
                        rs.getString("gosc_id"),
                        rs.getString("nazwa"),
                        rs.getString("forma_platnosci"),
                        String.valueOf(rs.getString("suma_kosztow")),
                        String.valueOf(rs.getString("podstawa_cenowa")),
                        String.valueOf(rs.getString("ilosc_nocy")),
                });
            }
            rs.close();
            pst.close();    }
        catch(SQLException e)  {
            System.out.println("Blad podczas przetwarzania danych:"+e);
            JOptionPane.showMessageDialog(null, e.getMessage(), "BLAD", JOptionPane.ERROR_MESSAGE);
        }

        Object[][] returnArray = new Object[table.size()][];
        for(int i=0; i<table.size(); i++){
            returnArray[i] = new Object[7];
            for(int j=0; j<7; j++){
                returnArray[i][j] = table.get(i)[j];
            }
        }
        return returnArray;
    }

    /**
     * Get array of bills of all users
     */
    protected Object[][] getTableBillsContent(){
        List<Object[]> table = new ArrayList<>();
        try {
            PreparedStatement pst = connection.prepareStatement("select * FROM projekt.rachunki_goscia", ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = pst.executeQuery();
            while (rs.next())  {
                table.add(new Object[]{
                        rs.getString("rezerwacja_id"),
                        rs.getString("gosc_id"),
                        rs.getString("nazwa"),
                        rs.getString("forma_platnosci"),
                        String.valueOf(rs.getString("suma_kosztow")),
                        String.valueOf(rs.getString("podstawa_cenowa")),
                        String.valueOf(rs.getString("ilosc_nocy")),
                });
            }
            rs.close();
            pst.close();    }
        catch(SQLException e)  {
            System.out.println("Blad podczas przetwarzania danych:"+e);
            JOptionPane.showMessageDialog(null, e.getMessage(), "BLAD", JOptionPane.ERROR_MESSAGE);
        }

        Object[][] returnArray = new Object[table.size()][];
        for(int i=0; i<table.size(); i++){
            returnArray[i] = new Object[7];
            for(int j=0; j<7; j++){
                returnArray[i][j] = table.get(i)[j];
            }
        }
        return returnArray;
    }

    /**
     * Get array of best clients
     */
    protected Object[][] getTableBestClientsContent(){
        List<Object[]> table = new ArrayList<>();
        try {
            PreparedStatement pst = connection.prepareStatement("select * FROM projekt.stali_klienci", ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = pst.executeQuery();
            while (rs.next())  {
                table.add(new Object[]{
                        rs.getString("imie"),
                        rs.getString("nazwisko"),
                        String.valueOf(rs.getString("suma_kosztow")),
                        String.valueOf(rs.getString("ilosc_pobytow"))
                });
            }
            rs.close();
            pst.close();    }
        catch(SQLException e)  {
            System.out.println("Blad podczas przetwarzania danych:"+e);
            JOptionPane.showMessageDialog(null, e.getMessage(), "BLAD", JOptionPane.ERROR_MESSAGE);
        }

        Object[][] returnArray = new Object[table.size()][];
        for(int i=0; i<table.size(); i++){
            returnArray[i] = new Object[4];
            for(int j=0; j<4; j++){
                returnArray[i][j] = table.get(i)[j];
            }
        }
        return returnArray;
    }

    /**
     * Reset database to factory settings
     */
    protected void resetDB(){
        ScriptRunner scriptRunner = new ScriptRunner(connection);
        scriptRunner.setSendFullScript(false);
        scriptRunner.setStopOnError(true);
        try{

            boolean continueOrError = false;
            boolean ignoreFailedDrops = false;
            String commentPrefix = "--";
            String separator = ";";
            String blockCommentStartDelimiter = "/*";
            String blockCommentEndDelimiter = "*/";

            ScriptUtils.executeSqlScript(
                    connection,
                    new EncodedResource(new PathResource("../SQL/create_db.sql")),
                    continueOrError,
                    ignoreFailedDrops,
                    commentPrefix,
                    separator,
                    blockCommentStartDelimiter,
                    blockCommentEndDelimiter
            );

            ScriptUtils.executeSqlScript(
                    connection,
                    new EncodedResource(new PathResource("../SQL/add_data.sql")),
                    continueOrError,
                    ignoreFailedDrops,
                    commentPrefix,
                    separator,
                    blockCommentStartDelimiter,
                    blockCommentEndDelimiter
            );

            JOptionPane.showMessageDialog(null, "Przywrocono do ustawien domyslnych", "SUKCES", JOptionPane.INFORMATION_MESSAGE);
        }catch (Exception e){
            System.out.println("Blad podczas resetowania bazdy: "+e);
            JOptionPane.showMessageDialog(null, e.getMessage(), "BLAD", JOptionPane.ERROR_MESSAGE);
        }
    }
}