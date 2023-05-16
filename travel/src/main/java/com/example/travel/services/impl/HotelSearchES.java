package com.example.travel.services.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.example.travel.Util.Util;
import com.example.travel.dto.Hotel;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bson.Document;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Log4j2
public class HotelSearchES {

    private final ElasticsearchClient esClient;

    public String searchHotels() throws IOException {
        SearchResponse<Document> response = esClient.search(search -> search
                .index(Util.HOTELS.toLowerCase())
                .query(q -> q
                        .matchAll(m -> m.boost(2.0F))), Document.class);

        return response.hits().hits()
                .stream()
                .map(Hit::source)
                .filter(Objects::nonNull)
                .map(Document::toJson)
                .map(JSONObject::new)
                .toList()
                .toString();

    }
}
