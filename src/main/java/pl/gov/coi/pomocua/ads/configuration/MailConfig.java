package pl.gov.coi.pomocua.ads.configuration;

import org.apache.commons.codec.CharEncoding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@Configuration
public class MailConfig {

    @Bean
    public org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer freemarkerConfigurer() {
        org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer freeMarkerConfigurer = new org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer();
        freemarker.template.Configuration configuration = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_31);
        configuration.setDefaultEncoding(CharEncoding.UTF_8);
        configuration.setClassForTemplateLoading(getClass(), "/");

        freeMarkerConfigurer.setConfiguration(configuration);
        return freeMarkerConfigurer;
    }

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasenames("messages/messages");
        source.setDefaultEncoding(CharEncoding.UTF_8);
        source.setUseCodeAsDefaultMessage(true);
        return source;
    }
}
