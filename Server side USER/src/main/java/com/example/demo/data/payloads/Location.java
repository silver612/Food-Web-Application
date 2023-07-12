package com.example.demo.data.payloads;

public class Location {
    
    private Float latitude, longitude;
    private String address;

    public Location(){
        this.latitude = null;
        this.longitude = null;
        this.address = null;
    }

    public Location(Float latitude, Float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = null;
    }

    public Location(Float latitude, Float longitude, String address) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    public Location(String locString) {
        this.latitude = Float.parseFloat(locString.split("=")[0].split(",")[0]);
        this.longitude = Float.parseFloat(locString.split("=")[0].split(",")[1]);
        this.address = locString.split("=")[1];
    }

    public Float getLatitude() {
        return latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return latitude + "," + longitude;
    }
}
