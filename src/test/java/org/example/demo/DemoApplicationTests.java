package org.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class DemoApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() {
        // context bootstrap check
    }

    @Test
    void actuatorMetricsEndpointShouldBeAvailable() {
        String body = restTemplate.getForObject("http://127.0.0.1:18180/actuator-metrics", String.class);
        assertThat(body).isNotBlank();
        assertThat(body).contains("names");
    }
}
