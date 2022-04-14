package pl.gov.coi.pomocua.ads.law;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import pl.gov.coi.pomocua.ads.BaseResourceFunctionalTest;
import pl.gov.coi.pomocua.ads.Language;
import pl.gov.coi.pomocua.ads.law.LawOffer.HelpKind;
import pl.gov.coi.pomocua.ads.law.LawOffer.HelpMode;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class LawResourceFunctionalTest extends BaseResourceFunctionalTest {

    @Autowired
    private LawOfferRepository lawOfferRepository;

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        lawOfferRepository.deleteAll();
    }

    @Override
    protected String getUrl() {
        return "/api/law";
    }

    @Override
    protected String postUrl() {
        return "/api/secure/law";
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
                  "helpKind": [
                    "IMMIGRATION_LAW",
                    "TAX_LAW"
                  ],
                  "helpMode": [
                    "ONLINE",
                    "BY_PHONE"
                  ],
                  "language": [
                    "PL",
                    "EN"
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
                .body("helpKind", equalTo(List.of("IMMIGRATION_LAW", "TAX_LAW")))
                .body("helpMode", equalTo(List.of("ONLINE", "BY_PHONE")))
                .body("language", equalTo(List.of("PL", "EN")));
    }

    @Override
    protected void assertFirstElement(ValidatableResponse response) {
        response
                .body("content[0].title", equalTo("testTitle"))
                .body("content[0].description", equalTo("testDescription"))
                .body("content[0].location.region", equalTo("Mazowieckie"))
                .body("content[0].location.city", equalTo("Warszawa"))
                .body("content[0].helpKind", equalTo(List.of("IMMIGRATION_LAW", "TAX_LAW")))
                .body("content[0].helpMode", equalTo(List.of("ONLINE", "BY_PHONE")))
                .body("content[0].language", equalTo(List.of("PL", "EN")));
    }

    @Override
    protected void assertOfferIsSavedInDb(Long id) {
        Optional<LawOffer> lawOfferOptional = lawOfferRepository.findById(id);
        assertThat(lawOfferOptional.isPresent()).isTrue();
        LawOffer lawOffer = lawOfferOptional.get();
        assertThat(lawOffer.title).isEqualTo("testTitle");
        assertThat(lawOffer.description).isEqualTo("testDescription");
        assertThat(lawOffer.location.region).isEqualTo("Mazowieckie");
        assertThat(lawOffer.location.city).isEqualTo("Warszawa");
        assertThat(lawOffer.getHelpKind()).isEqualTo(List.of(HelpKind.IMMIGRATION_LAW, HelpKind.TAX_LAW));
        assertThat(lawOffer.getHelpMode()).isEqualTo(List.of(HelpMode.ONLINE, HelpMode.BY_PHONE));
        assertThat(lawOffer.getLanguage()).isEqualTo(List.of(Language.PL, Language.EN));
    }
}
