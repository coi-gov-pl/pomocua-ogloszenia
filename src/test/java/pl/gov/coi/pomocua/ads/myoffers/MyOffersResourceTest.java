package pl.gov.coi.pomocua.ads.myoffers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.gov.coi.pomocua.ads.BaseOffer;
import pl.gov.coi.pomocua.ads.PageableResponse;
import pl.gov.coi.pomocua.ads.TestConfiguration;
import pl.gov.coi.pomocua.ads.UserId;
import pl.gov.coi.pomocua.ads.accomodations.AccommodationOffer;
import pl.gov.coi.pomocua.ads.accomodations.AccommodationsTestDataGenerator;
import pl.gov.coi.pomocua.ads.authentication.TestCurrentUser;
import pl.gov.coi.pomocua.ads.jobs.JobOffer;
import pl.gov.coi.pomocua.ads.jobs.JobsTestDataGenerator;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestConfiguration.class)
class MyOffersResourceTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TestCurrentUser testCurrentUser;

    @Test
    void shouldReturnOffersForUser() {
        UserId userId = new UserId("my-offer user id");
        testCurrentUser.setCurrentUserId(userId);

        AccommodationOffer accOffer = postOffer(AccommodationsTestDataGenerator.sampleOffer(), AccommodationOffer.class);
        JobOffer jobOffer = postOffer(JobsTestDataGenerator.sampleOffer(), JobOffer.class);

        BaseOffer[] offers = listOffers();
        assertThat(offers).extracting("title").contains(accOffer.title, jobOffer.title);
    }

    private BaseOffer[] listOffers() {
        ResponseEntity<PageableResponse<AccommodationOffer>> list = restTemplate.exchange(
                "/api/secure/my-offers", HttpMethod.GET, null, new ParameterizedTypeReference<>() {}
        );
        return list.getBody().content;
    }

    private <T extends BaseOffer> T postOffer(T request, Class<T> clazz) {
        ResponseEntity<T> response = restTemplate.postForEntity("/api/secure/jobs", request, clazz);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        T entity = response.getBody();
        assertThat(entity.id).isNotNull();
        assertThat(entity).usingRecursiveComparison().ignoringFields("id").isEqualTo(request);
        return entity;
    }
}