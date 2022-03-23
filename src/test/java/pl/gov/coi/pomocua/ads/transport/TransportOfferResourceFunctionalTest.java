package pl.gov.coi.pomocua.ads.transport;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import pl.gov.coi.pomocua.ads.BaseResourceFunctionalTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static pl.gov.coi.pomocua.ads.transport.TransportTestDataGenerator.TRANSPORT_DATE;

public class TransportOfferResourceFunctionalTest extends BaseResourceFunctionalTest {

  private static final String TRANSPORT_DATE_TEXT = TRANSPORT_DATE.toString();

  @Autowired
  TransportOfferRepository transportOfferRepository;

  @LocalServerPort
  int port;

  @BeforeEach
  public void setUp() {
    RestAssured.port = port;
    transportOfferRepository.deleteAll();
  }

  @Override
  protected String getUrl() {
    return "/api/transport";
  }

  @Override
  protected String postUrl() {
    return "/api/secure/transport";
  }

  @Override
  protected String getBody() {
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
            "transportDate": "2022-03-05",
            "phoneNumber": "+48123456789"
          }
        """;
  }

  @Override
  protected void assertResponseBody(ValidatableResponse response) {
    response
            .body("title", equalTo("testTitle"))
            .body("description", equalTo("testDescription"))
            .body("origin.region", equalTo("Małopolska"))
            .body("origin.city", equalTo("Kraków"))
            .body("destination.region", equalTo("Mazowieckie"))
            .body("destination.city", equalTo("Warszawa"))
            .body("capacity", equalTo(3))
            .body("transportDate", equalTo(TRANSPORT_DATE_TEXT));
  }

  @Override
  protected void assertFirstElement(ValidatableResponse response) {
    response
        .body("content[0].title", equalTo("testTitle"))
        .body("content[0].description", equalTo("testDescription"))
        .body("content[0].origin.region", equalTo("Małopolska"))
        .body("content[0].origin.city", equalTo("Kraków"))
        .body("content[0].destination.region", equalTo("Mazowieckie"))
        .body("content[0].destination.city", equalTo("Warszawa"))
        .body("content[0].capacity", equalTo(3))
        .body("content[0].transportDate", equalTo(TRANSPORT_DATE_TEXT));

  }

  @Override
  protected void assertOfferIsSavedInDb(Long id) {
    Optional<TransportOffer> transportOfferOptional = transportOfferRepository.findById(id);
    assertThat(transportOfferOptional.isPresent()).isTrue();
    TransportOffer transportOffer = transportOfferOptional.get();
    assertThat(transportOffer.title).isEqualTo("testTitle");
    assertThat(transportOffer.description).isEqualTo("testDescription");
    assertThat(transportOffer.origin.region).isEqualTo("Małopolska");
    assertThat(transportOffer.origin.city).isEqualTo("Kraków");
    assertThat(transportOffer.destination.region).isEqualTo("Mazowieckie");
    assertThat(transportOffer.destination.city).isEqualTo("Warszawa");
    assertThat(transportOffer.capacity).isEqualTo(3);
    assertThat(transportOffer.transportDate).isEqualTo(TRANSPORT_DATE_TEXT);
  }
}
