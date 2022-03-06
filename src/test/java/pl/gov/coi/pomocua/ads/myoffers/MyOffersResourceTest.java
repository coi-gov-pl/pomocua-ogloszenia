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
import pl.gov.coi.pomocua.ads.*;
import pl.gov.coi.pomocua.ads.accomodations.AccommodationOffer;
import pl.gov.coi.pomocua.ads.authentication.TestCurrentUser;
import pl.gov.coi.pomocua.ads.jobs.JobOffer;

import java.util.List;

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

        AccommodationOffer accOffer = postOffer(sampleAccommodationOfferRequest());
        JobOffer jobOffer = postOffer(sampleJobOfferRequest());

        BaseOffer[] offers = listOffers();
        assertThat(offers).extracting("title").contains(accOffer.title, jobOffer.title);
    }

    private BaseOffer[] listOffers() {
        ResponseEntity<PageableResponse<AccommodationOffer>> list = restTemplate.exchange(
                "/api/secure/my-offers", HttpMethod.GET, null, new ParameterizedTypeReference<>() {}
        );
        return list.getBody().content;
    }

    private AccommodationOffer postOffer(AccommodationOffer request) {
        ResponseEntity<AccommodationOffer> response = restTemplate.postForEntity("/api/secure/accommodation", request, AccommodationOffer.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        AccommodationOffer entity = response.getBody();
        assertThat(entity.id).isNotNull();
        assertThat(entity).usingRecursiveComparison().ignoringFields("id").isEqualTo(request);
        return entity;
    }

    private JobOffer postOffer(JobOffer request) {
        ResponseEntity<JobOffer> response = restTemplate.postForEntity("/api/secure/job", request, JobOffer.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        JobOffer entity = response.getBody();
        assertThat(entity.id).isNotNull();
        assertThat(entity).usingRecursiveComparison().ignoringFields("id").isEqualTo(request);
        return entity;
    }

    private AccommodationOffer sampleAccommodationOfferRequest() {
        AccommodationOffer request = new AccommodationOffer();
        request.title = "sample work";
        request.location = new Location("Mazowieckie", "Warszawa");
        request.hostLanguage = List.of(AccommodationOffer.Language.PL, AccommodationOffer.Language.UA);
        request.description = "description";
        request.lengthOfStay = AccommodationOffer.LengthOfStay.MONTH_2;
        request.guests = 5;
        return request;
    }

    private JobOffer sampleJobOfferRequest() {
        JobOffer request = new JobOffer();
        request.title = "sample work";
        request.mode = JobOffer.Mode.REMOTE;
        request.location = new Location("Mazowieckie", "Warszawa");
        request.type = List.of(JobOffer.Type.TEMPORARY);
        request.language = List.of(JobOffer.Language.PL, JobOffer.Language.UA);
        request.description = "description";
        return request;
    }
}