package com.company.analyze.model;

public class AnalyzeResponse {

    private String sentiment;
    private ConfidenceScore confidence;

    public AnalyzeResponse(String sentiment, ConfidenceScore confidence) {
        this.sentiment = sentiment;
        this.confidence = confidence;
    }

    public String getSentiment() { return sentiment; }
    public ConfidenceScore getConfidence() { return confidence; }
}
