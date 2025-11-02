package com.example.restaurantapp;

public class Reservation {
    private String date;
    private String time;
    private int numberOfGuests;

    public Reservation(String date, String time, int numberOfGuests) {
        this.date = date;
        this.time = time;
        this.numberOfGuests = numberOfGuests;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
