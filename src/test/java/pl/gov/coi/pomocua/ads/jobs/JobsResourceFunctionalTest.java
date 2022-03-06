package pl.gov.coi.pomocua.ads.jobs;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import pl.gov.coi.pomocua.ads.BaseResourceFunctionalTest;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

public class JobsResourceFunctionalTest extends BaseResourceFunctionalTest {

  @Autowired
  JobsRepository jobsRepository;

  @LocalServerPort
  int port;

  private static final String POST_URL = "/api/secure/jobs";
  private static final String GET_URL = "/api/jobs";

  @BeforeEach
  public void setUp() {
    RestAssured.port = port;
    jobsRepository.deleteAll();
  }

  @Test
  @Transactional
  void shouldAddJobOffer() {
    //when
    ValidatableResponse response = postJobOffer(getBody());
    //then
    assertResponseBody(response);
    //and
    Optional<JobOffer> jobOfferOptional = jobsRepository.findById(getId(response));
    assertThat(jobOfferOptional.isPresent()).isTrue();
    JobOffer jobOffer = jobOfferOptional.get();
    assertThat(jobOffer.title).isEqualTo("testTitle");
    assertThat(jobOffer.description).isEqualTo("testDescription");
    assertThat(jobOffer.mode).isEqualTo(JobOffer.Mode.REMOTE);
    assertThat(jobOffer.location.city).isEqualTo("Warszawa");
    assertThat(jobOffer.location.region).isEqualTo("Mazowieckie");
    assertThat(jobOffer.type.toString()).isEqualTo("[TEMPORARY]");
    assertThat(jobOffer.language.toString()).isEqualTo("[UA, PL]");
  }

  @Test
  void shouldGetJobOfferById() {
    //given
    Long id = getId(postJobOffer(getBody()));
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

  @Test
  void shouldReturnNotFoundWhenJobOfferNotExistsById() {
    postJobOffer(getBody());
    given()
        .when()
        .get(GET_URL + "/99")
        .then()
        .assertThat()
        .statusCode(404);
  }

  @Test
  void shouldGetAllJobOffers() {
    //given
    postJobOffer(getBody());
    postJobOffer(getBody());
    postJobOffer(getBody());
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
  void shouldGetFirstPageOfJobOffers() {
    //given
    postJobOffer(getBody());
    postJobOffer(getBody());
    postJobOffer(getBody());
    postJobOffer(getBody());
    postJobOffer(getBody());
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
  void shouldReturnEmptyListIfNoJobOffersFound() {
    given()
        .when()
        .get(GET_URL)
        .then()
        .assertThat()
        .statusCode(200)
        .body("content", hasSize(0));
  }

  private void assertResponseBody(ValidatableResponse response) {
    response
        .body("title", equalTo("testTitle"))
        .body("description", equalTo("testDescription"))
        .body("mode", equalTo("REMOTE"))
        .body("location.city", equalTo("Warszawa"))
        .body("location.region", equalTo("Mazowieckie"))
        .body("type", equalTo(List.of("TEMPORARY")))
        .body("language", equalTo(List.of("UA", "PL")));
  }

  private void assertFirstElement(ValidatableResponse response) {
    response
        .body("content[0].title", equalTo("testTitle"))
        .body("content[0].description", equalTo("testDescription"))
        .body("content[0].mode", equalTo("REMOTE"))
        .body("content[0].location.city", equalTo("Warszawa"))
        .body("content[0].location.region", equalTo("Mazowieckie"))
        .body("content[0].type", equalTo(List.of("TEMPORARY")))
        .body("content[0].language", equalTo(List.of("UA", "PL")));
  }

  private ValidatableResponse postJobOffer(String body) {
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
      "userId": {
        "value": "testUser1"
      },
      "title": "testTitle",
      "description": "testDescription",
      "mode": "REMOTE",
      "location": {
        "region": "Mazowieckie",
        "city": "Warszawa"
      },
      "type": [
        "TEMPORARY"
      ],
      "language": [
        "UA",
        "PL"
      ]
    }
    """;
  }

  private Long getId(ValidatableResponse response) {
    return response.extract().body().jsonPath().getLong("id");
  }
}
