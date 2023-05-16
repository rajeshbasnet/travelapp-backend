package com.example.travel.controllers;

import com.example.travel.services.HotelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/hotels")
@Log4j2
@RequiredArgsConstructor
@CrossOrigin("*")
public class HotelIndexingController {

    private final HotelService hotelService;

    @PostMapping("/detail")
    public ResponseEntity<Map<String, String>> indexHotelDetails(@RequestBody String places) {
        log.info("Request fired on controller, Indexing Controller method : indexPlaces");
        hotelService.indexHotelDetails(places);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Hotel created successfully"));
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> indexHotel(@RequestBody String hotel) throws IOException {
        log.info("Request fired on controller, Indexing Controller method : indexPlaces");
        hotelService.indexHotel(hotel);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Hotel created successfully"));
    }


    @GetMapping
    public String getHotels() {
        return hotelService.fetchAllHotels();
    }

    @GetMapping("/{id}")
    public String getHotelDetails(@PathVariable String id) {
        return hotelService.fetchHotelById(id);
    }

    @PostMapping("/username")
    public String getHotelDetailsByUsername(@RequestParam String username) {
        return hotelService.fetchHotelByUsername(username);
    }

    @GetMapping("/details")
    public String fetchAllHotelDetails() {
        return hotelService.fetchAllHotelDetails();
    }

    @PutMapping("/details/{id}")
    public ResponseEntity<Map<String, String>> updateHotelDetails(@PathVariable String id, @RequestBody String details) {
        hotelService.updateHotelDetails(id, details);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Reviews added successfully"));
    }

    @PostMapping("/column")
    public String getHotelsByColumn(@RequestParam String column, @RequestParam String value ) {
        return hotelService.fetchHotelByColumn(column, value);
    }
}
