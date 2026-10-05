package io.github.blueberry44477.course_enrollment_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.blueberry44477.course_enrollment_system.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
