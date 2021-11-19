package com.example.pcpoint.controller;

import com.example.pcpoint.model.entity.location.LocationEntity;
import com.example.pcpoint.model.request.location.LocationAddRequest;
import com.example.pcpoint.model.response.JwtResponse;
import com.example.pcpoint.model.service.user.UserLoginServiceModel;
import com.example.pcpoint.repository.location.LocationRepository;
import com.example.pcpoint.repository.user.UserRepository;
import com.example.pcpoint.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.is;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class LocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private Long LOCATION1_ID, LOCATION2_ID;

    private String CITY_NAME1 = "Plovdiv", CITY_NAME2 = "Sofia";
    private Double LATITUDE1 = 42.20, LATITUDE2 = 28.59;
    private Double LONGITUDE1 = 56.1, LONGITUDE2 = 23.2;
    private String ADDRESS1 = "Jordan Gavazov", ADDRESS2 = "Eliezer Kalev";
    private String userToken;
    private String adminToken;
    @BeforeEach
    public void setUp(){

        locationRepository.deleteAll();

        UserLoginServiceModel userLoginServiceModel = new UserLoginServiceModel();
        userLoginServiceModel.setUsername("admin");
        userLoginServiceModel.setPassword("1234");

        JwtResponse jwtResponseAdmin = userService.loginUser(userLoginServiceModel);

        adminToken = jwtResponseAdmin.getAccessToken();

        userLoginServiceModel.setUsername("user");
        userLoginServiceModel.setPassword("1234");

        JwtResponse jwtResponseUser = userService.loginUser(userLoginServiceModel);

        userToken = jwtResponseUser.getAccessToken();


        LocationEntity location1 = new LocationEntity();
        location1.setCity(CITY_NAME1)
                .setLongitude(LONGITUDE1)
                .setLatitude(LATITUDE1)
                .setAddress(ADDRESS1);
        LOCATION1_ID = locationRepository.save(location1).getId();

        LocationEntity location2 = new LocationEntity();

        location2.setCity(CITY_NAME2)
                .setLongitude(LONGITUDE2)
                .setLatitude(LATITUDE2)
                .setAddress(ADDRESS2);
        LOCATION2_ID = locationRepository.save(location2).getId();
    }

    @AfterEach
    public void tearDown(){
        locationRepository.deleteAll();
    }

    @Test
    @DisplayName("Testing if testing components load successfully")
    public void contextLoads() throws Exception {
        assertThat(locationRepository).isNotNull();
        assertThat(mockMvc).isNotNull();
        assertThat(userService).isNotNull();
        assertThat(userRepository).isNotNull();
    }

    @Test
    @DisplayName("Test if 'all' method status is ok")
    public void testLocationsReturnsCorrectStatusCode() throws Exception {
        this.mockMvc.perform(get("/api/location/all"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Testing if 'all' method returns correct location")
    public void testLocationsReturnsCorrectContentType() throws Exception {
        this.mockMvc.perform(get("/api/location/all"))
                .andExpect(content().contentType("application/json"));
    }

    @Test
    @DisplayName("Testing if 'all' method returns correct number of locations")
    public void testLocationsReturnsCorrectNumberOfEntries() throws Exception {
        this.mockMvc.perform(get("/api/location/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void testFindLocationReturnsCorrectEntity() throws Exception {
        this.mockMvc.perform(get("/api/location/find/{id}", LOCATION1_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(LOCATION1_ID.intValue())));
    }

    @Test
    public void testFindLocationWhenNonExistingEntityId() throws Exception {
        this.mockMvc.perform(get("/api/location/find/{id}", -1))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFindLocationWhenNonExistingEntity() throws Exception {
        long count = locationRepository.count();
        this.mockMvc.perform(get("/api/location/find/{id}", count + 1))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteNotAuthorized() throws Exception {
        this.mockMvc.perform(delete("/api/location/delete/{id}", LOCATION1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testDeleteNotAuthorizedWithWrongRole() throws Exception {
        this.mockMvc.perform(delete("/api/location/delete/{id}", LOCATION1_ID)
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testDeleteAuthorized() throws Exception {
        this.mockMvc.perform(delete("/api/location/delete/{id}", LOCATION1_ID)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteAuthorizedWithUnexistingEntity() throws Exception {
        long count = locationRepository.count();
        this.mockMvc.perform(delete("/api/location/delete/{id}", count + 1)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteAuthorizedWithUnexistingEntityId() throws Exception {
        this.mockMvc.perform(delete("/api/location/delete/{id}", -1)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAddLocationNotAuthorized() throws Exception {

        LocationAddRequest locationAddRequest = new LocationAddRequest();
        locationAddRequest.setAddress(ADDRESS1);
        locationAddRequest.setCity(CITY_NAME1);
        locationAddRequest.setLongitude(LONGITUDE1);
        locationAddRequest.setLatitude(LATITUDE1);

        ObjectMapper mapper = new ObjectMapper();

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/location/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(locationAddRequest))
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());

    }

    @Test
    public void testAddLocationAuthorized() throws Exception {

        LocationAddRequest locationAddRequest = new LocationAddRequest();
        locationAddRequest.setAddress(ADDRESS1);
        locationAddRequest.setCity(CITY_NAME1);
        locationAddRequest.setLongitude(LONGITUDE1);
        locationAddRequest.setLatitude(LATITUDE1);

        ObjectMapper objectMapper = new ObjectMapper();

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/location/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(locationAddRequest))
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());

    }





}
