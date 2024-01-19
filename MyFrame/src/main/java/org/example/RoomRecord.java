package org.example;

// record storing information from database from table hotele
public record RoomRecord(int room_id, int hotel_id, int cat_id, int maxGuests, String name) {}
