package com.example.pcpoint.service;

import com.example.pcpoint.model.entity.location.LocationEntity;
import com.example.pcpoint.model.service.location.LocationAddServiceModel;
import com.example.pcpoint.model.service.location.LocationUpdateServiceModel;
import com.example.pcpoint.repository.location.LocationRepository;
import com.example.pcpoint.service.location.LocationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class LocationServiceTest {

    @Autowired
    private LocationService locationService;

    @Autowired
    private LocationRepository locationRepository;


    LocationEntity locationEntity1, locationEntity2;

    @BeforeEach
    public void setUp() {
        locationRepository.deleteAll();

        LocationEntity testEntity1 = new LocationEntity();
        testEntity1.setCity("TestCity1")
                .setAddress("Test address")
                .setLatitude(42.00)
                .setLongitude(21.00);

        LocationEntity testEntity2 = new LocationEntity();
        testEntity2.setCity("TestCity2")
                .setAddress("Test address 2")
                .setLatitude(84.00)
                .setLongitude(42.00);


        locationEntity1 = locationRepository.save(testEntity1);
        locationEntity2 = locationRepository.save(testEntity2);

    }

    @AfterEach
    public void tearDown() {
        locationRepository.deleteAll();
    }



    @Test
    @DisplayName("Testing if testing components load successfully")
    public void contextLoads() {
        assertThat(locationService).isNotNull();
        assertThat(locationRepository).isNotNull();
    }

    @Test
    @DisplayName("Testing if get method returns correct amount of locations")
    public void testGetAllMethod() {
        assertThat(locationService.findAllLocations().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Testing if add method works correctly")
    public void testAdd() {
        LocationAddServiceModel locationAddServiceModel = new LocationAddServiceModel();

        assertThat(locationService.findAllLocations().size()).isEqualTo(2);

        locationAddServiceModel.setCity("TestCity3")
                .setAddress("Test address 3")
                .setLatitude(10.00)
                .setLongitude(20.00);

        locationService.addLocation(locationAddServiceModel);



        assertThat(locationService.findAllLocations().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("Testing if update method works correctly")
    public void testUpdate() {
        LocationUpdateServiceModel locationUpdateServiceModel = new LocationUpdateServiceModel();

        locationUpdateServiceModel
                .setId(locationEntity1.getId())
                .setCity("TestCity-Updated")
                .setAddress("Test address - Updated")
                .setLatitude(10.00)
                .setLongitude(20.00);

        locationService.updateLocation(locationUpdateServiceModel);

        assertThat(locationService.findAllLocations().size()).isEqualTo(2);
        assertThat(locationService.findLocationById(locationEntity1.getId()).getCity()).isEqualTo("TestCity-Updated");
    }

    @Test
    @DisplayName("Testing if find by id method works correctly")
    public void testFindById() {
        LocationEntity test = locationService.findLocationById(locationEntity1.getId());
        assertThat(test).isNotNull();
        assertThat(test.getId()).isEqualTo(locationEntity1.getId());
    }

    @Test
    @DisplayName("Testing if delete by id method works correctly")
    public void testDeleteById() {
        locationService.deleteLocation(locationEntity1.getId());

        assertThat(locationService.findAllLocations().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Testing if find all by city method works correctly")
    public void testFindAllByCity() {
        List<LocationEntity> test1 = locationService.findAllLocationsByCity("TestCity1");
        List<LocationEntity> test2 = locationService.findAllLocationsByCity("TestCity2");

        assertThat(test1.size()).isEqualTo(1);
        assertThat(test2.size()).isEqualTo(1);

        assertThat(test1.get(0).getCity()).isEqualTo("TestCity1");
        assertThat(test2.get(0).getCity()).isEqualTo("TestCity2");
    }

    @Test
    @DisplayName("Testing if find all cities method works correctly")
    public void testFindByTimesBought() {
        List<String> list = locationService.findAllCities();
        assertThat(list.size()).isEqualTo(2);

        assertThat(list.get(0)).isEqualTo("TestCity1");
        assertThat(list.get(1)).isEqualTo("TestCity2");
    }
}
