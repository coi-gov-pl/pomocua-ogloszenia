package pl.gov.coi.pomocua.ads.health;

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

import static pl.gov.coi.pomocua.ads.health.HealthOffer.HealthCareMode;

public class HealthResourceFunctionalTest extends BaseResourceFunctionalTest {

    @Autowired
    private HealthOfferRepository healthOfferRepository;

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        healthOfferRepository.deleteAll();
    }

    @Override
    protected String getUrl() {
        return "/api/health-care";
    }

    @Override
    protected String postUrl() {
        return "/api/secure/health-care";
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
            "IN_FACILITY",
            "ONLINE"
          ],
          "specialization": "GENERAL",
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
                .body("mode", equalTo(List.of("IN_FACILITY", "ONLINE")))
                .body("specialization", equalTo("GENERAL"))
                .body("language", equalTo(List.of("UA", "PL")));
    }

    @Override
    protected void assertFirstElement(ValidatableResponse response) {
        response
                .body("content[0].title", equalTo("testTitle"))
                .body("content[0].description", equalTo("testDescription"))
                .body("content[0].location.region", equalTo("Mazowieckie"))
                .body("content[0].location.city", equalTo("Warszawa"))
                .body("content[0].mode", equalTo(List.of("IN_FACILITY", "ONLINE")))
                .body("content[0].specialization", equalTo("GENERAL"))
                .body("content[0].language", equalTo(List.of("UA", "PL")));
    }

    @Override
    protected void assertOfferIsSavedInDb(Long id) {
        Optional<HealthOffer> healthOfferOptional = healthOfferRepository.findById(id);
        assertThat(healthOfferOptional.isPresent()).isTrue();
        HealthOffer healthOffer = healthOfferOptional.get();
        assertThat(healthOffer.title).isEqualTo("testTitle");
        assertThat(healthOffer.description).isEqualTo("testDescription");
        assertThat(healthOffer.location.region).isEqualTo("Mazowieckie");
        assertThat(healthOffer.location.city).isEqualTo("Warszawa");
        assertThat(healthOffer.getMode()).isEqualTo(List.of(HealthCareMode.IN_FACILITY, HealthCareMode.ONLINE));
        assertThat(healthOffer.specialization).isEqualTo(HealthCareSpecialization.GENERAL);
        assertThat(healthOffer.getLanguage()).isEqualTo(List.of(Language.UA, Language.PL));
    }
}
