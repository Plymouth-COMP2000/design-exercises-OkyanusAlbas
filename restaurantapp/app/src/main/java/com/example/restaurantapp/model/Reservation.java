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

    // --- Getters ---
    public long getId() {
        return id;
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

    // --- Setters ---
    public void setId(long id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }
}
