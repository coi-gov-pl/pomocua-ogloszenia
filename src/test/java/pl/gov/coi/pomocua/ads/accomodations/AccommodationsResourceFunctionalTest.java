package pl.gov.coi.pomocua.ads.accomodations;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import pl.gov.coi.pomocua.ads.BaseResourceFunctionalTest;

import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

public class AccommodationsResourceFunctionalTest extends BaseResourceFunctionalTest {

  @Autowired
  private AccommodationsRepository accommodationsRepository;

  @LocalServerPort
  private int port;

  @BeforeEach
  public void setUp() {
    RestAssured.port = port;
    accommodationsRepository.deleteAll();
  }

  @Test
  void shouldFindAccommodationsByRegionAndCity() {
    //given
    postOffer(getBody(), postUrl());
    postOffer(getBody(), postUrl());
    postOffer(getBody(), postUrl());
    postOffer(getBody(), postUrl());
    postOffer(getBody(), postUrl());
    //when
    ValidatableResponse response = given()
        .when()
        .get(getUrl() + "/Mazowieckie/Warszawa")
        .then()
        .assertThat()
        .statusCode(200);
    //then
    response.body("content", hasSize(5));
    assertFirstElement(response);
  }

  @Test
  void shouldReturnEmptyListWhenNoAccommodationsFoundByRegionAndCity() {
    postOffer(getBody(), postUrl());
    given()
        .when()
        .get(getUrl() + "/Małopolskie/Kraków")
        .then()
        .assertThat()
        .statusCode(200)
        .body("content", hasSize(0));
  }

  @Override
  protected String getUrl() {
    return "/api/accommodations";
  }

  @Override
  protected String postUrl() {
    return "/api/secure/accommodations";
  }

  protected String getBody() {
    return """
        {
          "title": "testTitle",
          "description": "testDescription",
          "location": {
            "region": "Mazowieckie",
            "city": "Warszawa"
          },
          "guests": 3,
          "lengthOfStay": "WEEK_1",
          "hostLanguage": [
            "UA"
          ],
          "phoneNumber": "481234567890"
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
        .body("guests", equalTo(3))
        .body("lengthOfStay", equalTo("WEEK_1"))
        .body("hostLanguage", equalTo(List.of("UA")));
  }

  @Override
  protected void assertFirstElement(ValidatableResponse response) {
    response
        .body("content[0].title", equalTo("testTitle"))
        .body("content[0].description", equalTo("testDescription"))
        .body("content[0].location.region", equalTo("Mazowieckie"))
        .body("content[0].location.city", equalTo("Warszawa"))
        .body("content[0].guests", equalTo(3))
        .body("content[0].lengthOfStay", equalTo("WEEK_1"))
        .body("content[0].hostLanguage", equalTo(List.of("UA")));
  }

  @Override
  protected void assertOfferIsSavedInDb(Long id) {
    Optional<AccommodationOffer> accommodationOfferOptional = accommodationsRepository.findById(id);
    assertThat(accommodationOfferOptional.isPresent()).isTrue();
    AccommodationOffer accommodationOffer = accommodationOfferOptional.get();
    assertThat(accommodationOffer.title).isEqualTo("testTitle");
    assertThat(accommodationOffer.description).isEqualTo("testDescription");
    assertThat(accommodationOffer.location.region).isEqualTo("Mazowieckie");
    assertThat(accommodationOffer.location.city).isEqualTo("Warszawa");
    assertThat(accommodationOffer.guests).isEqualTo(3);
    assertThat(accommodationOffer.lengthOfStay.toString()).isEqualTo("WEEK_1");
    assertThat(accommodationOffer.hostLanguage.toString()).isEqualTo("[UA]");
  }
}
