package com.example.travel.elastic;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import com.example.travel.Util.Util;
import com.example.travel.dto.Hotel;
import com.example.travel.services.HotelService;
import jakarta.json.Json;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class ReIndex {

    private final ElasticsearchClient esClient;

    private final MongoTemplate mongoTemplate;

    public void indexHotels() throws IOException {
        List<Hotel> hotels = mongoTemplate.findAll(Hotel.class, Util.HOTELS);

        BulkRequest.Builder bulkRequest = new BulkRequest.Builder();

        for (Hotel hotel : hotels) {
            bulkRequest.operations(bulk -> bulk
                    .index(idx -> idx
                            .id(hotel.getHotelId())
                            .index(Util.HOTELS.toLowerCase())
                            .document(hotel)));
        }

        BulkResponse bulkResponse = esClient.bulk(bulkRequest.build());

        if(bulkResponse.errors()) {
            for (BulkResponseItem item : bulkResponse.items()) {
                log.error(item.error());
            }
        }

    }
}
