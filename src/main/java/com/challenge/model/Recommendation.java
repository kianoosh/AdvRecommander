package com.challenge.model;

import java.util.List;

public class Recommendation {
    public int app_id;
    public String country_code;
    public List<Integer> recommended_advertiser_ids;

    public Recommendation(int app_id, String country_code, List<Integer> recommended_advertiser_ids) {
        this.app_id = app_id;
        this.country_code = country_code;
        this.recommended_advertiser_ids = recommended_advertiser_ids;
    }
}
