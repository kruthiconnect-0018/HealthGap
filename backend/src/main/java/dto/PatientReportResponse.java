package dto;

import com.anemiaguard.model.AnemiaSeverity;
import com.anemiaguard.model.FoodRecommendation;

import java.time.LocalDateTime;
import java.util.List;

public record PatientReportResponse(
        Long patientId,
        String fullName,
        Integer age,
        String gender,
        String region,
        Double hemoglobinLevel,
        AnemiaSeverity severity,
        String severityLabel,
        LocalDateTime registeredAt,
        List<FoodRecommendation> foodRecommendations,
        String aiSummary,
        String aiDietPlan,
        String aiSource
) {
}