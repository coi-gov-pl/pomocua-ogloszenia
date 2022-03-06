package pl.gov.coi.pomocua.ads.iam.service;

import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pl.gov.coi.pomocua.ads.UserId;
import pl.gov.coi.pomocua.ads.iam.UserDetails;
import pl.gov.coi.pomocua.ads.iam.UserNotFoundException;

import javax.ws.rs.NotFoundException;

@Profile("!dev")
@Service
@RequiredArgsConstructor
public class KeycloakUserService implements UserService {

    private final UsersResource usersResource;

    public UserDetails getUserDetails(UserId userId) {
        try {
            UserRepresentation userRepresentation = usersResource.get(userId.value).toRepresentation();
            return new UserDetails(userRepresentation.getEmail());
        } catch (NotFoundException e) {
            throw new UserNotFoundException();
        }
    }
}
