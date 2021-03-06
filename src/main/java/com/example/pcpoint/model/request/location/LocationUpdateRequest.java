package com.example.pcpoint.model.request.location;

import javax.validation.constraints.*;

public class LocationUpdateRequest {

    @Positive
    private Long id;

    @NotBlank
    private String city;

    @Min(0)
    @NotNull
    private Double latitude;

    @Min(0)
    @NotNull
    private Double longitude;

    @NotBlank
    private String address;

    public Long getId() {
        return id;
    }

    public LocationUpdateRequest setId(Long id) {
        this.id = id;
        return this;
    }

    public String getCity() {
        return city;
    }

    public LocationUpdateRequest setCity(String city) {
        this.city = city;
        return this;
    }

    public Double getLatitude() {
        return latitude;
    }

    public LocationUpdateRequest setLatitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    public Double getLongitude() {
        return longitude;
    }

    public LocationUpdateRequest setLongitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public LocationUpdateRequest setAddress(String address) {
        this.address = address;
        return this;
    }
}
