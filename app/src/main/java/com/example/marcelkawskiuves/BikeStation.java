package com.example.marcelkawskiuves;

public class BikeStation {

    private int number;
    private String name;
    private int available;

    public BikeStation(int number, String name, int available) {
        // I need to make it serializable, there's an instruction on virtual classroom
        this.number = number;
        this.name = name;
        this.available = available;
    }

    int getNumber() { return number; }
    String getName() { return name; }
    int getAvailable() { return available; }

    public void setNumber(int number) { this.number = number; }
    public void setName(String name) { this.name = name; }
    public void setAvailable(int available) { this.available = available; }
}
