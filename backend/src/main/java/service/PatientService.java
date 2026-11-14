package service;

import com.anemiaguard.dto.*;
import com.anemiaguard.model.AnemiaSeverity;
import com.anemiaguard.model.FoodRecommendation;
import com.anemiaguard.model.Patient;
import com.anemiaguard.repository.FoodRecommendationRepository;
import com.anemiaguard.repository.PatientRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final FoodRecommendationRepository foodRepository;
    private final AnemiaAnalysisService analysisService;
    private final CopilotIntegrationService copilotService;

    public RegistrationResponse register(RegisterPatientRequest req) {
        Patient p = new Patient();
        p.setFullName(req.fullName().trim());
        p.setAge(req.age());
        p.setGender(req.gender());
        p.setRegion(req.region());
        p.setHemoglobinLevel(req.hemoglobinLevel());
        AnemiaSeverity severity = analysisService.classify(req.hemoglobinLevel(), req.age(), req.gender());
        p.setAnemiaSeverity(severity);
        p = patientRepository.save(p);

        return new RegistrationResponse(p.getId(), p.getFullName(), p.getHemoglobinLevel(),
                severity, severity.getLabel(), analysisService.recommendedAction(severity));
    }

    public PatientReportResponse getReport(Long id) {
        Patient p = patientRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not found: " + id));
        List<FoodRecommendation> foods = foodsForRegion(p.getRegion());
        AiReport ai = copilotService.generateReport(p, foods);

        return new PatientReportResponse(p.getId(), p.getFullName(), p.getAge(), p.getGender(), p.getRegion(),
                p.getHemoglobinLevel(), p.getAnemiaSeverity(), p.getAnemiaSeverity().getLabel(),
                p.getCreatedAt(), foods, ai.summary(), ai.dietPlan(), ai.source());
    }

    public List<FoodRecommendation> foodsForRegion(String region) {
        List<FoodRecommendation> foods = foodRepository.findByRegionIgnoreCase(region);
        return foods.isEmpty() ? foodRepository.findByRegionIgnoreCase("General") : foods;
    }
}