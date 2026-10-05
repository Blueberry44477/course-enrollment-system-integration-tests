package io.github.blueberry44477.course_enrollment_system.service;

import io.github.blueberry44477.course_enrollment_system.model.Course;
import io.github.blueberry44477.course_enrollment_system.model.Student;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class HttpEnrollmentPolicy implements EnrollmentPolicy {
    private final URI url;
    private final Duration timeout;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public HttpEnrollmentPolicy(URI url, Duration timeout) {
        this.url = url;
        this.timeout = timeout;
        this.httpClient = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NEVER)
                .connectTimeout(timeout)
                .build();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public boolean isAllowed(Student student, Course course) {
        try {
            String jsonBody = String.format("{\"studentId\":%d,\"courseId\":%d}", student.getId(), course.getId());
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url.resolve("/enrollment-policy"))
                    .timeout(timeout)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new IllegalStateException("Unexpected status code: " + response.statusCode());
            }

            JsonNode root = objectMapper.readTree(response.body());
            if (!root.has("allowed") || !root.get("allowed").isBoolean()) {
                throw new IllegalStateException("Invalid JSON response: missing or non-boolean 'allowed' field");
            }

            boolean allowed = root.get("allowed").asBoolean();
            return allowed;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Request was interrupted", e);
        } catch (Exception e) {
            throw new IllegalStateException("Error during policy check", e);
        }
    }
}
