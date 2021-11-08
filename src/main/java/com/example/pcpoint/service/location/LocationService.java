package com.example.pcpoint.service.location;

import com.example.pcpoint.model.entity.location.LocationEntity;
import com.example.pcpoint.model.service.location.LocationAddServiceModel;
import com.example.pcpoint.model.service.location.LocationUpdateServiceModel;

import java.util.List;

public interface LocationService {

    void addLocation(LocationAddServiceModel locationAddServiceModel);

    List<LocationEntity> findAllLocations();

    LocationEntity findLocationById(Long id);

    LocationEntity updateLocation(LocationUpdateServiceModel locationUpdateServiceModel);

    void deleteLocation(Long id);

    List<LocationEntity> findAllLocationsByCity(String city);

    List<String> findAllCities();

    void initializeLocations();
}
