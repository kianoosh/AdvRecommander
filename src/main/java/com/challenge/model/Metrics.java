package com.challenge.model;

public class Metrics {
    public int app_id;
    public String country_code;
    public int impressions;
    public int clicks;
    public double revenue;

    public Metrics(int app_id, String country_code) {
        this.app_id = app_id;
        this.country_code = country_code;
        this.impressions = 0;
        this.clicks = 0;
        this.revenue = 0.0;
    }
}
