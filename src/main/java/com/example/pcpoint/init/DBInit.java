package com.example.pcpoint.init;

import com.example.pcpoint.service.location.LocationService;
import com.example.pcpoint.service.user.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class DBInit implements CommandLineRunner {

    private final UserService userService;
    private final LocationService locationService;

    public DBInit(UserService userService, LocationService locationService) {
        this.userService = userService;
        this.locationService = locationService;
    }

    @Override
    public void run(String... args) throws Exception {
        userService.initializeUsersAndRoles();
        locationService.initializeLocations();
    }
}
