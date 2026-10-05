package io.github.blueberry44477.course_enrollment_system.repository;

import io.github.blueberry44477.course_enrollment_system.dto.EnrollmentStatus;
import io.github.blueberry44477.course_enrollment_system.model.Enrollment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.courseId = :courseId AND e.status IN ('ENROLLED', 'COMPLETED')")
    int countOccupied(@Param("courseId") Long courseId);

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Enrollment e WHERE e.studentId = :studentId AND e.courseId = :courseId AND e.status IN ('ENROLLED', 'COMPLETED')")
    boolean hasEnrollment(@Param("studentId") Long studentId, @Param("courseId") Long courseId);

    @Modifying
    @Query("UPDATE Enrollment e SET e.status = :next, e.refund = :refund WHERE e.id = :id AND e.status = 'ENROLLED'")
    int updateStatus(@Param("id") Long id, @Param("next") EnrollmentStatus next, @Param("refund") long refund);
}
