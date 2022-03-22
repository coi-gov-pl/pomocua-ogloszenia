package pl.gov.coi.pomocua.ads.materialaid;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import pl.gov.coi.pomocua.ads.BaseResourceFunctionalTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class MaterialAidResourceFunctionalTest extends BaseResourceFunctionalTest {

  @Autowired
  MaterialAidOfferRepository materialAidOfferRepository;

  @LocalServerPort
  int port;

  @BeforeEach
  public void setUp() {
    RestAssured.port = port;
    materialAidOfferRepository.deleteAll();
  }

  @Override
  protected String getUrl() {
    return "/api/material-aid";
  }

  @Override
  protected String postUrl() {
    return "api/secure/material-aid";
  }

  @Override
  protected String getBody() {
    return """
        {
          "title": "testTitle",
          "description": "testDescription",
          "category": "FOOD",
          "location": {
            "region": "Mazowieckie",
            "city": "Warszawa"
          },
          "phoneNumber": "+48123456789"
        }
        """;
  }

  @Override
  protected void assertResponseBody(ValidatableResponse response) {
    response
        .body("title", equalTo("testTitle"))
        .body("description", equalTo("testDescription"))
        .body("category", equalTo("FOOD"))
        .body("location.region", equalTo("Mazowieckie"))
        .body("location.city", equalTo("Warszawa"));
  }

  @Override
  protected void assertFirstElement(ValidatableResponse response) {
    response
        .body("content[0].title", equalTo("testTitle"))
        .body("content[0].description", equalTo("testDescription"))
        .body("content[0].category", equalTo("FOOD"))
        .body("content[0].location.region", equalTo("Mazowieckie"))
        .body("content[0].location.city", equalTo("Warszawa"));
  }

  @Override
  protected void assertOfferIsSavedInDb(Long id) {
    Optional<MaterialAidOffer> translationOfferOptional = materialAidOfferRepository.findById(id);
    assertThat(translationOfferOptional.isPresent()).isTrue();
    MaterialAidOffer materialAidOffer = translationOfferOptional.get();
    assertThat(materialAidOffer.title).isEqualTo("testTitle");
    assertThat(materialAidOffer.description).isEqualTo("testDescription");
    assertThat(materialAidOffer.category).isEqualTo(MaterialAidCategory.FOOD);
    assertThat(materialAidOffer.location.region).isEqualTo("Mazowieckie");
    assertThat(materialAidOffer.location.city).isEqualTo("Warszawa");
  }
}
