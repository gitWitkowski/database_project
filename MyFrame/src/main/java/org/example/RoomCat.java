package org.example;

public record RoomCat(int id, String nazwa, double cena) {
    @Override
    public String toString() {
        return nazwa;
    }
}
