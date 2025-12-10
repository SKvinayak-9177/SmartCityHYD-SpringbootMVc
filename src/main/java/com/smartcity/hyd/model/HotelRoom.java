package com.smartcity.hyd.model;

public class HotelRoom {

    private Long id;
    private int roomNo;
    private int capacity;
    private String amenities;
    private double pricePerDay;
    private String status;
    
    public HotelRoom() {}
    public HotelRoom(Long id, int roomNo, int capacity, String amenities, double price, String status) {
        this.id = id;
        this.roomNo = roomNo;
        this.capacity = capacity;
        this.amenities = amenities;
        this.pricePerDay = price;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(int roomNo) {
        this.roomNo = roomNo;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getAmenities() {
        return amenities;
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
