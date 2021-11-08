package com.example.pcpoint.model.request.location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

public class LocationAddRequest {

    @NotBlank
    private String city;

    @NotBlank
    @PositiveOrZero
    private Double latitude;

    @NotBlank
    @PositiveOrZero
    private Double longitude;

    @NotBlank
    private String address;

    public String getCity() {
        return city;
    }

    public LocationAddRequest setCity(String city) {
        this.city = city;
        return this;
    }

    public Double getLatitude() {
        return latitude;
    }

    public LocationAddRequest setLatitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    public Double getLongitude() {
        return longitude;
    }

    public LocationAddRequest setLongitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public LocationAddRequest setAddress(String address) {
        this.address = address;
        return this;
    }
}
