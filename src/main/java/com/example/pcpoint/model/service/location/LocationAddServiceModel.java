package com.example.pcpoint.model.service.location;


public class LocationAddServiceModel {


    private String city;

    private Double latitude;

    private Double longitude;

    private String address;

    public String getCity() {
        return city;
    }

    public LocationAddServiceModel setCity(String city) {
        this.city = city;
        return this;
    }

    public Double getLatitude() {
        return latitude;
    }

    public LocationAddServiceModel setLatitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    public Double getLongitude() {
        return longitude;
    }

    public LocationAddServiceModel setLongitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public LocationAddServiceModel setAddress(String address) {
        this.address = address;
        return this;
    }
}
