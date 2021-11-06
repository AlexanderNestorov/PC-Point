package com.example.pcpoint.service.location;

import com.example.pcpoint.model.entity.location.Location;

import java.util.List;

public interface LocationService {

    Location addLocation(Location location);

    List<Location> findAllLocations();

    Location findLocationById(Long id);

    Location updateLocation(Location location);

    void deleteLocation(Long id);

    List<Location> findAllLocationsByCity(String city);

    List<String> findAllCities();
}
