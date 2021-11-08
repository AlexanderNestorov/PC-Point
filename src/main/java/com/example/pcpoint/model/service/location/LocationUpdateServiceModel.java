package com.example.pcpoint.model.service.location;

public class LocationUpdateServiceModel {

    private Long id;

    private String city;

    private Double latitude;

    private Double longitude;

    private String address;

    public Long getId() {
        return id;
    }

    public LocationUpdateServiceModel setId(Long id) {
        this.id = id;
        return this;
    }

    public String getCity() {
        return city;
    }

    public LocationUpdateServiceModel setCity(String city) {
        this.city = city;
        return this;
    }

    public Double getLatitude() {
        return latitude;
    }

    public LocationUpdateServiceModel setLatitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    public Double getLongitude() {
        return longitude;
    }

    public LocationUpdateServiceModel setLongitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public LocationUpdateServiceModel setAddress(String address) {
        this.address = address;
        return this;
    }
}
