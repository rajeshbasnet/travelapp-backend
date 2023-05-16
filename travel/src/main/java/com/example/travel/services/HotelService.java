package com.example.travel.services;

import java.io.IOException;

public interface HotelService {

    void indexHotelDetails(String detailsInJson);

    void indexHotel(String hotel) throws IOException;

    String fetchAllHotels();

    String fetchHotelById(String id);

    String fetchHotelByColumn(String column, String value);

    String fetchHotelByUsername(String username);

    String fetchAllHotelDetails();

    void updateHotelDetails(String id, String details);
}
