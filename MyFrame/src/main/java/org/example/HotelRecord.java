package org.example;

// record storing information from database from table hotele
public record HotelRecord(int hotel_id, String name, String city, String address, double rating) {
    @Override
    public String toString() {
        return city;
    }
}
