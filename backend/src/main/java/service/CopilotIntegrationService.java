package service;

import com.anemiaguard.dto.AiReport;
import com.anemiaguard.model.FoodRecommendation;
import com.anemiaguard.model.Patient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import Exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CopilotIntegrationService {

    private static final Logger log = LoggerFactory.getLogger(CopilotIntegrationService.class);

    private static final String SYSTEM_PROMPT = """
            You are a public-health assistant supporting community anemia screening camps in India.
            Write clear, compassionate, plain-language guidance a health worker can read to a patient.
            You are NOT diagnosing; you summarise camp screening results and suggest dietary support.
            For MODERATE or SEVERE results, always advise referral to a doctor or hospital.
            Respond ONLY with a JSON object: {"summary": "<string>", "dietPlan": "<string>"}.
            "dietPlan" must be a single string with line breaks (a 1-day sample plan plus 3 practical tips),
            using the regional foods provided and pairing iron-rich foods with vitamin C sources.
            """;

    private final RestClient restClient;
    private final ObjectMapper mapper;
    private final String provider;
    private final String apiUrl;
    private final String apiKey;
    private final String model;

    public CopilotIntegrationService(ObjectMapper mapper,
                                     @Value("${copilot.api.provider}") String provider,
                                     @Value("${copilot.api.url}") String apiUrl,
                                     @Value("${copilot.api.key}") String apiKey,
                                     @Value("${copilot.api.model}") String model) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(30000);
        this.restClient = RestClient.builder().requestFactory(factory).build();
        this.mapper = mapper;
        this.provider = provider;
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
        this.model = model;
    }

    public AiReport generateReport(Patient patient, List<FoodRecommendation> foods) {
        if (apiKey == null || apiKey.isBlank()) {
            return fallback(patient, foods);
        }
        try {
            Map<String, Object> body = Map.of(
                    "model", model,
                    "temperature", 0.4,
                    "response_format", Map.of("type", "json_object"),
                    "messages", List.of(
                            Map.of("role", "system", "content", SYSTEM_PROMPT),
                            Map.of("role", "user", "content", buildUserPrompt(patient, foods))
                    )
            );

            RestClient.RequestBodySpec request = restClient.post()
                    .uri(apiUrl)
                    .contentType(MediaType.APPLICATION_JSON);
            if ("azure".equalsIgnoreCase(provider)) {
                request = request.header("api-key", apiKey);
            } else {
                request = request.header("Authorization", "Bearer " + apiKey);
            }

            JsonNode response = request.body(body).retrieve().body(JsonNode.class);
            String content = response.path("choices").path(0).path("message").path("content").asText("");
            JsonNode parsed = mapper.readTree(stripFences(content));

            String summary = text(parsed.path("summary"));
            String dietPlan = text(parsed.path("dietPlan"));
            if (summary.isBlank() || dietPlan.isBlank()) {
                throw new IllegalStateException("AI response missing summary or dietPlan");
            }
            return new AiReport(summary, dietPlan, "AI");
        } catch (Exception e) {
            log.warn("Copilot call failed, using fallback report: {}", e.getMessage());
            return fallback(patient, foods);
        }
    }

    /** Structured prompt payload built from patient metrics + regional data. */
    String buildUserPrompt(Patient p, List<FoodRecommendation> foods) {
        String foodList = foods.stream()
                .map(f -> f.getFoodItem() + " (" + f.getIronContentCategory() + ")")
                .collect(Collectors.joining("; "));
        return """
                PATIENT SCREENING DATA
                - Name: %s
                - Age: %d years
                - Gender: %s
                - Region: %s
                - Hemoglobin: %s g/dL
                - Anemia severity (WHO-based): %s

                REGIONAL FOODS AVAILABLE (iron category)
                %s

                TASK
                1. "summary": 3-4 sentences explaining the result, what it means, and next steps.
                2. "dietPlan": a 1-day sample meal plan (breakfast, lunch, snack, dinner) built from the regional foods above,
                   followed by 3 tips (e.g. pair with vitamin C, avoid tea/coffee with meals, cook in iron cookware).
                """.formatted(p.getFullName(), p.getAge(), p.getGender(), p.getRegion(),
                p.getHemoglobinLevel(), p.getAnemiaSeverity().getLabel(), foodList);
    }

    private AiReport fallback(Patient p, List<FoodRecommendation> foods) {
        String items = foods.stream().map(FoodRecommendation::getFoodItem).limit(6)
                .collect(Collectors.joining(", "));
        String summary = p.getFullName() + " (" + p.getAge() + ", " + p.getGender() + ") from " + p.getRegion()
                + " has a hemoglobin of " + p.getHemoglobinLevel() + " g/dL, classified as "
                + p.getAnemiaSeverity().getLabel() + ". "
                + (p.getAnemiaSeverity().ordinal() >= 2
                ? "Please consult a doctor or visit the nearest health centre soon."
                : "Focus on iron-rich foods and re-screen at the next camp.");
        String plan = "Suggested regional foods: " + items + ".\n"
                + "Tips:\n"
                + "1. Pair iron-rich foods with vitamin C (lemon, amla, guava).\n"
                + "2. Avoid tea or coffee within an hour of meals.\n"
                + "3. Cook in iron cookware where possible.";
        return new AiReport(summary, plan, "FALLBACK");
    }

    private String text(JsonNode node) {
        if (node.isMissingNode() || node.isNull()) return "";
        return node.isTextual() ? node.asText() : node.toString();
    }

    private String stripFences(String s) {
        return s.trim().replaceAll("^```(?:json)?\\s*", "").replaceAll("\\s*```$", "").trim();
    }
}