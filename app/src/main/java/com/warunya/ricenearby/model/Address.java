package com.warunya.ricenearby.model;

import org.parceler.Parcel;

@Parcel
public class Address {

    public String name;
    public Double latitude;
    public Double longitude;

    public Address() {
    }

    public Address(String name, Double latitude, Double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
