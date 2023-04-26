package com.example.travel.controllers;

import com.example.travel.services.HotelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hotels")
@Log4j2
@RequiredArgsConstructor
@CrossOrigin("*")
public class HotelIndexingController {

    private final HotelService hotelService;

    @PostMapping
    public void indexHotelDetails(@RequestBody String places) {
        log.info("Request fired on controller, Indexing Controller method : indexPlaces");
        hotelService.indexHotelDetails(places);
    }

    @GetMapping
    public String getHotels() {
        return hotelService.fetchAllHotels();
    }

    @GetMapping("/{id}")
    public String getHotelDetails(@PathVariable String id) {
        return hotelService.fetchHotelById(id);
    }

    @GetMapping("/details")
    public String fetchAllHotelDetails() {
        return hotelService.fetchAllHotelDetails();
    }
}
