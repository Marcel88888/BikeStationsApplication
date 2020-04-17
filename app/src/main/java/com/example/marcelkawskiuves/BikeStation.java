package com.example.marcelkawskiuves;

import android.location.Location;

import java.io.Serializable;

public class BikeStation implements Serializable {

    private String name;
    private int number;
    private String address;
    private int total;
    private int available;
    private int free;
    private int reports = 0;
    private double coordinate1;
    private double coordinate2;
    private double distance = Double.MAX_VALUE;


    public BikeStation(String name, int number, String address, int total, int available, int free, double coordinate1, double coordinate2){

        this.name = name;
        this.number = number;
        this.address = address;
        this.total = total;
        this.available = available;
        this.free = free;
        this.coordinate1 = coordinate1;
        this.coordinate2 = coordinate2;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public void setFree(int free) {
        this.free = free;
    }

    public void setCoordinate1(double coordinate1) {
        this.coordinate1 = coordinate1;
    }

    public void setCoordinate2(double coordinate2) {
        this.coordinate2 = coordinate2;
    }

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    public String getAddress() {
        return address;
    }

    public int getTotal() {
        return total;
    }

    public int getAvailable() {
        return available;
    }

    public int getFree() {
        return free;
    }

    public double getCoordinate1() {
        return coordinate1;
    }

    public double getCoordinate2() {
        return coordinate2;
    }

    public double getDistance() { return distance; }


    public void calculateDistance() {
        Location ETSELocation = new Location("ETSELocation");
        ETSELocation.setLatitude(39.512634);
        ETSELocation.setLongitude(-0.424035);
        Location bikeStationLocation = new Location("bikeStationLocation");
        bikeStationLocation.setLatitude(this.getCoordinate1());
        bikeStationLocation.setLongitude(this.getCoordinate2());
        this.distance = bikeStationLocation.distanceTo(ETSELocation);
    }
}
