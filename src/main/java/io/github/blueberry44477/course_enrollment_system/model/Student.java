package io.github.blueberry44477.course_enrollment_system.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 40)
    private String code;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    private int entryScore;

    @Column(name = "is_regular", nullable = false)
    private boolean regular;

    @PrePersist
    @PreUpdate
    private void validate() {
        if (code == null || code.isEmpty() || code.length() > 40) {
            throw new IllegalArgumentException("Invalid code");
        }
        if (age < 14 || age > 100) {
            throw new IllegalArgumentException("Invalid age");
        }
        if (entryScore < 0 || entryScore > 100) {
            throw new IllegalArgumentException("Invalid entry score");
        }
    }
}
