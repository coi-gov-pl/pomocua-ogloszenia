package pl.gov.coi.pomocua.ads.iam.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import pl.gov.coi.pomocua.ads.UserId;
import pl.gov.coi.pomocua.ads.iam.UserDetails;
import pl.gov.coi.pomocua.ads.iam.UserNotFoundException;

import javax.ws.rs.NotFoundException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class KeycloakUserServiceTest {

    private UserService userService;

    private UsersResource usersResource;

    @BeforeEach
    void setUp() {
        usersResource = mock(UsersResource.class);
        userService = new KeycloakUserService(usersResource);
    }

    @Test
    void getUserDetails_whenUserExists_expectUserDetails() {

        //given
        String id = UUID.randomUUID().toString();
        String email = "mock@email.com";
        UserId userId = new UserId(id);

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setId(id);
        userRepresentation.setEmail(email);

        UserResource userResource = mock(UserResource.class);
        when(usersResource.get(id)).thenReturn(userResource);
        when(userResource.toRepresentation()).thenReturn(userRepresentation);

        //when
        UserDetails userDetails = userService.getUserDetails(userId);

        //then
        assertAll(
                () -> assertThat(userDetails).extracting(UserDetails::getEmail).isEqualTo(email),
                () -> verify(usersResource, times(1)).get(id),
                () -> verify(userResource, times(1)).toRepresentation()
        );
    }

    @Test
    void getUserDetails_whenUserNotFound_expectUserNotFoundException() {

        //given
        String id = UUID.randomUUID().toString();
        UserId userId = new UserId(id);

        UserResource userResource = mock(UserResource.class);
        when(usersResource.get(id)).thenReturn(userResource);
        when(userResource.toRepresentation()).thenThrow(new NotFoundException());

        //then
        assertAll(
                () -> assertThatThrownBy(() -> userService.getUserDetails(userId))
                        .isInstanceOf(UserNotFoundException.class),
                () -> verify(usersResource, times(1)).get(id),
                () -> verify(userResource, times(1)).toRepresentation()
        );
    }
}