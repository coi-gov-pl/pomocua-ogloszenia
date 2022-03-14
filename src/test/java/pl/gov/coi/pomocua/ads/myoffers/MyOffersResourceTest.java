package pl.gov.coi.pomocua.ads.myoffers;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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
import pl.gov.coi.pomocua.ads.materialaid.MaterialAidOffer;
import pl.gov.coi.pomocua.ads.materialaid.MaterialAidTestDataGenerator;
import pl.gov.coi.pomocua.ads.users.TestUser;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.gov.coi.pomocua.ads.transport.TransportTestDataGenerator.aTransportOffer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestConfiguration.class)
@AutoConfigureEmbeddedDatabase(refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_CLASS)
class MyOffersResourceTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TestUser testUser;

    @AfterEach
    void tearDown() {
        testUser.setDefault();
    }

    @Nested
    class GettingList {
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
            assertThat(offers[0]).isInstanceOf(AccommodationOffer.class);
            assertThat(offers[0]).isEqualTo(accOffer);
        }

        @ParameterizedTest
        @MethodSource("pl.gov.coi.pomocua.ads.myoffers.MyOffersResourceTest#differentOfferTypes")
        void shouldNotReturnDeactivatedOffer(BaseOffer offer, String urlSuffix) {
            BaseOffer createdOffer = postOffer(offer, urlSuffix, BaseOffer.class);
            deleteOffer(createdOffer.id, urlSuffix);

            BaseOffer[] offers = listOffers();

            assertThat(offers).isEmpty();
        }
    }

    @Nested
    class GettingSingle {
        @Test
        void shouldReturnOfferForCurrentUser() {
            testUser.setCurrentUserWithId(new UserId("accommodation offer user id"));
            AccommodationOffer accOffer = postOffer(AccommodationsTestDataGenerator.sampleOffer(), "accommodations", AccommodationOffer.class);

            ResponseEntity<AccommodationOffer> response = getOffer(accOffer.id, AccommodationOffer.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo(accOffer);
        }

        @Test
        void shouldReturnNotFoundWhenGettingOfferForOtherUser() {
            testUser.setCurrentUserWithId(new UserId("accommodation offer user id"));
            AccommodationOffer accOffer = postOffer(AccommodationsTestDataGenerator.sampleOffer(), "accommodations", AccommodationOffer.class);

            testUser.setCurrentUserWithId(new UserId("different user id"));
            ResponseEntity<AccommodationOffer> response = getOffer(accOffer.id, AccommodationOffer.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @ParameterizedTest
        @MethodSource("pl.gov.coi.pomocua.ads.myoffers.MyOffersResourceTest#differentOfferTypes")
        void shouldNotReturnDeactivatedOffer(BaseOffer offer, String urlSuffix) {
            BaseOffer createdOffer = postOffer(offer, urlSuffix, BaseOffer.class);
            deleteOffer(createdOffer.id, urlSuffix);

            ResponseEntity<BaseOffer> response = getOffer(createdOffer.id, BaseOffer.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    private static Stream<Arguments> differentOfferTypes() {
        return Stream.of(
                Arguments.of(AccommodationsTestDataGenerator.sampleOffer(), "accommodations"),
                Arguments.of(MaterialAidTestDataGenerator.sampleOffer(), "material-aid"),
                Arguments.of(aTransportOffer().build(), "transport")
        );
    }

    private <T extends BaseOffer> T[] listOffers() {
        ResponseEntity<PageableResponse<T>> list = restTemplate.exchange(
                "/api/secure/my-offers", HttpMethod.GET, null, new ParameterizedTypeReference<>() {}
        );
        return list.getBody().content;
    }

    private <T extends BaseOffer> ResponseEntity<T> getOffer(Long id, Class<T> clazz) {
        return restTemplate.getForEntity("/api/secure/my-offers/" + id, clazz);
    }

    private <T extends BaseOffer> T postOffer(T request, String urlSuffix, Class<T> clazz) {
        ResponseEntity<T> response = restTemplate.postForEntity("/api/secure/" + urlSuffix, request, clazz);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        return response.getBody();
    }

    private void deleteOffer(Long id, String urlSuffix) {
        restTemplate.delete("/api/secure/%s/%d".formatted(urlSuffix, id));
    }
}
