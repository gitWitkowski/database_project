package org.example;

// record storing information from database from table hotele

/**
 * Record holding values from pokoje table
 * @param room_id room ID
 * @param hotel_id hotel ID
 * @param cat_id room category Id
 * @param maxGuests maximal number of guests
 * @param name name of the hotel
 */
public record RoomRecord(int room_id, int hotel_id, int cat_id, int maxGuests, String name) {}
