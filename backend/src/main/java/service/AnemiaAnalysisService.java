package service;

import com.anemiaguard.model.AnemiaSeverity;
import org.springframework.stereotype.Service;

/**
 * Classifies anemia using WHO hemoglobin cut-offs (g/dL) by age group and sex.
 * Pregnancy is not modelled (pregnant women use different cut-offs).
 */
@Service
public class AnemiaAnalysisService {

    public AnemiaSeverity classify(double hb, int age, String gender) {
        double normalMin;  // >= this -> NORMAL
        double mildMin;    // >= this -> MILD
        double moderateMin; // >= this -> MODERATE, otherwise SEVERE

        if (age < 5) {
            normalMin = 11.0; mildMin = 10.0; moderateMin = 7.0;
        } else if (age < 12) {
            normalMin = 11.5; mildMin = 11.0; moderateMin = 8.0;
        } else if (age < 15) {
            normalMin = 12.0; mildMin = 11.0; moderateMin = 8.0;
        } else if ("male".equalsIgnoreCase(gender)) {
            normalMin = 13.0; mildMin = 11.0; moderateMin = 8.0;
        } else {
            normalMin = 12.0; mildMin = 11.0; moderateMin = 8.0;
        }

        if (hb >= normalMin) return AnemiaSeverity.NORMAL;
        if (hb >= mildMin) return AnemiaSeverity.MILD;
        if (hb >= moderateMin) return AnemiaSeverity.MODERATE;
        return AnemiaSeverity.SEVERE;
    }

    public String recommendedAction(AnemiaSeverity severity) {
        return switch (severity) {
            case NORMAL -> "Hemoglobin is in the normal range. Maintain an iron-rich balanced diet and re-screen at the next camp.";
            case MILD -> "Increase iron-rich foods with vitamin C, consider supplementation as advised by a health worker, and re-test in about 3 months.";
            case MODERATE -> "Refer to a medical officer for evaluation and iron supplementation. Follow-up testing within 4 to 6 weeks.";
            case SEVERE -> "URGENT: refer to the nearest hospital or PHC immediately for clinical evaluation and treatment.";
        };
    }
}