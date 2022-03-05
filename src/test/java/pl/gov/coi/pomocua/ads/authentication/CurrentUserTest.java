package pl.gov.coi.pomocua.ads.authentication;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.gov.coi.pomocua.ads.TestConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestConfiguration.class)
public class CurrentUserTest {
    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    protected TestCurrentUser testCurrentUser;

    @Test
    void shouldReturnCurrentUserIdIfSet() {
        testCurrentUser.setCurrentUserId("some-current-id");

        ResponseEntity<String> response = restTemplate.getForEntity("/api/test-authentication", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("some-current-id");
    }

    @Test
    void shouldReturn401WhenCurrentUserAbsent() {
        testCurrentUser.clear();

        ResponseEntity<String> response = restTemplate.getForEntity("/api/test-authentication", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @org.springframework.boot.test.context.TestConfiguration
    static class ResourceConfiguration {
        @Bean
        public TestAuthenticationResource resource(CurrentUser currentUser) {
            return new TestAuthenticationResource(currentUser);
        }
    }

    @RestController
    @RequiredArgsConstructor
    @RequestMapping("/api/test-authentication")
    static class TestAuthenticationResource {
        private final CurrentUser currentUser;

        @GetMapping
        public String getCurrentUserId() {
            return currentUser.getCurrentUserId();
        }
    }
}
