package pl.gov.coi.pomocua.ads.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import pl.gov.coi.pomocua.ads.locale.LanguageHeaderLocaleResolver;

@Configuration
public class WebConfig {

    @Value("${app.locale.header}")
    private String localeHeader;

    @Bean
    public LocaleResolver localeResolver(WebProperties webProperties) {
        LanguageHeaderLocaleResolver languageHeaderLocaleResolver = new LanguageHeaderLocaleResolver(localeHeader);
        languageHeaderLocaleResolver.setDefaultLocale(webProperties.getLocale());
        return languageHeaderLocaleResolver;
    }
}
