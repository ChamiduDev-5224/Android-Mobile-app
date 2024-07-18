package com.example.busseatbookingapp;

public class Bus {
    private String busId;
    private String startForm;
    private String endDestination;
    private String busRouteNo;
    private String busNumber;
    private String departureTime;
    private String arrivalTime;
    private String busType;
    private String runningCondition;
    private int seatCount; // Assuming seatCount is stored as a Long in Firebase

    public Bus() {
        // Default constructor required for calls to DataSnapshot.getValue(Bus.class)
    }

    public Bus(String busId, String startForm, String endDestination, String busRouteNo, String busNumber, String departureTime, String arrivalTime, String busType, String runningCondition, int seatCount) {
        this.busId = busId;
        this.startForm = startForm;
        this.endDestination = endDestination;
        this.busRouteNo = busRouteNo;
        this.busNumber = busNumber;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.busType = busType;
        this.runningCondition = runningCondition;
        this.seatCount = seatCount;
    }

    public String getBusId() {
        return busId;
    }

    public void setBusId(String busId) {
        this.busId = busId;
    }

    public String getStartForm() {
        return startForm;
    }

    public void setStartForm(String startForm) {
        this.startForm = startForm;
    }

    public String getEndDestination() {
        return endDestination;
    }

    public void setEndDestination(String endDestination) {
        this.endDestination = endDestination;
    }

    public String getBusRouteNo() {
        return busRouteNo;
    }

    public void setBusRouteNo(String busRouteNo) {
        this.busRouteNo = busRouteNo;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public String getRunningCondition() {
        return runningCondition;
    }

    public void setRunningCondition(String runningCondition) {
        this.runningCondition = runningCondition;
    }

    public int getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(int seatCount) {
        this.seatCount = seatCount;
    }


}
