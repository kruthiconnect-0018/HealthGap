package model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "food_recommendations", indexes = @Index(name = "idx_food_region", columnList = "region"))
@Getter
@Setter
@NoArgsConstructor
public class FoodRecommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String region;

    @Column(nullable = false)
    private String foodItem;

    @Column(nullable = false, length = 20)
    private String ironContentCategory; // HIGH | MEDIUM | VITAMIN_C

    public FoodRecommendation(String region, String foodItem, String ironContentCategory) {
        this.region = region;
        this.foodItem = foodItem;
        this.ironContentCategory = ironContentCategory;
    }
}