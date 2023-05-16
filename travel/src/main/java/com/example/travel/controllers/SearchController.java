package com.example.travel.controllers;

import com.example.travel.services.impl.HotelSearchES;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/es")
@RequiredArgsConstructor
@CrossOrigin("*")
public class SearchController {

    private final HotelSearchES hotelSearchES;

    @GetMapping("/hotels")
    public String getHotels() throws IOException {
        log.info("Fetching all hotels");
        return hotelSearchES.searchHotels();
    }
}
