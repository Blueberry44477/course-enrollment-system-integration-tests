package io.github.blueberry44477.course_enrollment_system.service;

import io.github.blueberry44477.course_enrollment_system.model.Course;
import io.github.blueberry44477.course_enrollment_system.model.Student;

public interface EnrollmentPolicy {
    boolean isAllowed(Student student, Course course);
}
