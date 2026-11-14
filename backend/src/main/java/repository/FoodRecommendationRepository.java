package repository;

import com.anemiaguard.model.FoodRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodRecommendationRepository extends JpaRepository<FoodRecommendation, Long> {
    List<FoodRecommendation> findByRegionIgnoreCase(String region);
}