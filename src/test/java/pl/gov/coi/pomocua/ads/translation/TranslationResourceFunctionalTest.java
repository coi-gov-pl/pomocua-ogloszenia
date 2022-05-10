package pl.gov.coi.pomocua.ads.translation;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import pl.gov.coi.pomocua.ads.BaseResourceFunctionalTest;
import pl.gov.coi.pomocua.ads.Language;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TranslationResourceFunctionalTest extends BaseResourceFunctionalTest {

    @Autowired
    private TranslationOfferRepository translationOfferRepository;

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        translationOfferRepository.deleteAll();
    }

    @Override
    protected String getUrl() {
        return "/api/translation";
    }

    @Override
    protected String postUrl() {
        return "/api/secure/translation";
    }

    @Override
    protected String getBody() {
        return """
        {
          "title": "testTitle",
          "description": "testDescription",
          "location": {
            "region": "Mazowieckie",
            "city": "Warszawa"
          },
          "mode": [
            "BY_EMAIL",
            "ONLINE"
          ],
          "language": [
            "UA",
            "PL"
          ],
          "phoneNumber": "+48123456789"
        }
        """;
    }

    @Override
    protected void assertResponseBody(ValidatableResponse response) {
        response
                .body("title", equalTo("testTitle"))
                .body("description", equalTo("testDescription"))
                .body("location.region", equalTo("Mazowieckie"))
                .body("location.city", equalTo("Warszawa"))
                .body("mode", equalTo(List.of("BY_EMAIL", "ONLINE")))
                .body("language", equalTo(List.of("UA", "PL")));
    }

    @Override
    protected void assertFirstElement(ValidatableResponse response) {
        response
                .body("content[0].title", equalTo("testTitle"))
                .body("content[0].description", equalTo("testDescription"))
                .body("content[0].location.region", equalTo("Mazowieckie"))
                .body("content[0].location.city", equalTo("Warszawa"))
                .body("content[0].mode", equalTo(List.of("BY_EMAIL", "ONLINE")))
                .body("content[0].language", equalTo(List.of("UA", "PL")));
    }

    @Override
    protected void assertOfferIsSavedInDb(Long id) {
        Optional<TranslationOffer> translationOfferOptional = translationOfferRepository.findById(id);
        assertThat(translationOfferOptional.isPresent()).isTrue();
        TranslationOffer translationOffer = translationOfferOptional.get();
        assertThat(translationOffer.title).isEqualTo("testTitle");
        assertThat(translationOffer.description).isEqualTo("testDescription");
        assertThat(translationOffer.location.region).isEqualTo("Mazowieckie");
        assertThat(translationOffer.location.city).isEqualTo("Warszawa");
        assertThat(translationOffer.getMode()).isEqualTo(
                List.of(TranslationOffer.TranslationMode.BY_EMAIL, TranslationOffer.TranslationMode.ONLINE));
        assertThat(translationOffer.getLanguage()).isEqualTo(List.of(Language.UA, Language.PL));
    }
}
