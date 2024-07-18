package com.example.busseatbookingapp;

public class Reservation {
    private String email;
    private String phoneNumber;
    private String seatCount;

    public Reservation() {
        // Default constructor required for calls to DataSnapshot.getValue(Reservation.class)
    }

    public Reservation(String email, String phoneNumber, String seatCount) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.seatCount = seatCount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(String seatCount) {
        this.seatCount = seatCount;
    }
}
