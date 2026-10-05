package io.github.blueberry44477.course_enrollment_system.model;

import io.github.blueberry44477.course_enrollment_system.dto.EnrollmentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "enrollments")
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 40)
    private String reference;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EnrollmentStatus status;

    @Column(nullable = false)
    private long total;

    @Column(nullable = false)
    private long prepaid;

    @Column(nullable = false)
    private long refund;

    @Column(name = "coupon_valid", nullable = false)
    private boolean couponValid;

    @PrePersist
    @PreUpdate
    private void validate() {
        if (reference == null || reference.isEmpty() || reference.length() > 40) {
            throw new IllegalArgumentException("Invalid reference");
        }
        if (studentId == null || studentId <= 0) {
            throw new IllegalArgumentException("Invalid studentId");
        }
        if (courseId == null || courseId <= 0) {
            throw new IllegalArgumentException("Invalid courseId");
        }
    }
}
