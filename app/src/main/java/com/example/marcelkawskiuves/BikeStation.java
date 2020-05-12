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
    private double coordinate1;
    private double coordinate2;
    private double distance;
    private String distanceString = "";
    private String distanceUnit = "";


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

    public void setDistanceString(String distanceString) { this.distanceString = distanceString; }

    public void setDistanceUnit(String distanceUnit) { this.distanceUnit = distanceUnit; }

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

    public String getDistanceString() { return distanceString; }

    public String getDistanceUnit() { return distanceUnit; }

    public void calculateDistance(Location deviceLocation) {
        Location bikeStationLocation = new Location("bikeStationLocation");
        bikeStationLocation.setLatitude(this.getCoordinate1());
        bikeStationLocation.setLongitude(this.getCoordinate2());
        if (deviceLocation != null) {
            this.distance = bikeStationLocation.distanceTo(deviceLocation);
        }
    }
}
