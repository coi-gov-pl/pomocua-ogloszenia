package pl.gov.coi.pomocua.ads.transport;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransportOfferResourceFunctionalTest {

  @Autowired
  TransportOfferRepository transportOfferRepository;

  @LocalServerPort
  int port;

  private static final String POST_URL = "/api/secure/transport";
  private static final String GET_URL = "/api/transport";

  @BeforeEach
  public void setUp() {
    RestAssured.port = port;
    transportOfferRepository.deleteAll();
  }

  @Test
  void shouldAddTransferOffer() {
    //when
    ValidatableResponse response = postTransferOffer(getBody());
    //then
    assertResponseBody(response);
    //and
    Optional<TransportOffer> transportOfferOptional = transportOfferRepository.findById(getId(response));
    assertThat(transportOfferOptional.isPresent()).isTrue();
    TransportOffer transportOffer = transportOfferOptional.get();
    assertThat(transportOffer.title).isEqualTo("testTitle");
    assertThat(transportOffer.description).isEqualTo("testDescription");
    assertThat(transportOffer.origin.region).isEqualTo("Małopolska");
    assertThat(transportOffer.origin.city).isEqualTo("Kraków");
    assertThat(transportOffer.destination.region).isEqualTo("Mazowieckie");
    assertThat(transportOffer.destination.city).isEqualTo("Warszawa");
    assertThat(transportOffer.capacity).isEqualTo(3);
    assertThat(transportOffer.transportDate).isEqualTo("2022-03-05");
  }

  @Test
  void shouldGetTransferOfferById() {
    //given
    Long id = getId(postTransferOffer(getBody()));
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
  void shouldReturnNotFoundWhenTransferOfferNotExistsById() {
    postTransferOffer(getBody());
    given()
        .when()
        .get(GET_URL + "/99")
        .then()
        .assertThat()
        .statusCode(404);
  }

  @Test
  void shouldGetAllTransferOffers() {
    //given
    postTransferOffer(getBody());
    postTransferOffer(getBody());
    postTransferOffer(getBody());
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
  void shouldGetFirstPageOfTransferOffers() {
    //given
    postTransferOffer(getBody());
    postTransferOffer(getBody());
    postTransferOffer(getBody());
    postTransferOffer(getBody());
    postTransferOffer(getBody());
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
  void shouldReturnEmptyListIfNoTransferOffersFound() {
    given()
        .when()
        .get(GET_URL)
        .then()
        .assertThat()
        .statusCode(200)
        .body("content", hasSize(0));
  }

  private ValidatableResponse postTransferOffer(String body) {
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
        "origin": {
          "region": "Małopolska",
          "city": "Kraków"
        },
        "destination": {
          "region": "Mazowieckie",
          "city": "Warszawa"
        },
        "capacity": 3,
        "transportDate": "2022-03-05"
      }
    """;
  }

  private void assertResponseBody(ValidatableResponse response) {
    response
        .body("title", equalTo("testTitle"))
        .body("description", equalTo("testDescription"))
        .body("origin.region", equalTo("Małopolska"))
        .body("origin.city", equalTo("Kraków"))
        .body("destination.region", equalTo("Mazowieckie"))
        .body("destination.city", equalTo("Warszawa"))
        .body("capacity", equalTo(3))
        .body("transportDate", equalTo("2022-03-05"));
  }

  private void assertFirstElement(ValidatableResponse response) {
    response
        .body("content[0].title", equalTo("testTitle"))
        .body("content[0].description", equalTo("testDescription"))
        .body("content[0].origin.region", equalTo("Małopolska"))
        .body("content[0].origin.city", equalTo("Kraków"))
        .body("content[0].destination.region", equalTo("Mazowieckie"))
        .body("content[0].destination.city", equalTo("Warszawa"))
        .body("content[0].capacity", equalTo(3))
        .body("content[0].transportDate", equalTo("2022-03-05"));
  }

  private Long getId(ValidatableResponse response) {
    return response.extract().body().jsonPath().getLong("id");
  }
}
