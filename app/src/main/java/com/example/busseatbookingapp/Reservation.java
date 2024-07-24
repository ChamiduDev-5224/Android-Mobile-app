package com.example.busseatbookingapp;

import java.io.Serializable;

public class Reservation implements Serializable {
    private String reservationId;
    private String busId;
    private String userId;
    private int seatNumber;
    private String checkingPlace;
    private String reservingDate;
    private int status;
    private long reservedAt;
    private String email;

    // No-argument constructor
    public Reservation()  {
    }

    // All-argument constructor
    public Reservation(String reservationId,String busId, String userId, int seatNumber, String checkingPlace, String reservingDate, int status, long reservedAt, String email) {
        this.reservationId = reservationId;
        this.busId = busId;
        this.userId = userId;
        this.seatNumber = seatNumber;
        this.checkingPlace = checkingPlace;
        this.reservingDate = reservingDate;
        this.status = status;
        this.reservedAt = reservedAt;
        this.email = email;
    }

    // Getters and setters
    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
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

    public String getReservingDate() {
        return reservingDate;
    }

    public void setReservingDate(String reservingDate) {
        this.reservingDate = reservingDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getReservedAt() {
        return reservedAt;
    }

    public void setReservedAt(long reservedAt) {
        this.reservedAt = reservedAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
