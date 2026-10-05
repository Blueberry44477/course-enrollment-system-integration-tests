package io.github.blueberry44477.course_enrollment_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.blueberry44477.course_enrollment_system.model.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
