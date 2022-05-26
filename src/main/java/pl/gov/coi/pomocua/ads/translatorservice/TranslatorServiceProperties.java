package pl.gov.coi.pomocua.ads.translatorservice;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "translator")
public class TranslatorServiceProperties {
    private String endpoint;
    private String key;
    private String region;
}
