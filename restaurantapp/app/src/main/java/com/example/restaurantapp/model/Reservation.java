package com.example.restaurantapp.model;

public class Reservation {
    private long id;
    private String date;
    private String time;
    private int numberOfGuests;

    public Reservation(String date, String time, int numberOfGuests) {
        this.date = date;
        this.time = time;
        this.numberOfGuests = numberOfGuests;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
