package com.example.travel.services.impl;

import com.example.travel.Util.Util;
import com.example.travel.services.HotelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class HotelServiceImpl implements HotelService {

    private final MongoTemplate mongoTemplate;

    @Override
    public void indexHotelDetails(String detailsInJson) {
        log.info(detailsInJson);
        Document document = Document.parse(detailsInJson);
        mongoTemplate.insert(document, "HOTEL_DETAILS_V1");
    }

    @Override
    public String fetchAllHotels() {
        return mongoTemplate.findAll(Document.class, Util.HOTELS)
                .stream()
                .map(document -> new JSONObject(document.toJson()))
                .limit(10)
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
    public String fetchAllHotelDetails() {
        return mongoTemplate.findAll(Document.class, Util.HOTEL_DETAILS)
                .stream()
                .map(document -> new JSONObject(document.toJson()))
                .toList()
                .toString();
    }

}
