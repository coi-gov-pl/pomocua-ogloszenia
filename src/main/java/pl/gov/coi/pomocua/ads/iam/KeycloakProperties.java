package pl.gov.coi.pomocua.ads.iam;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.keycloak")
public class KeycloakProperties {

    private String serverUrl;

    private String realm;

    private String clientId;

    private String clientSecret;

    private Integer connectionPoolSize;
}
