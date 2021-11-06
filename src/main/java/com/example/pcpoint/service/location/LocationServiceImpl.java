package com.example.pcpoint.service.location;

import com.example.pcpoint.model.entity.location.Location;
import com.example.pcpoint.repository.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    public LocationServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }


    @Override
    public Location addLocation(Location location) {
        return locationRepository.save(location);
    }

    @Override
    public List<Location> findAllLocations() {
        return locationRepository.findAll();
    }

    @Override
    public Location findLocationById(Long id) {
        return null;
    }

    @Override
    public Location updateLocation(Location location) {
        return null;
    }

    @Override
    public void deleteLocation(Long id) {

    }

    @Override
    public List<Location> findAllLocationsByCity(String city) {
        return null;
    }

    @Override
    public List<String> findAllCities() {
        return null;
    }
}
