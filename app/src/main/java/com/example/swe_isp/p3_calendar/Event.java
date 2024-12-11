package com.example.swe_isp.p3_calendar;

import java.io.Serializable;

public class Event implements Serializable { // (serializable) required to pass Event to EventActivity
    private String type;
    private String date;
    private String description;

    public Event(String type, String date, String description) {
        this.type = type;
        this.date = date;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
