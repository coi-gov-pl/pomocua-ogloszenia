package pl.gov.coi.pomocua.ads.jobs;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import pl.gov.coi.pomocua.ads.BaseResourceFunctionalTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class JobsResourceFunctionalTest extends BaseResourceFunctionalTest {

  @Autowired
  JobsRepository jobsRepository;

  @LocalServerPort
  int port;

  @BeforeEach
  public void setUp() {
    RestAssured.port = port;
    jobsRepository.deleteAll();
  }

  @Override
  protected String getUrl() {
    return "/api/jobs";
  }

  @Override
  protected String postUrl() {
    return "/api/secure/jobs";
  }

  @Override
  protected String getBody() {
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

  @Override
  protected void assertResponseBody(ValidatableResponse response) {
    response
        .body("title", equalTo("testTitle"))
        .body("description", equalTo("testDescription"))
        .body("mode", equalTo("REMOTE"))
        .body("location.city", equalTo("Warszawa"))
        .body("location.region", equalTo("Mazowieckie"))
        .body("type", equalTo(List.of("TEMPORARY")))
        .body("language", equalTo(List.of("UA", "PL")));
  }

  @Override
  protected void assertFirstElement(ValidatableResponse response) {
    response
        .body("content[0].title", equalTo("testTitle"))
        .body("content[0].description", equalTo("testDescription"))
        .body("content[0].mode", equalTo("REMOTE"))
        .body("content[0].location.city", equalTo("Warszawa"))
        .body("content[0].location.region", equalTo("Mazowieckie"))
        .body("content[0].type", equalTo(List.of("TEMPORARY")))
        .body("content[0].language", equalTo(List.of("UA", "PL")));
  }

  @Override
  protected void assertOfferIsSavedInDb(Long id) {
    Optional<JobOffer> jobOfferOptional = jobsRepository.findById(id);
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
}
