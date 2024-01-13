package org.example;

public record RoomCategoryRecord(int id, String nazwa, double cena) {
    @Override
    public String toString() {
        return nazwa;
    }
}
