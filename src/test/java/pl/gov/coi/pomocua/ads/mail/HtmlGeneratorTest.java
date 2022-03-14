package pl.gov.coi.pomocua.ads.mail;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import pl.gov.coi.pomocua.ads.TestConfiguration;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@Import(TestConfiguration.class)
@AutoConfigureEmbeddedDatabase(refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_CLASS)
class HtmlGeneratorTest {

    @Autowired
    private HtmlGenerator htmlGenerator;

    @Test
    void generateHtml_whenTemplateExistsAndHtmlInjection_thenHtmlIsEscaped() {
        //given
        Map<String, Object> variables = new HashMap<>();
        variables.put("HTML_VARIABLE", "<div>Hello World</div>");

        String expectedHtmlFragment = "&lt;div&gt;Hello World&lt;/div&gt;";

        //when
        String generatedHtml = htmlGenerator.generateHtml("templates/example.ftlh", variables);

        //then
        assertThat(generatedHtml).isNotNull().contains(expectedHtmlFragment);
    }

    @Test
    void generateHtml_whenTemplateNotFound_expectException() {
        //given
        Map<String, Object> variables = new HashMap<>();

        //then
        assertThatThrownBy(() -> htmlGenerator.generateHtml("template/not-exist.ftlh", variables))
                .isInstanceOf(HtmlGenerationException.class);
    }
}