package dto;

import com.anemiaguard.model.AnemiaSeverity;

public record RegistrationResponse(
        Long patientId,
        String fullName,
        Double hemoglobinLevel,
        AnemiaSeverity severity,
        String severityLabel,
        String recommendedAction
) {
}