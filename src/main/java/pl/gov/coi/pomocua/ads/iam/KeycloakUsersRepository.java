package pl.gov.coi.pomocua.ads.iam;

import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.util.CollectionUtils;
import pl.gov.coi.pomocua.ads.UserId;
import pl.gov.coi.pomocua.ads.users.User;
import pl.gov.coi.pomocua.ads.users.UserNotFoundException;
import pl.gov.coi.pomocua.ads.users.UsersRepository;

import javax.ws.rs.NotFoundException;
import java.util.Optional;

@RequiredArgsConstructor
public class KeycloakUsersRepository implements UsersRepository {

    private static final String OBFUSCATED = "OBFUSCATED";

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
    public User obfuscateUser(UserId userId) {
        try {
            UserResource userResource = usersResource.get(userId.value);
            userResource.logout();

            UserRepresentation userRepresentation = userResource.toRepresentation();
            User user = new User(userId, userRepresentation.getEmail(), userRepresentation.getFirstName());

            obfuscateUserRepresentation(userRepresentation);

            if (!CollectionUtils.isEmpty(userRepresentation.getFederatedIdentities())) {
                userRepresentation.getFederatedIdentities()
                        .forEach(rep -> userResource.removeFederatedIdentity(rep.getIdentityProvider()));
            }

            userResource.update(userRepresentation);

            return user;
        } catch (NotFoundException e) {
            throw new UserNotFoundException();
        }
    }

    private void obfuscateUserRepresentation(UserRepresentation userRepresentation) {
        userRepresentation.setEnabled(false);
        userRepresentation.setUsername(userRepresentation.getId());
        userRepresentation.setFirstName(OBFUSCATED);
        userRepresentation.setLastName(OBFUSCATED);

        userRepresentation.setEmail(OBFUSCATED);
        userRepresentation.setEmailVerified(false);
    }
}
