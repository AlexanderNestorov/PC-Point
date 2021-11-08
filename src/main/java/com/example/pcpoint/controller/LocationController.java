package com.example.pcpoint.controller;

import com.example.pcpoint.model.entity.location.LocationEntity;
import com.example.pcpoint.model.request.location.LocationAddRequest;
import com.example.pcpoint.model.request.location.LocationUpdateRequest;
import com.example.pcpoint.model.response.MessageResponse;
import com.example.pcpoint.model.service.location.LocationAddServiceModel;
import com.example.pcpoint.model.service.location.LocationUpdateServiceModel;
import com.example.pcpoint.service.location.LocationService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/location")
public class LocationController {

    private final LocationService locationService;


    private final ModelMapper modelMapper;

    public LocationController(LocationService locationService, ModelMapper modelMapper) {
        this.locationService = locationService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addLocation(@Valid @RequestBody LocationAddRequest locationAddRequest) {

        LocationAddServiceModel locationAddServiceModel =
                modelMapper.map(locationAddRequest, LocationAddServiceModel.class);


        locationService.addLocation(locationAddServiceModel);


        return ResponseEntity.ok(new MessageResponse("Location added successfully!"));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllLocations() {
        List<LocationEntity> locations = this.locationService.findAllLocations();
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<?> getLocationById(@PathVariable("id") Long id) {
        LocationEntity location = this.locationService.findLocationById(id);

        return ResponseEntity.ok(location);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateLocation(@Valid @RequestBody LocationUpdateRequest locationUpdateRequest) {
        LocationUpdateServiceModel location =
                modelMapper.map(locationUpdateRequest, LocationUpdateServiceModel.class);


        LocationEntity updated = locationService.updateLocation(location);


        return ResponseEntity.ok(updated);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteLocation(@PathVariable("id") Long id) {

        Long deletionId = locationService.findLocationById(id).getId();

        if(deletionId == null){
            return ResponseEntity.notFound().build();
        }

        this.locationService.deleteLocation(id);

        return ResponseEntity.ok(new MessageResponse("Location deleted successfully!"));
    }
}

