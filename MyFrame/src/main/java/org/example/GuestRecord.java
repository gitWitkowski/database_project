package org.example;

/**
 * Record holding values from goscie table
 * @param guestId guest ID
 * @param login login
 * @param fname first name
 * @param lname last name
 * @param pesel PESEL number
 * @param phone phone number
 */
public record GuestRecord(int guestId, String login, String fname, String lname, String pesel, String phone) {
    @Override
    public String toString() {
        return fname + " " + lname;
    }
}
