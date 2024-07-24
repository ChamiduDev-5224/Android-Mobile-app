package com.example.busseatbookingapp;

public class ReservedSeat {

    private int seatNumber;
    private String checkingPlace;

    public ReservedSeat() {
        // Default constructor required for calls to DataSnapshot.getValue(ReservedSeat.class)
    }

    public ReservedSeat(int seatNumber, String checkingPlace) {
        this.seatNumber = seatNumber;
        this.checkingPlace = checkingPlace;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getCheckingPlace() {
        return checkingPlace;
    }

    public void setCheckingPlace(String checkingPlace) {
        this.checkingPlace = checkingPlace;
    }
}
