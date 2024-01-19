package org.example;

/**
 * Record holding values from kategorie_pokoi table
 * @param id room category ID
 * @param nazwa room category name
 * @param cena room category price per night
 */
public record RoomCategoryRecord(int id, String nazwa, double cena) {
    @Override
    public String toString() {
        return nazwa;
    }
}
