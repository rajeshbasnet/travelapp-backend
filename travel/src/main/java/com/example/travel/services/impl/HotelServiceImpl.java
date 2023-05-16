package com.example.travel.services.impl;

import com.example.travel.Util.Util;
import com.example.travel.elastic.ReIndex;
import com.example.travel.services.HotelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bson.Document;
import org.json.JSONObject;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Log4j2
public class HotelServiceImpl implements HotelService {

    private final MongoTemplate mongoTemplate;

    private final ReIndex reIndex;

    @Override
    public void indexHotelDetails(String detailsInJson) {
        log.info(detailsInJson);
        Document document = Document.parse(detailsInJson);
        mongoTemplate.insert(document, "HOTEL_DETAILS_V1");
    }

    @Override
    public void indexHotel(String hotel) throws IOException {
        log.info(hotel);
        Document document = Document.parse(hotel);
        mongoTemplate.insert(document, Util.HOTELS);

        reIndex.indexHotels();
    }


    @Override
    public String fetchAllHotels() {
        return mongoTemplate.findAll(Document.class, Util.HOTELS)
                .stream()
                .map(document -> new JSONObject(document.toJson()))
                .toList()
                .toString();
    }

    @Override
    public String fetchHotelById(String id) {
        log.info(id);
        Query query = new Query();
        query.addCriteria(Criteria.where("id")
                .is(id));

        return mongoTemplate.find(query, Document.class, Util.HOTEL_DETAILS_V1)
                .stream()
                .map(document -> new JSONObject(document.toJson()))
                .findFirst()
                .get()
                .toString();
    }

    @Override
    public String fetchHotelByColumn(String column, String value) {
        log.info(column);
        Query query = new Query();
        query.addCriteria(Criteria.where(column)
                .is(value));
        log.info(query);

        String hotels = mongoTemplate.find(query, Document.class, Util.HOTELS)
                .stream()
                .map(document -> new JSONObject(document.toJson()))
                .toList()
                .toString();
        log.debug(hotels);

        return hotels;
    }

    @Override
    public String fetchHotelByUsername(String username) {
        log.info(username);
        Query query = new Query();
        query.addCriteria(Criteria.where("username")
                .is(username));

        return mongoTemplate.find(query, Document.class, Util.HOTEL_DETAILS_V1)
                .stream()
                .map(document -> new JSONObject(document.toJson()))
                .findFirst()
                .get()
                .toString();
    }

    @Override
    public String fetchAllHotelDetails() {
        return mongoTemplate.findAll(Document.class, Util.HOTEL_DETAILS)
                .stream()
                .map(document -> new JSONObject(document.toJson()))
                .toList()
                .toString();
    }


    @Override
    public void updateHotelDetails(String id, String details) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id")
                .is(id));

        Document document = Document.parse(details);
        Update update = Update.fromDocument(document);
        mongoTemplate.updateFirst(query, update, Util.HOTEL_DETAILS_V1);
    }

}
