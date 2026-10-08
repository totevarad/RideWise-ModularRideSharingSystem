package com.airtribe.ridewise.model;

public class Driver {
    private String id;
    private String name;
    private String currentLocation;
    private boolean available;

    public Driver(String id, String name, String currentLocation) {
        this.id = id;
        this.name = name;
        this.currentLocation = currentLocation;
        this.available = true; // Available by default
    }

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getCurrentLocation() {
        return currentLocation;
    }
    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }
    public boolean isAvailable() {
        return available;
    }
    public void setAvailable(boolean available) {
        this.available = available;
    }
}
