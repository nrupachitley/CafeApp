package com.nrupachitley.cafesearch;

import androidx.annotation.NonNull;

import java.util.Comparator;

public class DataModel<override> {

    private String cafeName;
    private String streetAddress;
    private String city;
    private String state;
    private String zipcode;
    private double rating;
    private double latitude;
    private double longitude;

    public DataModel(String cafeName, String streetAddress, String city, String state, String zipcode, double rating, double latitude, double longitude) {
        this.cafeName = cafeName;
        this.streetAddress = streetAddress;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
        this.rating = rating;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getCafeName() {
        return cafeName;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public String getCity() {
        return city;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getState() {
        return state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public double getRating() {
        return rating;
    }

    public String fullAddress() {
        return streetAddress + ", " + city + ", " + state + ", " + zipcode;
    }

    @NonNull
    @Override
    public String toString() {
        return cafeName + " " + String.valueOf(rating);
    }
}
