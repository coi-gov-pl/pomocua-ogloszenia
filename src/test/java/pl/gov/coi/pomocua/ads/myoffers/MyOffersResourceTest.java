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
import pl.gov.coi.pomocua.ads.accomodations.AccommodationsResource;
import pl.gov.coi.pomocua.ads.accomodations.AccommodationsTestDataGenerator;
import pl.gov.coi.pomocua.ads.materialaid.MaterialAidOffer;
import pl.gov.coi.pomocua.ads.materialaid.MaterialAidTestDataGenerator;
import pl.gov.coi.pomocua.ads.users.TestUser;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestConfiguration.class)
class MyOffersResourceTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TestUser testUser;

    @AfterEach
    void tearDown() {
        testUser.setDefault();
    }

    @Test
    void shouldReturnDifferentOffersForUser() {
        testUser.setCurrentUserWithId(new UserId("my-offer user id"));

        AccommodationOffer accOffer = postOffer(AccommodationsTestDataGenerator.sampleOffer(), "accommodations", AccommodationOffer.class);
        MaterialAidOffer materialAidOffer = postOffer(MaterialAidTestDataGenerator.sampleOffer(), "material-aid", MaterialAidOffer.class);

        BaseOffer[] offers = listOffers();
        assertThat(offers).extracting("id").containsExactly(accOffer.id, materialAidOffer.id);
        assertThat(offers).extracting("title").containsExactly(accOffer.title, materialAidOffer.title);
        assertThat(offers).extracting("description").containsExactly(accOffer.description, materialAidOffer.description);
        assertThat(offers).extracting("status").doesNotContain(BaseOffer.Status.INACTIVE);
    }

    @Test
    void shouldReturnOffersOnlyForLoggedUser() {
        UserId accommodationOfferUserId = new UserId("acommodation offer user id");
        testUser.setCurrentUserWithId(accommodationOfferUserId);
        AccommodationOffer accOffer = postOffer(AccommodationsTestDataGenerator.sampleOffer(), "accommodations", AccommodationOffer.class);

        UserId materialAidOfferUserId = new UserId("job offer user id");
        testUser.setCurrentUserWithId(materialAidOfferUserId);
        postOffer(MaterialAidTestDataGenerator.sampleOffer(), "material-aid", MaterialAidOffer.class);

        testUser.setCurrentUserWithId(accommodationOfferUserId);
        BaseOffer[] offers = listOffers();
        assertThat(offers).hasSize(1);
        assertThat(offers).extracting("status").doesNotContain(BaseOffer.Status.INACTIVE);
        assertThat(offers[0]).isInstanceOf(AccommodationOffer.class);
        assertThat(offers[0]).isEqualTo(accOffer);
    }

    @Test
    void shouldReturnOfferForCurrentUser() {
        testUser.setCurrentUserWithId(new UserId("accommodation offer user id"));
        AccommodationOffer accOffer = postOffer(AccommodationsTestDataGenerator.sampleOffer(), "accommodations", AccommodationOffer.class);

        ResponseEntity<AccommodationOffer> response = restTemplate.getForEntity("/api/secure/my-offers/" + accOffer.id, AccommodationOffer.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(accOffer);
    }

    @Test
    void shouldReturnNotFoundWhenGettingOfferForOtherUser() {
        testUser.setCurrentUserWithId(new UserId("accommodation offer user id"));
        AccommodationOffer accOffer = postOffer(AccommodationsTestDataGenerator.sampleOffer(), "accommodations", AccommodationOffer.class);

        testUser.setCurrentUserWithId(new UserId("different user id"));

        ResponseEntity<BaseOffer> response = restTemplate.getForEntity("/api/secure/my-offers/" + accOffer.id, BaseOffer.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldReturnInactiveOffers() {
        AccommodationOffer accOffer = postOffer(AccommodationsTestDataGenerator.sampleOffer(), "accommodations", AccommodationOffer.class);
        deleteOffer("accommodations", AccommodationOffer.class, accOffer.id);
        ResponseEntity<BaseOffer> response = restTemplate.getForEntity("/api/secure/my-offers/" + accOffer.id, BaseOffer.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().status).isEqualTo(BaseOffer.Status.INACTIVE);
    }

    private <T extends BaseOffer> T[] listOffers() {
        ResponseEntity<PageableResponse<T>> list = restTemplate.exchange(
                "/api/secure/my-offers", HttpMethod.GET, null, new ParameterizedTypeReference<>() {}
        );
        return list.getBody().content;
    }

    private <T extends BaseOffer> T postOffer(T request, String urlSuffix, Class<T> clazz) {
        ResponseEntity<T> response = restTemplate.postForEntity("/api/secure/" + urlSuffix, request, clazz);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        T entity = response.getBody();
        assertThat(entity.id).isNotNull();
        assertThat(entity).usingRecursiveComparison()
                .ignoringFields("id", "modifiedDate", "userFirstName")
                .isEqualTo(request);
        return entity;
    }

    private <T extends BaseOffer> void deleteOffer(String urlSuffix, Class<T> clazz, Long id) {
        ParameterizedTypeReference<AccommodationsResource> accommodationsResource = new ParameterizedTypeReference<AccommodationsResource>() {};
        ResponseEntity<AccommodationsResource> responseDelete = restTemplate.exchange("/api/secure/" + urlSuffix + "/" + id, HttpMethod.DELETE, null, accommodationsResource, clazz);
        assertThat(responseDelete.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
