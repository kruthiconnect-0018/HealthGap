package Controller;

import com.anemiaguard.dto.PatientReportResponse;
import com.anemiaguard.dto.RegisterPatientRequest;
import com.anemiaguard.dto.RegistrationResponse;
import com.anemiaguard.model.FoodRecommendation;
import com.anemiaguard.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PostMapping("/patients/register")
    public ResponseEntity<RegistrationResponse> register(@Valid @RequestBody RegisterPatientRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(patientService.register(request));
    }

    @GetMapping("/patients/{id}/report")
    public PatientReportResponse report(@PathVariable Long id) {
        return patientService.getReport(id);
    }

    @GetMapping("/food-suggestions")
    public List<FoodRecommendation> foodSuggestions(@RequestParam String region) {
        return patientService.foodsForRegion(region);
    }
}