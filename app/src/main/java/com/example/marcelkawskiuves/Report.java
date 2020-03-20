package com.example.marcelkawskiuves;

public class Report {

    private int id;
    private int stationId;
    private String name;
    private String description;
    private String status;
    private String type;

    public Report(int id, int stationId, String name, String description, String status, String type) {
        this.id = id;
        this.stationId = stationId;
        this.name = name;
        this.description = description;
        this.status = status;
        this.type = type;
    }


}
