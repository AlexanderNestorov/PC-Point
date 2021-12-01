package com.example.pcpoint.repository;

import com.example.pcpoint.exception.ItemNotFoundException;
import com.example.pcpoint.model.entity.location.LocationEntity;
import com.example.pcpoint.repository.location.LocationRepository;
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
public class LocationRepositoryTest {

    @Autowired
    private LocationRepository locationRepository;

    LocationEntity locationEntity1, locationEntity2;

    @BeforeEach
    public void setUp() {
        locationRepository.deleteAll();

        LocationEntity testEntity1 = new LocationEntity();
        testEntity1.setAddress("Test Address 1")
                .setCity("Test City 1")
                .setLatitude(42.0)
                .setLongitude(21.0);

        LocationEntity testEntity2 = new LocationEntity();
        testEntity2.setAddress("Test Address 2")
                .setCity("Test City 2")
                .setLatitude(84.0)
                .setLongitude(10.5);

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
        assertThat(locationRepository).isNotNull();
    }
    @Test
    @DisplayName("Testing if get method returns correct amount of locations")
    public void testGetAllMethod() {
        assertThat(locationRepository.findAll().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Testing if add method works correctly")
    public void testAdd() {
        assertThat(locationRepository.findAll().size()).isEqualTo(2);
        assertThat(locationRepository.findById(locationEntity1.getId()).get().getCity()).isEqualTo("Test City 1");
    }

    @Test
    @DisplayName("Testing if find by id method works correctly")
    public void testFindById() {
        LocationEntity test = locationRepository.findById(locationEntity1.getId()).orElseThrow(
                () -> new ItemNotFoundException("Location not found")
        );
        assertThat(test).isNotNull();
        assertThat(test.getId()).isEqualTo(locationEntity1.getId());
    }

    @Test
    @DisplayName("Testing if find by address method works correctly")
    public void testFindByAddress() {
        LocationEntity test = locationRepository.findByAddress(locationEntity2.getAddress()).orElseThrow(
                () -> new ItemNotFoundException("Product not found")
        );
        assertThat(test).isNotNull();
        assertThat(test.getAddress()).isEqualTo(locationEntity2.getAddress());
        assertThat(test.getId()).isEqualTo(locationEntity2.getId());
    }

    @Test
    @DisplayName("Testing if delete by id method works correctly")
    public void testDeleteById() {
        LocationEntity test = locationRepository.findById(locationEntity2.getId()).orElse(null);
        assertThat(test).isNotNull();

        locationRepository.deleteById(test.getId());

        assertThat(locationRepository.count()).isEqualTo(1);
        assertThat(locationRepository.findById(locationEntity2.getId()).orElse(null)).isNull();
    }

    @Test
    @DisplayName("Testing if find all by city method works correctly")
    public void testFindAllByCity() {
        List<LocationEntity> test1 = locationRepository.findAllByCity("Test City 1");
        List<LocationEntity> test2 = locationRepository.findAllByCity("Test City 2");

        assertThat(test1).isNotNull();
        assertThat(test1.size()).isEqualTo(1);

        assertThat(test2).isNotNull();
        assertThat(test2.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Testing if find all cities method works correctly")
    public void testAllCities() {
        List<String> test = locationRepository.findAllCities();
        assertThat(test).isNotNull();
        assertThat(test.size()).isEqualTo(2);
        assertThat(test.get(0)).isEqualTo("Test City 1");
        assertThat(test.get(1)).isEqualTo("Test City 2");
    }

}
