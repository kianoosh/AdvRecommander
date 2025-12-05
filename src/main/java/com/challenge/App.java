package com.challenge;

import com.challenge.model.Impression;
import com.challenge.model.Click;
import java.util.Map;
import java.util.List;

public class App {
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("Usage: java -jar advert-metrics-challenge.jar <impressions.json> <clicks.json>");
            System.exit(1);
        }
        String impressionsFile = args[0];
        String clicksFile = args[1];

        List<Impression> impressions = com.challenge.util.JsonLoader.loadImpressions(impressionsFile);
        List<Click> clicks = com.challenge.util.JsonLoader.loadClicks(clicksFile);

        // Map impression id to Impression (allow all impressions with non-null id)
        Map<String, Impression> impressionMap = impressions.stream()
                .filter(i -> i.id != null)
                .collect(java.util.stream.Collectors.toMap(i -> i.id, i -> i, (a, b) -> a));

        // Metrics by app_id and country_code (allow null country_code)
        Map<String, com.challenge.model.Metrics> metricsMap = new java.util.HashMap<>();
        for (Impression imp : impressions) {
            if (imp.id == null)
                continue;
            String key = imp.app_id + "_" + String.valueOf(imp.country_code);
            metricsMap.putIfAbsent(key, new com.challenge.model.Metrics(imp.app_id, imp.country_code));
            metricsMap.get(key).impressions++;
        }
        for (Click click : clicks) {
            Impression imp = impressionMap.get(click.impression_id);
            if (imp == null)
                continue;
            String key = imp.app_id + "_" + String.valueOf(imp.country_code);
            com.challenge.model.Metrics m = metricsMap.get(key);
            if (m != null) {
                m.clicks++;
                m.revenue += click.revenue;
            }
        }
        // Write metrics output
        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        java.util.List<com.challenge.model.Metrics> metricsList = new java.util.ArrayList<>(metricsMap.values());
        mapper.writerWithDefaultPrettyPrinter().writeValue(new java.io.File("metrics_output.json"), metricsList);

        // Recommendation logic
        // Map: app_id + country_code -> advertiser_id -> [impression count, revenue]
        Map<String, Map<Integer, double[]>> advStats = new java.util.HashMap<>();
        for (Impression imp : impressions) {
            if (imp.id == null)
                continue;
            String key = imp.app_id + "_" + String.valueOf(imp.country_code);
            advStats.putIfAbsent(key, new java.util.HashMap<>());
            advStats.get(key).putIfAbsent(imp.advertiser_id, new double[] { 0, 0.0 });
            advStats.get(key).get(imp.advertiser_id)[0]++;
        }
        for (Click click : clicks) {
            Impression imp = impressionMap.get(click.impression_id);
            if (imp == null)
                continue;
            String key = imp.app_id + "_" + String.valueOf(imp.country_code);
            Map<Integer, double[]> advMap = advStats.get(key);
            if (advMap != null) {
                double[] stats = advMap.get(imp.advertiser_id);
                if (stats != null) {
                    stats[1] += click.revenue;
                }
            }
        }
        java.util.List<com.challenge.model.Recommendation> recs = new java.util.ArrayList<>();
        for (String key : advStats.keySet()) {
            Map<Integer, double[]> advMap = advStats.get(key);
            java.util.List<Integer> topAdvertisers = advMap.entrySet().stream()
                    .sorted((a, b) -> Double.compare(
                            b.getValue()[1] / Math.max(1, b.getValue()[0]),
                            a.getValue()[1] / Math.max(1, a.getValue()[0])))
                    .limit(5)
                    .map(java.util.Map.Entry::getKey)
                    .collect(java.util.stream.Collectors.toList());
            String[] parts = key.split("_", 2);
            int appId = Integer.parseInt(parts[0]);
            String countryCode = parts[1];
            recs.add(new com.challenge.model.Recommendation(appId, countryCode, topAdvertisers));
        }
        mapper.writerWithDefaultPrettyPrinter().writeValue(new java.io.File("recommendations_output.json"), recs);
        System.out
                .println("Processing complete. Output written to metrics_output.json and recommendations_output.json");
    }
}
