package io.github.blueberry44477.course_enrollment_system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentRequest {

    @NotBlank
    @Size(max = 40)
    private String reference;

    @NotNull
    @Positive
    private Long studentId;

    @NotNull
    @Positive
    private Long courseId;

    private boolean couponValid;
}
