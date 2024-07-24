package com.example.busseatbookingapp;

public class Reserve {

    private int seatNumber;
    private String checkingPlace;
    private String reservedAt;
    private String busId;
    private String userId;
    private int status;

    public Reserve() {
        // Default constructor required for calls to DataSnapshot.getValue(Reserve.class)
    }

    public Reserve(int seatNumber, String checkingPlace, String reservedAt, String busId, String userId, int status) {
        this.seatNumber = seatNumber;
        this.checkingPlace = checkingPlace;
        this.reservedAt = reservedAt;
        this.busId = busId;
        this.userId = userId;
        this.status = status;
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

    public String getReservedAt() {
        return reservedAt;
    }

    public void setReservedAt(String reservedAt) {
        this.reservedAt = reservedAt;
    }

    public String getBusId() {
        return busId;
    }

    public void setBusId(String busId) {
        this.busId = busId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
