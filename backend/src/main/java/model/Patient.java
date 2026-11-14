package model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "patients")
@Getter
@Setter
@NoArgsConstructor
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false, length = 20)
    private String gender;

    @Column(nullable = false, length = 100)
    private String region;

    @Column(nullable = false)
    private Double hemoglobinLevel; // g/dL

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AnemiaSeverity anemiaSeverity;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}