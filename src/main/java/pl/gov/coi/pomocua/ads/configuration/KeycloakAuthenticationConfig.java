package pl.gov.coi.pomocua.ads.configuration;

import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import pl.gov.coi.pomocua.ads.authentication.CurrentUser;
import pl.gov.coi.pomocua.ads.iam.KeycloakCurrentUser;
import pl.gov.coi.pomocua.ads.iam.KeycloakUsersRepository;
import pl.gov.coi.pomocua.ads.users.UsersRepository;

@Profile("!dev")
@Configuration
public class KeycloakAuthenticationConfig {

    @Bean
    public CurrentUser keycloakCurrentUser() {
        return new KeycloakCurrentUser();
    }

    @Bean
    public UsersRepository keycloakUsersRepository(UsersResource keycloakUsersResource) {
        return new KeycloakUsersRepository(keycloakUsersResource);
    }
}
