package io.github.blueberry44477.course_enrollment_system;

import org.springframework.boot.SpringApplication;

public class TestCourseEnrollmentSystemApplication {

	public static void main(String[] args) {
		SpringApplication.from(CourseEnrollmentSystemApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
