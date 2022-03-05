package pl.gov.coi.pomocua.ads.accomodations;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.junit.Ignore;
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

  private static final String POST_URL = "/api/secure/accommodations";
  private static final String GET_URL = "/api/accommodations";

  @BeforeEach
  public void setUp() {
    RestAssured.port = port;
    accommodationsRepository.deleteAll();
  }

  @Test
  void shouldAddAccommodation() {
    //when
    ValidatableResponse response = postAccommodation(getBody());
    //then
    assertResponseBody(response);
    //and
    Optional<AccommodationOffer> accommodationOfferOptional = accommodationsRepository.findById(getId(response));
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

  @Test
  void shouldGetAccommodationOfferById() {
    //given
    Long id = getId(postAccommodation(getBody()));
    //when
    ValidatableResponse response = given()
        .when()
        .get(GET_URL + "/" + id)
        .then()
        .assertThat()
        .statusCode(200);
    //then
    assertResponseBody(response);
  }

  @Ignore
  void shouldReturnNotFoundWhenAccommodationOfferNotExistsById() {
    postAccommodation(getBody());
    given()
        .when()
        .get(GET_URL + "/99")
        .then()
        .assertThat()
        .statusCode(404);
  }

  @Test
  void shouldGetAllAccommodationsOffers() {
    //given
    postAccommodation(getBody());
    postAccommodation(getBody());
    postAccommodation(getBody());
    //when
    ValidatableResponse response = given()
        .when()
        .get(GET_URL)
        .then()
        .assertThat()
        .statusCode(200);
    //then
    response.body("content", hasSize(3));
    assertFirstElement(response);
  }

  @Test
  void shouldGetFirstPageOfAccommodationsOffers() {
    //given
    postAccommodation(getBody());
    postAccommodation(getBody());
    postAccommodation(getBody());
    postAccommodation(getBody());
    postAccommodation(getBody());
    //when
    ValidatableResponse response = given()
        .queryParam("page", 0)
        .queryParam("size", 2)
        .when()
        .get(GET_URL)
        .then()
        .assertThat()
        .statusCode(200);
    //then
    response.body("content", hasSize(2));
    response.body("totalPages", equalTo(3));
    assertFirstElement(response);
  }

  @Test
  void shouldReturnEmptyListIfNoAccommodationOffersFound() {
    given()
        .when()
        .get(GET_URL)
        .then()
        .assertThat()
        .statusCode(200)
        .body("content", hasSize(0));
  }

  @Test
  void shouldFindAccommodationsByRegionAndCity() {
    //given
    postAccommodation(getBody());
    postAccommodation(getBody());
    postAccommodation(getBody());
    postAccommodation(getBody());
    postAccommodation(getBody());
    //when
    ValidatableResponse response = given()
        .when()
        .get(GET_URL + "/Mazowieckie/Warszawa")
        .then()
        .assertThat()
        .statusCode(200);
    //then
    response.body("content", hasSize(5));
    assertFirstElement(response);
  }

  @Test
  void shouldReturnEmptyListWhenNoAccommodationsFoundByRegionAndCity() {
    postAccommodation(getBody());
    given()
        .when()
        .get(GET_URL + "/Małopolskie/Kraków")
        .then()
        .assertThat()
        .statusCode(200)
        .body("content", hasSize(0));
  }

  private void assertResponseBody(ValidatableResponse response) {
    response
        .body("title", equalTo("testTitle"))
        .body("description", equalTo("testDescription"))
        .body("location.region", equalTo("Mazowieckie"))
        .body("location.city", equalTo("Warszawa"))
        .body("guests", equalTo(3))
        .body("lengthOfStay", equalTo("WEEK_1"))
        .body("hostLanguage", equalTo(List.of("UA")));
  }

  private void assertFirstElement(ValidatableResponse response) {
    response
        .body("content[0].title", equalTo("testTitle"))
        .body("content[0].description", equalTo("testDescription"))
        .body("content[0].location.region", equalTo("Mazowieckie"))
        .body("content[0].location.city", equalTo("Warszawa"))
        .body("content[0].guests", equalTo(3))
        .body("content[0].lengthOfStay", equalTo("WEEK_1"))
        .body("content[0].hostLanguage", equalTo(List.of("UA")));
  }

  private ValidatableResponse postAccommodation(String body) {
    return given()
        .body(body)
        .contentType(ContentType.JSON)
        .when()
        .post(POST_URL)
        .then()
        .assertThat()
        .statusCode(201);
  }

  private String getBody() {
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
      ]
    }
    """;
  }

  private Long getId(ValidatableResponse response) {
    return response.extract().body().jsonPath().getLong("id");
  }

}
