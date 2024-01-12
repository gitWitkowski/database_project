package org.example;

public record Hotel(int id, String name, String city, String address, double rating) {
    @Override
    public String toString() {
        return city;
    }
}
