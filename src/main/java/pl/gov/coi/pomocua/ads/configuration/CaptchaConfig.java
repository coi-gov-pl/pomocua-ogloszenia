package pl.gov.coi.pomocua.ads.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import pl.gov.coi.pomocua.ads.captcha.CaptchaProperties;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(value = CaptchaProperties.class)
public class CaptchaConfig {

    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(3 * 1000);
        factory.setReadTimeout(7 * 1000);
        return factory;
    }

    @Bean
    public RestOperations restTemplate() {
        return new RestTemplate(this.clientHttpRequestFactory());
    }
}
