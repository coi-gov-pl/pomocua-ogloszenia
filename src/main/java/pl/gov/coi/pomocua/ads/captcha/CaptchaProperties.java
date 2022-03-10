package pl.gov.coi.pomocua.ads.captcha;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.captcha")
public class CaptchaProperties {

    private String site;

    private String secret;

    private boolean enabled;
}
