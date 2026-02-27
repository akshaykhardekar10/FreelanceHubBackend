package com.example.FreelanceX2.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@Service
public class EmbeddingService {

    private static final Logger log = LoggerFactory.getLogger(EmbeddingService.class);

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    @Value("${gemini.model.name:text-embedding-004}")
    private String modelName;

    private final RestTemplate restTemplate;

    public EmbeddingService() {
        this.restTemplate = new RestTemplate();
    }

    @SuppressWarnings("unchecked")
    public List<Double> generateEmbedding(String text) {
        try {
            if (apiKey == null || apiKey.isBlank() || apiKey.equals("${API_KEY}")) {
                throw new IllegalStateException("Gemini API key is not configured.");
            }

            if (apiUrl == null || apiUrl.isBlank()) {
                throw new IllegalStateException("Gemini API URL is not configured.");
            }

            String model = (modelName != null && modelName.startsWith("models/"))
                    ? modelName
                    : "models/" + (modelName == null || modelName.isBlank()
                    ? "text-embedding-004"
                    : modelName);

            String effectiveUrl = apiUrl;

            boolean looksLikeGenerateContent = apiUrl.contains(":generateContent");
            boolean isEmbedText = apiUrl.contains(":embedText");
            boolean isEmbedContent = apiUrl.contains(":embedContent");

            if (looksLikeGenerateContent || (!isEmbedText && !isEmbedContent)) {
                effectiveUrl = "https://generativelanguage.googleapis.com/v1beta/"
                        + model + ":embedContent";
                log.warn("Overriding misconfigured gemini.api.url to embedding endpoint: {}", effectiveUrl);
                isEmbedContent = true;
                isEmbedText = false;
            }

            boolean useEmbedContent = isEmbedContent;

            Map<String, Object> requestBody;

            if (useEmbedContent) {
                requestBody = Map.of(
                        "model", model,
                        "content", Map.of(
                                "parts", List.of(Map.of("text", text))
                        )
                );
                log.info("Embedding request using embedContent endpoint: {}", effectiveUrl);
            } else {
                requestBody = Map.of(
                        "model", model,
                        "text", text
                );
                log.info("Embedding request using embedText endpoint: {}", effectiveUrl);
            }

            Map<String, Object> responseBody;

            try {
                responseBody = sendGeminiRequest(effectiveUrl, requestBody);
            } catch (HttpStatusCodeException ex) {

                String errBody = ex.getResponseBodyAsString();
                boolean is400 = ex.getStatusCode().value() == 400;

                if (is400 && errBody != null && errBody.contains("Unknown name \"text\"")) {

                    Map<String, Object> altBody = Map.of(
                            "model", model,
                            "content", Map.of(
                                    "parts", List.of(Map.of("text", text))
                            )
                    );

                    log.warn("Gemini 400 Unknown 'text' — retrying with embedContent body");

                    responseBody = sendGeminiRequest(
                            "https://generativelanguage.googleapis.com/v1beta/"
                                    + model + ":embedContent",
                            altBody
                    );

                } else if (is400 && errBody != null && errBody.contains("Unknown name \"content\"")) {

                    Map<String, Object> altBody = Map.of(
                            "model", model,
                            "text", text
                    );

                    log.warn("Gemini 400 Unknown 'content' — retrying with embedText body");

                    responseBody = sendGeminiRequest(
                            "https://generativelanguage.googleapis.com/v1beta/"
                                    + model + ":embedText",
                            altBody
                    );

                } else {
                    throw ex;
                }
            }

            if (responseBody != null && responseBody.containsKey("embedding")) {
                Map<String, Object> embedding =
                        (Map<String, Object>) responseBody.get("embedding");

                if (embedding.containsKey("value")) {
                    return (List<Double>) embedding.get("value");
                }

                if (embedding.containsKey("values")) {
                    return (List<Double>) embedding.get("values");
                }
            }

            if (responseBody != null && responseBody.containsKey("error")) {
                throw new RuntimeException("Gemini API error: " + responseBody.get("error"));
            }

            throw new RuntimeException("Invalid response format from Gemini");

        } catch (HttpStatusCodeException ex) {
            throw new RuntimeException(
                    "Gemini HTTP " + ex.getStatusCode().value()
                            + ": " + ex.getResponseBodyAsString(), ex
            );
        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to generate embedding for text: " + text, e
            );
        }
    }

    private Map<String, Object> sendGeminiRequest(String url, Map<String, Object> body) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                url + "?key=" + apiKey,
                HttpMethod.POST,
                request,
                Map.class
        );

        return response.getBody();
    }

    public double calculateCosineSimilarity(List<Double> vector1, List<Double> vector2) {

        if (vector1.size() != vector2.size()) {
            throw new IllegalArgumentException("Vectors must have same dimension");
        }

        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        for (int i = 0; i < vector1.size(); i++) {
            dotProduct += vector1.get(i) * vector2.get(i);
            norm1 += Math.pow(vector1.get(i), 2);
            norm2 += Math.pow(vector2.get(i), 2);
        }

        if (norm1 == 0.0 || norm2 == 0.0) {
            return 0.0;
        }

        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    public String createEmbeddingText(
            String title,
            String description,
            List<String> skills,
            String domain
    ) {

        StringBuilder text = new StringBuilder();

        if (title != null && !title.trim().isEmpty()) {
            text.append("Title: ").append(title).append(". ");
        }

        if (description != null && !description.trim().isEmpty()) {
            text.append("Description: ").append(description).append(". ");
        }

        if (domain != null && !domain.trim().isEmpty()) {
            text.append("Domain: ").append(domain).append(". ");
        }

        if (skills != null && !skills.isEmpty()) {
            text.append("Skills: ").append(String.join(", ", skills)).append(".");
        }

        return text.toString().trim();
    }
}