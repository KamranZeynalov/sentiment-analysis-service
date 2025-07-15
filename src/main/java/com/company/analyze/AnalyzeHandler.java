package com.company.analyze;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.company.analyze.model.AnalyzeRequest;
import com.company.analyze.model.AnalyzeResponse;
import com.company.analyze.model.ConfidenceScore;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.services.comprehend.ComprehendClient;
import software.amazon.awssdk.services.comprehend.model.DetectSentimentRequest;
import software.amazon.awssdk.services.comprehend.model.DetectSentimentResponse;
import software.amazon.awssdk.services.comprehend.model.LanguageCode;
import software.amazon.awssdk.services.comprehend.model.SentimentScore;

import java.util.HashMap;
import java.util.Map;

public class AnalyzeHandler implements RequestHandler<Map<String, Object>, Map<String, Object>> {

    private final ComprehendClient comprehend = ComprehendClient.create();
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public Map<String, Object> handleRequest(Map<String, Object> input, Context context) {

        try{
            String bodyJson = (String) input.get("body");
            AnalyzeRequest analyzeRequest = objectMapper.readValue(bodyJson, AnalyzeRequest.class);

            DetectSentimentRequest request = DetectSentimentRequest.builder()
                                                                   .text(analyzeRequest.getText())
                                                                   .languageCode(LanguageCode.EN)
                                                                   .build();

            DetectSentimentResponse response = comprehend.detectSentiment(request);

            SentimentScore score = response.sentimentScore();

            ConfidenceScore confidence = new ConfidenceScore(
                    score.positive(), score.negative(), score.neutral(), score.mixed()
            );

            AnalyzeResponse analyzeResponse = new AnalyzeResponse(
                    response.sentimentAsString(), confidence
            );

            return buildResponse(200, analyzeResponse);

        } catch (Exception ex) {
            return buildResponse(500, Map.of(
                    "error", "Internal server error: " + (ex.getMessage() != null ? ex.getMessage() : "Unknown error")
            ));
        }
    }


    private  Map<String, Object> buildResponse(int statusCode, Object body) {
        Map<String, Object> response = new HashMap<>();
        response.put("statusCode", statusCode);
        response.put("headers", Map.of("Content-Type", "application/json"));
        try {
            response.put("body", objectMapper.writeValueAsString(body));
        } catch (Exception e) {
            response.put("body", "{\"error\":\"Response serialization failed\"}");
        }
        return response;
    }
}
