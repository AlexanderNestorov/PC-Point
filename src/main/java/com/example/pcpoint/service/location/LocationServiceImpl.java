package com.example.pcpoint.service.location;

import com.example.pcpoint.exception.ItemNotFoundException;
import com.example.pcpoint.model.entity.location.LocationEntity;
import com.example.pcpoint.model.service.location.LocationAddServiceModel;
import com.example.pcpoint.model.service.location.LocationUpdateServiceModel;
import com.example.pcpoint.repository.location.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;


    public LocationServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }


    @Override
    public void addLocation(LocationAddServiceModel  locationAddServiceModel) {

        LocationEntity locationEntity = new LocationEntity();

        locationEntity
                .setCity(locationAddServiceModel.getCity())
                .setAddress(locationAddServiceModel.getAddress())
                .setLatitude(locationAddServiceModel.getLatitude())
                .setLongitude(locationAddServiceModel.getLongitude());

         locationRepository.save(locationEntity);
    }

    @Override
    public List<LocationEntity> findAllLocations() {
        return locationRepository.findAll();
    }

    @Override
    public LocationEntity findLocationById(Long id) {
        return locationRepository.findLocationById(id)
                .orElseThrow(() -> new ItemNotFoundException("LocationEntity with id " + id + " was not found"));
    }

    @Override
    public LocationEntity updateLocation(LocationUpdateServiceModel locationUpdateServiceModel) {

        LocationEntity location = locationRepository.findById(locationUpdateServiceModel.getId())
                .orElseThrow(() -> new ItemNotFoundException("LocationEntity with id " + locationUpdateServiceModel.getId() + " was not found"));
        if (location == null) {
            return null;
        }


        location
                .setCity(locationUpdateServiceModel.getCity())
                .setAddress(locationUpdateServiceModel.getAddress())
                .setLatitude(locationUpdateServiceModel.getLatitude())
                .setLongitude(locationUpdateServiceModel.getLongitude());

        return locationRepository.save(location);
    }

    @Override
    public void deleteLocation(Long id) {
        locationRepository.deleteById(id);
    }

    @Override
    public Boolean existsByAddress(String address) {
        return locationRepository.existsByAddress(address);
    }

    @Override
    public List<LocationEntity> findAllLocationsByCity(String city) {
        return locationRepository.findAllByCity(city);
    }

    @Override
    public List<String> findAllCities() {
        return this.locationRepository.findAllCities();
    }

    @Override
    public void initializeLocations() {
        if (locationRepository.count() == 0) {

            LocationEntity locationEntityPlovidv = new LocationEntity();

            LocationEntity locationEntitySofia = new LocationEntity();

            locationEntityPlovidv
                    .setCity("Plovdiv")
                    .setAddress("Eliezer Kalev 15")
                    .setLatitude(42.12817534624463)
                    .setLongitude(24.74552395966985);

            locationEntitySofia
                    .setCity("Sofia")
                    .setAddress("Rusalka 3")
                    .setLatitude(42.66720525691555)
                    .setLongitude(23.362304667621743);


            locationRepository.saveAll(List.of(locationEntityPlovidv,locationEntitySofia));
        }
    }
}
