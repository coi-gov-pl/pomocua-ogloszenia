package pl.gov.coi.pomocua.ads.iam.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import pl.gov.coi.pomocua.ads.UserId;
import pl.gov.coi.pomocua.ads.iam.KeycloakUsersRepository;
import pl.gov.coi.pomocua.ads.iam.UserNotFoundException;
import pl.gov.coi.pomocua.ads.users.User;
import pl.gov.coi.pomocua.ads.users.UsersRepository;

import javax.ws.rs.NotFoundException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class KeycloakUsersRepositoryTest {

    private UsersRepository usersRepository;

    private UsersResource keycloakUsersResource;

    @BeforeEach
    void setUp() {
        keycloakUsersResource = mock(UsersResource.class);
        usersRepository = new KeycloakUsersRepository(keycloakUsersResource);
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
        when(keycloakUsersResource.get(id)).thenReturn(userResource);
        when(userResource.toRepresentation()).thenReturn(userRepresentation);

        //when
        User user = usersRepository.getById(userId);

        //then
        assertAll(
                () -> assertThat(user).extracting(User::id, User::email, User::phoneNumber).containsExactly(userId, email, null),
                () -> verify(keycloakUsersResource, times(1)).get(id),
                () -> verify(userResource, times(1)).toRepresentation()
        );
    }

    @Test
    void getUserDetails_whenUserNotFound_expectUserNotFoundException() {

        //given
        String id = UUID.randomUUID().toString();
        UserId userId = new UserId(id);

        UserResource userResource = mock(UserResource.class);
        when(keycloakUsersResource.get(id)).thenReturn(userResource);
        when(userResource.toRepresentation()).thenThrow(new NotFoundException());

        //then
        assertAll(
                () -> assertThatThrownBy(() -> usersRepository.getById(userId))
                        .isInstanceOf(UserNotFoundException.class),
                () -> verify(keycloakUsersResource, times(1)).get(id),
                () -> verify(userResource, times(1)).toRepresentation()
        );
    }
}