package pl.gov.coi.pomocua.ads.authentication;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
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
import pl.gov.coi.pomocua.ads.UserId;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestConfiguration.class)
@AutoConfigureEmbeddedDatabase(refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_CLASS)
public class CurrentUserTest {
    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    protected TestCurrentUser testCurrentUser;

    @Test
    void shouldReturnCurrentUserIdIfSet() {
        testCurrentUser.setCurrentUserId(new UserId("some-current-id"));

        ResponseEntity<UserId> response = restTemplate.getForEntity("/api/test-authentication", UserId.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(new UserId("some-current-id"));
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
        public UserId getCurrentUserId() {
            return currentUser.getCurrentUserId();
        }
    }
}
