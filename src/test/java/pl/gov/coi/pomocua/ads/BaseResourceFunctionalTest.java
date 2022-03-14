package pl.gov.coi.pomocua.ads;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import javax.transaction.Transactional;
import java.time.Instant;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestConfiguration.class)
public abstract class BaseResourceFunctionalTest {

  @Autowired
  TestTimeProvider timeProvider;

  protected abstract String getUrl();
  protected abstract String postUrl();
  protected abstract String getBody();
  protected abstract void assertResponseBody(ValidatableResponse response);
  protected abstract void assertFirstElement(ValidatableResponse response);
  protected abstract void assertOfferIsSavedInDb(Long id);

  @AfterEach
  public void cleanUp() {
    timeProvider.reset();
  }

  @Test
  @Transactional
  void shouldAddOffer() {
    //when
    ValidatableResponse response = postOffer(getBody(), postUrl());
    //then
    assertResponseBody(response);
    //and
    assertOfferIsSavedInDb(getId(response));
  }
  @Test
  @Transactional
  void shouldSaveModifiedDateOnOfferCreation() {
    timeProvider.setCurrentTime(Instant.parse("2022-03-07T15:23:22Z"));
    //when
    ValidatableResponse response = postOffer(getBody(), postUrl());
    //then
    response.body("modifiedDate", equalTo("2022-03-07T15:23:22Z")).assertThat();
  }

  @Test
  void shouldGetOfferById() {
    //given
    Long id = getId(postOffer(getBody(), postUrl()));
    //when
    ValidatableResponse response = given()
        .when()
        .get(getUrl() + "/"  + id)
        .then()
        .assertThat()
        .statusCode(200);
    //then
    assertResponseBody(response);
  }

  @Test
  void shouldReturnNotFoundWhenOfferNotExistsById() {
    postOffer(getBody(), postUrl());
    given()
        .when()
        .get(getUrl() + "/99")
        .then()
        .assertThat()
        .statusCode(404);
  }

  @Test
  void shouldGetAllOffers() {
    //given
    postOffer(getBody(), postUrl());
    postOffer(getBody(), postUrl());
    postOffer(getBody(), postUrl());
    //when
    ValidatableResponse response = given()
        .when()
        .get(getUrl())
        .then()
        .assertThat()
        .statusCode(200);
    //then
    response.body("content", hasSize(3));
    assertFirstElement(response);
  }

  @Test
  void shouldGetFirstPageOfOffers() {
    //given
    postOffer(getBody(), postUrl());
    postOffer(getBody(), postUrl());
    postOffer(getBody(), postUrl());
    postOffer(getBody(), postUrl());
    postOffer(getBody(), postUrl());
    //when
    ValidatableResponse response = given()
        .queryParam("page", 0)
        .queryParam("size", 2)
        .when()
        .get(getUrl())
        .then()
        .assertThat()
        .statusCode(200);
    //then
    response.body("content", hasSize(2));
    response.body("totalPages", equalTo(3));
    assertFirstElement(response);
  }

  @Test
  void shouldReturnEmptyListIfNoOffersFound() {
    given()
        .when()
        .get(getUrl())
        .then()
        .assertThat()
        .statusCode(200)
        .body("content", hasSize(0));
  }

  protected Long getId(ValidatableResponse response) {
    return response.extract().body().jsonPath().getLong("id");
  }

  protected ValidatableResponse postOffer(String body, String url) {
    return given()
        .body(body)
        .contentType(ContentType.JSON)
        .when()
        .post(url)
        .then()
        .assertThat()
        .statusCode(201);
  }
}
