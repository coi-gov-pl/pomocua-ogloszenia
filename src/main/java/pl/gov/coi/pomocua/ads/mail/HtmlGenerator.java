package pl.gov.coi.pomocua.ads.mail;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class HtmlGenerator {

    private final Configuration configuration;

    public String generateHtml(String filename, Map<String, Object> variables) {
        try {
            Template template = configuration.getTemplate(filename, Locale.getDefault());
            StringWriter result = new StringWriter();
            template.process(variables, result);
            return result.toString();
        } catch (IOException | TemplateException e) {
            log.error("Error during generate html", e);
            throw new HtmlGenerationException();
        }
    }
}
