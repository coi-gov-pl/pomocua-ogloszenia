package pl.gov.coi.pomocua.ads.configuration;

import org.apache.commons.codec.CharEncoding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FreeMarkerConfigurer {

    @Bean
    public org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer freemarkerConfigurer() {
        org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer freeMarkerConfigurer = new org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer();
        freemarker.template.Configuration configuration = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_31);
        configuration.setDefaultEncoding(CharEncoding.UTF_8);
        configuration.setClassForTemplateLoading(getClass(), "/");

        freeMarkerConfigurer.setConfiguration(configuration);
        return freeMarkerConfigurer;
    }
}
