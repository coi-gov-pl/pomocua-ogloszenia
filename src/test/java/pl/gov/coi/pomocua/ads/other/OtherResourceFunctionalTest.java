package pl.gov.coi.pomocua.ads.other;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import pl.gov.coi.pomocua.ads.BaseResourceFunctionalTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class OtherResourceFunctionalTest extends BaseResourceFunctionalTest {

    @Autowired
    private OtherOfferRepository otherOfferRepository;

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        otherOfferRepository.deleteAll();
    }

    @Override
    protected String getUrl() {
        return "/api/other";
    }

    @Override
    protected String postUrl() {
        return "/api/secure/other";
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
                .body("location.city", equalTo("Warszawa"));
    }

    @Override
    protected void assertFirstElement(ValidatableResponse response) {
        response
                .body("content[0].title", equalTo("testTitle"))
                .body("content[0].description", equalTo("testDescription"))
                .body("content[0].location.region", equalTo("Mazowieckie"))
                .body("content[0].location.city", equalTo("Warszawa"));
    }

    @Override
    protected void assertOfferIsSavedInDb(Long id) {
        Optional<OtherOffer> otherOfferOptional = otherOfferRepository.findById(id);
        assertThat(otherOfferOptional.isPresent()).isTrue();
        OtherOffer otherOffer = otherOfferOptional.get();
        assertThat(otherOffer.title).isEqualTo("testTitle");
        assertThat(otherOffer.description).isEqualTo("testDescription");
        assertThat(otherOffer.location.region).isEqualTo("Mazowieckie");
        assertThat(otherOffer.location.city).isEqualTo("Warszawa");
    }
}
