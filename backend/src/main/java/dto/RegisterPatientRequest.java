package dto;

import jakarta.validation.constraints.*;

public record RegisterPatientRequest(
        @NotBlank(message = "Full name is required") String fullName,
        @NotNull(message = "Age is required") @Min(value = 0, message = "Age must be 0 or more") @Max(value = 120, message = "Age must be 120 or less") Integer age,
        @NotBlank(message = "Gender is required") String gender,
        @NotBlank(message = "Region is required") String region,
        @NotNull(message = "Hemoglobin level is required") @DecimalMin(value = "1.0", message = "Hb must be at least 1.0 g/dL") @DecimalMax(value = "25.0", message = "Hb must be at most 25.0 g/dL") Double hemoglobinLevel
) {
}