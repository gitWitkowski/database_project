package org.example;

public record GuestRecord(int guestId, String login, String fname, String lname, String pesel, String phone) {
    @Override
    public String toString() {
        return fname + " " + lname;
    }
}
