package com.example.travel.services;

public interface HotelService {

    void indexHotelDetails(String detailsInJson);

    String fetchAllHotels();

    String fetchHotelById(String id);

    String fetchAllHotelDetails();
}
