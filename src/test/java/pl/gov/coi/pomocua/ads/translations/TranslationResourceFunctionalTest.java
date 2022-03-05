package pl.gov.coi.pomocua.ads.translations;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import pl.gov.coi.pomocua.ads.BaseResourceFunctionalTest;
import pl.gov.coi.pomocua.ads.Location;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TranslationResourceFunctionalTest extends BaseResourceFunctionalTest {

  @Autowired
  TranslationOfferRepository translationOfferRepository;

  @LocalServerPort
  int port;

  @BeforeEach
  public void setUp() {
    RestAssured.port = port;
    translationOfferRepository.deleteAll();
  }

  @Override
  protected String getUrl() {
    return "/api/translations";
  }

  @Override
  protected String postUrl() {
    return "api/secure/translations";
  }

  @Override
  protected String getBody() {
    return """
        {
          "title": "testTitle",
          "description": "testDescription",
          "mode": "REMOTE",
          "language": [
            "UA",
            "PL"
          ],
          "location": {
            "region": "Mazowieckie",
            "city": "Warszawa"
          },
          "sworn": true
        }
        """;
  }

  @Override
  protected void assertResponseBody(ValidatableResponse response) {
    response
        .body("title", equalTo("testTitle"))
        .body("description", equalTo("testDescription"))
        .body("mode", equalTo("REMOTE"))
        .body("language", equalTo(List.of("UA", "PL")))
        .body("location.city", equalTo("Warszawa"))
        .body("location.region", equalTo("Mazowieckie"))
        .body("sworn", equalTo(true));
  }

  @Override
  protected void assertFirstElement(ValidatableResponse response) {
    response
        .body("content[0].title", equalTo("testTitle"))
        .body("content[0].description", equalTo("testDescription"))
        .body("content[0].mode", equalTo("REMOTE"))
        .body("content[0].language", equalTo(List.of("UA", "PL")))
        .body("content[0].location.city", equalTo("Warszawa"))
        .body("content[0].location.region", equalTo("Mazowieckie"))
        .body("content[0].sworn", equalTo(true));
  }

  @Override
  protected void assertOfferIsSavedInDb(Long id) {
    Optional<TranslationOffer> translationOfferOptional = translationOfferRepository.findById(id);
    assertThat(translationOfferOptional.isPresent()).isTrue();
    TranslationOffer translationOffer = translationOfferOptional.get();
    assertThat(translationOffer.title).isEqualTo("testTitle");
    assertThat(translationOffer.description).isEqualTo("testDescription");
    assertThat(translationOffer.mode).isEqualTo(TranslationOffer.Mode.REMOTE);
    assertThat(translationOffer.language.toString()).isEqualTo("[UA, PL]");
    assertThat(translationOffer.location).isEqualTo(new Location("Mazowieckie", "Warszawa"));
    assertThat(translationOffer.sworn).isEqualTo(true);
  }
}
