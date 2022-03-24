package pl.gov.coi.pomocua.ads.captcha;

import com.google.cloud.recaptchaenterprise.v1beta1.RecaptchaEnterpriseServiceV1Beta1Client;
import com.google.cloud.recaptchaenterprise.v1beta1.RecaptchaEnterpriseServiceV1Beta1Settings;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

@Data
@ConfigurationProperties(prefix = "app.captcha")
public class CaptchaProperties {

    /**
     * Identyfikator projektu google cloud.
     * https://console.cloud.google.com/projectcreate
     */
    private String googleCloudProjectId;

    /**
     * Klucz recaptcha.
     * https://console.cloud.google.com/security/recaptcha
     */
    private String siteKey;

    /**
     * Klucz API z Google Cloud.
     * https://cloud.google.com/recaptcha-enterprise/docs/set-up-non-google-cloud-environments-api-keys
     */
    private String googleCloudApiKey;

    /**
     * Poziom akceptowalnej oceny. Po przekroczeniu tej wartości zapytania są odrzucane.
     * Możliwa skala to wartości od 0 do 1.
     * Gdzie wartość 0 - podejrzana, 1 - niepodejrzana.
     */
    private Float acceptLevel;

    /**
     * Flaga włączająca i wyłączająca walidację captcha.
     */
    private boolean enabled = true;

    @Bean
    RecaptchaEnterpriseServiceV1Beta1Client recaptchaEnterpriseServiceClient() throws IOException {
        var builder = RecaptchaEnterpriseServiceV1Beta1Settings.newBuilder();
        builder.getStubSettingsBuilder().setApiKey(googleCloudApiKey);
        var settings =  builder.build();
        return RecaptchaEnterpriseServiceV1Beta1Client.create(settings);
    }
}
