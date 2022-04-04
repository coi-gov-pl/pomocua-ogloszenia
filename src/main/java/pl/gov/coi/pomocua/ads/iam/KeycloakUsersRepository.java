package pl.gov.coi.pomocua.ads.iam;

import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import pl.gov.coi.pomocua.ads.UserId;
import pl.gov.coi.pomocua.ads.users.User;
import pl.gov.coi.pomocua.ads.users.UserNotFoundException;
import pl.gov.coi.pomocua.ads.users.UsersRepository;

import javax.ws.rs.NotFoundException;
import java.util.Optional;

@RequiredArgsConstructor
public class KeycloakUsersRepository implements UsersRepository {

    private final UsersResource usersResource;

    @Override
    public Optional<User> getById(UserId userId) {
        try {
            UserRepresentation userRepresentation = usersResource.get(userId.value).toRepresentation();
            return Optional.of(new User(userId, userRepresentation.getEmail(), userRepresentation.getFirstName()));
        } catch (NotFoundException e) {
            return Optional.empty();
        }
    }

    @Override
    public void removeUser(UserId userId) {
        try {
            UserResource userResource = usersResource.get(userId.value);
            userResource.logout();
            userResource.remove();
        } catch (NotFoundException e) {
            throw new UserNotFoundException();
        }
    }
}
