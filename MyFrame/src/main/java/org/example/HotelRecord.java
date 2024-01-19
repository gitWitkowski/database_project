package org.example;

/**
 * Record storing information from database from table hotele
 * @param hotel_id hotel ID
 * @param name hotel name
 * @param city city name
 * @param address hotel location
 * @param rating hotel's rating by users
 */
public record HotelRecord(int hotel_id, String name, String city, String address, double rating) {
    @Override
    public String toString() {
        return city;
    }
}
