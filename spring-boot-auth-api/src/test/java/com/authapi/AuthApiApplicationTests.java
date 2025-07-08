package com.authapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Basic integration test for the Spring Boot Auth API application
 */
@SpringBootTest
@ActiveProfiles("test")
class AuthApiApplicationTests {

    @Test
    void contextLoads() {
        // This test verifies that the Spring application context loads successfully
        // and all beans are properly configured
    }
}