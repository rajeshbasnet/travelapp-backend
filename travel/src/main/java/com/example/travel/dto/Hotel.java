package com.example.travel.dto;

import lombok.*;

import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Hotel {

    public String hotelId;
    public String title;
    public String primaryInfo;
    public String secondaryInfo;
    public HashMap<String, String> badge;
    public HashMap<String, String> bubbleRating;
    public String isSponsored;
    public String accentedLabel;
    public String provider;
    public String priceForDisplay;
    public String strikethroughPrice;
    public String priceDetails;
    public String priceSummary;
    public List<String> cardPhotos;
}
