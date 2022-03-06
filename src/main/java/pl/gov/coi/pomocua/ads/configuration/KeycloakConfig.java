package pl.gov.coi.pomocua.ads.configuration;

import lombok.RequiredArgsConstructor;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import pl.gov.coi.pomocua.ads.iam.KeycloakProperties;

@Profile("!dev")
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(value = KeycloakProperties.class)
public class KeycloakConfig {

    private final KeycloakProperties properties;

    @Bean
    public Keycloak keycloak() {

        ResteasyClientBuilder resteasyClientBuilder = new ResteasyClientBuilder()
                .connectionPoolSize(properties.getConnectionPoolSize())
                .disableTrustManager();

        resteasyClientBuilder.setIsTrustSelfSignedCertificates(true);
        ResteasyClient client = resteasyClientBuilder.build();

        return KeycloakBuilder.builder()
                .serverUrl(properties.getServerUrl())
                .realm(properties.getRealm())
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(properties.getClientId())
                .clientSecret(properties.getClientSecret())
                .resteasyClient(client)
                .build();
    }

    @Bean
    public UsersResource usersResource() {
        return keycloak()
                .realm(properties.getRealm())
                .users();
    }
}
