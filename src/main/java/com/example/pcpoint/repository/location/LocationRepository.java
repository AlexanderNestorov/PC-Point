package com.example.pcpoint.repository.location;

import com.example.pcpoint.model.entity.location.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<LocationEntity, Long> {
    Optional<LocationEntity> findLocationById(Long id);

    @Query(
            value = "SELECT * from locations l WHERE l.city = ?1",
            nativeQuery = true
    )
    List<LocationEntity> findAllByCity(String city);
    @Query(
            value = "SELECT DISTINCT l.city FROM locations l",
            nativeQuery = true
    )
    List<String> findAllCities();

    @Query(
            value = "SELECT * from locations l WHERE l.address = ?1",
            nativeQuery = true
    )
    Optional<LocationEntity> findByAddress(String address);

    Boolean existsByAddress(String address);
}
