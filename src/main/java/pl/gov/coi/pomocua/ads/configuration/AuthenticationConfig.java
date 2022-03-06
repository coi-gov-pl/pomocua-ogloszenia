package pl.gov.coi.pomocua.ads.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import pl.gov.coi.pomocua.ads.authentication.CurrentUser;
import pl.gov.coi.pomocua.ads.dev.FakeCurrentUser;
import pl.gov.coi.pomocua.ads.iam.KeycloakCurrentUser;

@Configuration
public class AuthenticationConfig {
    @Profile("dev")
    @Bean
    public CurrentUser fakeCurrentUser() {
        return new FakeCurrentUser();
    }

    @Profile("!dev")
    @Bean
    public CurrentUser keycloakCurrentUser() {
        return new KeycloakCurrentUser();
    }
}
