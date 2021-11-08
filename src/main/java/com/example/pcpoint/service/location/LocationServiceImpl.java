package com.example.pcpoint.service.location;

import com.example.pcpoint.exception.ItemNotFoundException;
import com.example.pcpoint.model.entity.location.LocationEntity;
import com.example.pcpoint.model.service.location.LocationAddServiceModel;
import com.example.pcpoint.model.service.location.LocationUpdateServiceModel;
import com.example.pcpoint.repository.LocationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    private final ModelMapper modelMapper;

    public LocationServiceImpl(LocationRepository locationRepository, ModelMapper modelMapper) {
        this.locationRepository = locationRepository;
        this.modelMapper = modelMapper;
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
                .orElse(null);
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
                    .setLatitude(42.151648)
                    .setLongitude(24.738892);

            locationEntitySofia
                    .setCity("Sofia")
                    .setAddress("Rusalka 3")
                    .setLatitude(42.6977)
                    .setLongitude(23.3219);


            locationRepository.saveAll(List.of(locationEntityPlovidv,locationEntitySofia));
        }
    }
}
