package io.github.blueberry44477.course_enrollment_system.config;

import io.github.blueberry44477.course_enrollment_system.service.EnrollmentPolicy;
import io.github.blueberry44477.course_enrollment_system.service.HttpEnrollmentPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.time.Duration;

@Configuration
public class PolicyConfig {

    @Bean
    public EnrollmentPolicy enrollmentPolicy(
            @Value("${policy.url}") String url,
            @Value("${policy.timeout}") Duration timeout) {
        return new HttpEnrollmentPolicy(URI.create(url), timeout);
    }
}
