package pl.gov.coi.pomocua.ads.iam;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.gov.coi.pomocua.ads.TestConfiguration;
import pl.gov.coi.pomocua.ads.UserId;
import pl.gov.coi.pomocua.ads.authentication.TestCurrentUser;
import pl.gov.coi.pomocua.ads.dev.FakeUserService;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestConfiguration.class)
class UserResourceTest {

    @Autowired
    private TestCurrentUser testCurrentUser;

    @Autowired
    protected TestRestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        testCurrentUser.clear();
    }

    @Test
    void getUserDetails_for() {

        //given
        String userId = UUID.randomUUID().toString();
        String email = FakeUserService.FAKE_EMAIL;

        //when
        ResponseEntity<UserDetails> response = restTemplate.getForEntity("/api/user-details/" + userId, UserDetails.class);

        //then
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody()).extracting(UserDetails::getEmail).isEqualTo(email)
        );
    }

    @Test
    void testGetUserDetails() {

        //given
        String userId = UUID.randomUUID().toString();
        String email = FakeUserService.FAKE_EMAIL;

        testCurrentUser.setCurrentUserId(new UserId(userId));

        //when
        ResponseEntity<UserDetails> response = restTemplate.getForEntity("/api/secure/user-details", UserDetails.class);

        //then
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody()).extracting(UserDetails::getEmail).isEqualTo(email)
        );
    }

    @Test
    void testGetUserDetails_noTestMock() {

        //when
        ResponseEntity<UserDetails> response = restTemplate.getForEntity("/api/secure/user-details", UserDetails.class);

        //then
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED)
        );
    }
}