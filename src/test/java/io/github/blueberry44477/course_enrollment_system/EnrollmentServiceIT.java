package io.github.blueberry44477.course_enrollment_system;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mysql.MySQLContainer;
import org.wiremock.integrations.testcontainers.WireMockContainer;

import com.github.tomakehurst.wiremock.client.WireMock;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

import io.github.blueberry44477.course_enrollment_system.dto.EnrollmentRequest;
import io.github.blueberry44477.course_enrollment_system.dto.EnrollmentStatus;
import io.github.blueberry44477.course_enrollment_system.model.Course;
import io.github.blueberry44477.course_enrollment_system.model.Enrollment;
import io.github.blueberry44477.course_enrollment_system.model.Student;
import io.github.blueberry44477.course_enrollment_system.repository.CourseRepository;
import io.github.blueberry44477.course_enrollment_system.repository.EnrollmentRepository;
import io.github.blueberry44477.course_enrollment_system.repository.StudentRepository;
import io.github.blueberry44477.course_enrollment_system.service.EnrollmentService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers 
// @WireMockTest
public class EnrollmentServiceIT {
    @Container 
    @ServiceConnection 
    static final MySQLContainer mysql = new MySQLContainer("mysql:latest");

    @Autowired
    private EnrollmentService enrollmentService;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Container
    static final WireMockContainer wiremock = new WireMockContainer("wiremock/wiremock:latest");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("policy.url", wiremock::getBaseUrl);
    }

    // Підключаємо статичний Java-клієнт WireMock до нашого контейнера
    @BeforeAll
    static void setupWireMockClient() {
        WireMock.configureFor(wiremock.getHost(), wiremock.getPort());
    }

    @BeforeEach
    void resetWireMock() {
        WireMock.reset();
    }

    @Test
    void shouldSuccessfullyEnrollStudent() {
        Student student = studentRepository.save(new Student(null, "STU-001", 20, 85, true));
        Course course = courseRepository.save(new Course(null, "CRS-101", 5000, 10, 3));

        // Підміна відповіді сервера. Якщо прийде POST з потрібним JSON, повернути 200 і allowed:true.
        stubFor(post(urlEqualTo("/enrollment-policy"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withRequestBody(matchingJsonPath("$.studentId", equalTo(student.getId().toString())))
                .withRequestBody(matchingJsonPath("$.courseId", equalTo(course.getId().toString())))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"allowed\": true}")
                        .withStatus(200)));
        
        EnrollmentRequest request = new EnrollmentRequest("REF-12345", student.getId(), course.getId(), false);
        
        // Act
        Enrollment enrollment = enrollmentService.enroll(request);
        
        assertNotNull(enrollment.getId());
        assertEquals(EnrollmentStatus.ENROLLED, enrollment.getStatus());
        assertEquals(5000, enrollment.getTotal());
        
        verify(1, postRequestedFor(urlEqualTo("/enrollment-policy")));
    }
}
