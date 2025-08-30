package com.example.FreelanceX2.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmbeddingService {

    private static final Logger log = LoggerFactory.getLogger(EmbeddingService.class);

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    @Value("${gemini.model.name:text-embedding-004}")
    private String modelName;

    private final WebClient webClient;

    public EmbeddingService() {
        this.webClient = WebClient.builder().build();
    }

    public List<Double> generateEmbedding(String text) {
        try {
            if (apiKey == null || apiKey.isBlank() || apiKey.equals("${API_KEY}")) {
                throw new IllegalStateException("Gemini API key is not configured. Set 'gemini.api.key' or environment variable API_KEY.");
            }
            if (apiUrl == null || apiUrl.isBlank()) {
                throw new IllegalStateException("Gemini API URL is not configured. Set 'gemini.api.url'.");
            }
            // Normalize model to include 'models/' prefix as required by API
            String model = modelName != null && modelName.startsWith("models/") ? modelName : "models/" + (modelName == null || modelName.isBlank() ? "text-embedding-004" : modelName);

            // Determine effective embedding endpoint URL
            String effectiveUrl = apiUrl;
            boolean looksLikeGenerateContent = apiUrl.contains(":generateContent");
            boolean isEmbedText = apiUrl.contains(":embedText");
            boolean isEmbedContent = apiUrl.contains(":embedContent");
            if (looksLikeGenerateContent || (!isEmbedText && !isEmbedContent)) {
                // Override to the correct embedding endpoint (embedContent by default)
                effectiveUrl = "https://generativelanguage.googleapis.com/v1beta/" + model + ":embedContent";
                log.warn("Overriding misconfigured gemini.api.url to embedding endpoint: {}", effectiveUrl);
                isEmbedContent = true;
                isEmbedText = false;
            }

            // Build request body depending on endpoint
            boolean useEmbedContent = isEmbedContent;
            Map<String, Object> requestBody;
            if (useEmbedContent) {
                // embedContent expects: { "model": "models/...", "content": { "parts": [ { "text": "..." } ] } }
                requestBody = Map.of(
                        "model", model,
                        "content", Map.of(
                                "parts", List.of(Map.of("text", text))
                        )
                );
                log.info("Embedding request using embedContent endpoint: {}", effectiveUrl);
            } else {
                // embedText expects: { "model": "models/...", "text": "..." }
                requestBody = Map.of(
                        "model", model,
                        "text", text
                );
                log.info("Embedding request using embedText endpoint: {}", effectiveUrl);
            }

            // Make API call to Gemini (first attempt)
            Map<String, Object> responseBody;
            try {
                responseBody = sendGeminiRequest(effectiveUrl, requestBody);
            } catch (WebClientResponseException wcre) {
                String errBody = wcre.getResponseBodyAsString();
                // Adaptive retry: switch payload if server complains about unknown field
                boolean is400 = wcre.getStatusCode().value() == 400;
                if (is400 && errBody != null && errBody.contains("Unknown name \"text\"")) {
                    // Retry with embedContent body
                    Map<String, Object> altBody = Map.of(
                            "model", model,
                            "content", Map.of(
                                    "parts", List.of(Map.of("text", text))
                            )
                    );
                    log.warn("Gemini 400 Unknown 'text' — retrying with embedContent body");
                    responseBody = sendGeminiRequest("https://generativelanguage.googleapis.com/v1beta/" + model + ":embedContent", altBody);
                } else if (is400 && errBody != null && errBody.contains("Unknown name \"content\"")) {
                    // Retry with embedText body
                    Map<String, Object> altBody = Map.of(
                            "model", model,
                            "text", text
                    );
                    log.warn("Gemini 400 Unknown 'content' — retrying with embedText body");
                    responseBody = sendGeminiRequest("https://generativelanguage.googleapis.com/v1beta/" + model + ":embedText", altBody);
                } else {
                    throw wcre;
                }
            }

            // Expected shape for embedText: { "embedding": { "value": [ ... ] } }
            if (responseBody != null && responseBody.containsKey("embedding")) {
                Map<String, Object> embedding = (Map<String, Object>) responseBody.get("embedding");
                if (embedding.containsKey("value")) {
                    return (List<Double>) embedding.get("value");
                }
            }

            // Fallback for embedContent response: { "embedding": { "values": [ ... ] } }
            if (responseBody != null && responseBody.containsKey("embedding")) {
                Map<String, Object> embedding = (Map<String, Object>) responseBody.get("embedding");
                if (embedding.containsKey("values")) {
                    return (List<Double>) embedding.get("values");
                }
            }

            // Some errors come as {"error": {...}}
            if (responseBody != null && responseBody.containsKey("error")) {
                throw new RuntimeException("Gemini API error: " + responseBody.get("error"));
            }

            throw new RuntimeException("Failed to generate embedding: Invalid response format from Gemini");
            
        } catch (WebClientResponseException wcre) {
            String errBody = wcre.getResponseBodyAsString();
            throw new RuntimeException("Gemini HTTP " + wcre.getStatusCode().value() + ": " + errBody, wcre);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate embedding for text: " + text, e);
        }
    }

    // Execute POST to Gemini and return parsed JSON body as Map
    private Map<String, Object> sendGeminiRequest(String url, Map<String, Object> body) {
        Mono<Map> response = webClient.post()
                .uri(url + "?key=" + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class);
        return response.block();
    }

    public double calculateCosineSimilarity(List<Double> vector1, List<Double> vector2) {
        if (vector1.size() != vector2.size()) {
            throw new IllegalArgumentException("Vectors must have the same dimension");
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

    public String createEmbeddingText(String title, String description, List<String> skills, String domain) {
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
