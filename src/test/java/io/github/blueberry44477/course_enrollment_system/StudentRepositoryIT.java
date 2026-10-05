package io.github.blueberry44477.course_enrollment_system;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mysql.MySQLContainer;

import io.github.blueberry44477.course_enrollment_system.model.Course;
import io.github.blueberry44477.course_enrollment_system.model.Student;
import io.github.blueberry44477.course_enrollment_system.repository.CourseRepository;
import io.github.blueberry44477.course_enrollment_system.repository.StudentRepository;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StudentRepositoryIT {

    @Container
    @ServiceConnection
    static final MySQLContainer mysql = new MySQLContainer("mysql:8.0");

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Test
    void shouldPersistAndRetrieveStudent() {
        Student student = new Student(null, "STU-" + UUID.randomUUID(), 20, 85, true);
        Student saved = studentRepository.save(student);
        
        assertNotNull(saved.getId());
        
        Student retrieved = studentRepository.findById(saved.getId()).orElse(null);
        assertNotNull(retrieved);
        assertEquals(student.getCode(), retrieved.getCode());
    }

    @Test
    void shouldUpdateCourseCapacity() {
        Course course = courseRepository.save(new Course(null, "CRS-" + UUID.randomUUID(), 5000, 10, 1));
        
        course.setCapacity(20);
        courseRepository.save(course);
        
        Course updated = courseRepository.findById(course.getId()).orElseThrow();
        assertEquals(20, updated.getCapacity());
    }
}
