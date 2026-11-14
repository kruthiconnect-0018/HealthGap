package config;

import com.anemiaguard.model.FoodRecommendation;
import com.anemiaguard.repository.FoodRecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final FoodRecommendationRepository repo;
    private final List<FoodRecommendation> batch = new ArrayList<>();

    @Override
    public void run(String... args) {
        if (repo.count() > 0) return;

        seed("Karnataka", "Ragi mudde|HIGH", "Avarekalu (hyacinth beans)|HIGH", "Harive soppu (amaranth leaves)|HIGH",
                "Bella (jaggery)|HIGH", "Ellu (sesame) chikki|HIGH", "Nellikai (amla)|VITAMIN_C");
        seed("Tamil Nadu", "Keerai (spinach/greens)|HIGH", "Ragi koozh|HIGH", "Kambu (pearl millet) porridge|HIGH",
                "Kollu (horse gram)|HIGH", "Karuppu ulundu (black urad) kanji|HIGH", "Elumichai (lemon)|VITAMIN_C");
        seed("Kerala", "Cheera thoran (amaranth)|HIGH", "Muringa ila (drumstick leaves)|HIGH", "Ragi puttu|HIGH",
                "Kadala (black chickpeas)|HIGH", "Mathi (sardine) curry|HIGH", "Nellikka (amla)|VITAMIN_C");
        seed("Maharashtra", "Bajra bhakri|HIGH", "Jowar bhakri|MEDIUM", "Matki (moth bean) usal|HIGH",
                "Palak bhaji|HIGH", "Til-gul laddu|HIGH", "Lemon|VITAMIN_C");
        seed("Punjab", "Sarson da saag|HIGH", "Bathua paratha|HIGH", "Rajma|HIGH",
                "Chole|HIGH", "Gur (jaggery)|HIGH", "Amla|VITAMIN_C");
        seed("West Bengal", "Pui/Palong shak (leafy greens)|HIGH", "Masoor dal|HIGH", "Chhola (chickpeas)|HIGH",
                "Small fish (mourala)|HIGH", "Nolen gur (date palm jaggery)|HIGH", "Gondhoraj lebu (lime)|VITAMIN_C");
        seed("Gujarat", "Methi thepla|HIGH", "Bajra rotla|HIGH", "Chana (chickpeas)|HIGH",
                "Til chikki|HIGH", "Dates (khajoor)|HIGH", "Amla chutney|VITAMIN_C");
        seed("Rajasthan", "Bajra roti|HIGH", "Moong dal|MEDIUM", "Bathua|HIGH",
                "Gur (jaggery)|HIGH", "Khajoor (dates)|HIGH", "Lemon|VITAMIN_C");
        seed("General", "Spinach and other leafy greens|HIGH", "Lentils and pulses|HIGH", "Jaggery|HIGH",
                "Beetroot|MEDIUM", "Dates and raisins|HIGH", "Guava, lemon, amla|VITAMIN_C");

        repo.saveAll(batch);
    }

    private void seed(String region, String... items) {
        for (String item : items) {
            String[] parts = item.split("\\|");
            batch.add(new FoodRecommendation(region, parts[0], parts[1]));
        }
    }
}