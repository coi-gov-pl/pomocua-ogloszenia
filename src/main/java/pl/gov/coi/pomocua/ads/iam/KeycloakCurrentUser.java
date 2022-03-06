package pl.gov.coi.pomocua.ads.iam;

import org.keycloak.KeycloakSecurityContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import pl.gov.coi.pomocua.ads.UserId;
import pl.gov.coi.pomocua.ads.authentication.CurrentUser;

import java.util.Optional;

public class KeycloakCurrentUser implements CurrentUser {

    @Override
    public Optional<UserId> findCurrentUserId() {

        ServletRequestAttributes attributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        if (attributes == null) return Optional.empty();

        KeycloakSecurityContext context = (KeycloakSecurityContext) attributes.getRequest().getAttribute(KeycloakSecurityContext.class.getName());
        if (context == null) return Optional.empty();

        return Optional.of(new UserId(context.getToken().getSubject()));
    }
}
