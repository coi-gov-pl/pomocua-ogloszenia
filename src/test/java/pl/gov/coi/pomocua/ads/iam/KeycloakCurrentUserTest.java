package pl.gov.coi.pomocua.ads.iam;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.IDToken;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import pl.gov.coi.pomocua.ads.UserId;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class KeycloakCurrentUserTest {

    private final KeycloakCurrentUser keycloakCurrentUser = new KeycloakCurrentUser();

    @BeforeEach
    void setUp() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void findCurrentUserId_whenContextHasNoRequest_expectOptionalEmpty() {

        //when
        Optional<UserId> currentUserId = keycloakCurrentUser.findCurrentUserId();

        //then
        assertThat(currentUserId).isEmpty();
    }

    @Test
    void findCurrentUserId_whenRequestHasNoAttribute_expectOptionalEmpty() {

        //given
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockRequest));

        //when
        Optional<UserId> currentUserId = keycloakCurrentUser.findCurrentUserId();

        //then
        assertThat(currentUserId).isEmpty();
    }

    @Test
    void findCurrentUserId_whenRequestHasTokenAttribute_expectUserIdIsPresent() {

        //given
        String subject = UUID.randomUUID().toString();

        AccessToken accessToken = new AccessToken();
        accessToken.setSubject(subject);
        KeycloakSecurityContext context = new KeycloakSecurityContext("", accessToken, "", new IDToken());

        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.setAttribute(KeycloakSecurityContext.class.getName(), context);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockRequest));

        //when
        Optional<UserId> currentUserId = keycloakCurrentUser.findCurrentUserId();

        //then
        assertThat(currentUserId).isPresent();
        assertThat(currentUserId.get().value).isEqualTo(subject);
    }

}