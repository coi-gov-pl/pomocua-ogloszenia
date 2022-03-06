package pl.gov.coi.pomocua.ads.myoffers;

import org.junit.jupiter.api.AfterEach;
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

    @AfterEach
    void tearDown() {
        testCurrentUser.setDefault();
    }

    @Test
    void shouldReturnDifferentOffersForUser() {
        UserId userId = new UserId("my-offer user id");
        testCurrentUser.setCurrentUserId(userId);

        AccommodationOffer accOffer = postOffer(AccommodationsTestDataGenerator.sampleOffer(), "accommodations", AccommodationOffer.class);
        JobOffer jobOffer = postOffer(JobsTestDataGenerator.sampleOffer(), "jobs", JobOffer.class);

        BaseOffer[] offers = listOffers();
        assertThat(offers).extracting("id").containsExactly(accOffer.id, jobOffer.id);
        assertThat(offers).extracting("title").containsExactly(accOffer.title, jobOffer.title);
        assertThat(offers).extracting("description").containsExactly(accOffer.description, jobOffer.description);
    }

    @Test
    void shouldReturnOffersOnlyForLoggedUser() {
        UserId accommodationOfferUserId = new UserId("acommodation offer user id");
        testCurrentUser.setCurrentUserId(accommodationOfferUserId);
        AccommodationOffer accOffer = postOffer(AccommodationsTestDataGenerator.sampleOffer(), "accommodations", AccommodationOffer.class);

        UserId jobOfferUserId = new UserId("job offer user id");
        testCurrentUser.setCurrentUserId(jobOfferUserId);
        JobOffer jobOffer = postOffer(JobsTestDataGenerator.sampleOffer(), "jobs", JobOffer.class);

        testCurrentUser.setCurrentUserId(accommodationOfferUserId);
        BaseOffer[] offers = listOffers();
        assertThat(offers).extracting("id").containsExactly(accOffer.id);
        assertThat(offers).extracting("title").containsExactly(accOffer.title);
        assertThat(offers).extracting("description").containsExactly(accOffer.description);
    }

    @Test
    void shouldReturnNotFoundWhenGettingOfferForOtherUser() {
        UserId accommodationOfferUserId = new UserId("acommodation offer user id");
        testCurrentUser.setCurrentUserId(accommodationOfferUserId);
        AccommodationOffer accOffer = postOffer(AccommodationsTestDataGenerator.sampleOffer(), "accommodations", AccommodationOffer.class);

        UserId differentUserId = new UserId("different user id");
        testCurrentUser.setCurrentUserId(differentUserId);

        ResponseEntity<BaseOffer> response = restTemplate.getForEntity("/api/secure/my-offers/" + accOffer.id, BaseOffer.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private BaseOffer[] listOffers() {
        ResponseEntity<PageableResponse<BaseOffer>> list = restTemplate.exchange(
                "/api/secure/my-offers", HttpMethod.GET, null, new ParameterizedTypeReference<>() {}
        );
        return list.getBody().content;
    }

    private <T extends BaseOffer> T postOffer(T request, String urlSuffix, Class<T> clazz) {
        ResponseEntity<T> response = restTemplate.postForEntity("/api/secure/" + urlSuffix, request, clazz);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        T entity = response.getBody();
        assertThat(entity.id).isNotNull();
        assertThat(entity).usingRecursiveComparison().ignoringFields("id").isEqualTo(request);
        return entity;
    }
}