package pl.gov.coi.pomocua.ads.accomodations;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.gov.coi.pomocua.ads.BaseResourceTest;
import pl.gov.coi.pomocua.ads.Location;
import pl.gov.coi.pomocua.ads.UserId;
import pl.gov.coi.pomocua.ads.accomodations.AccommodationOffer.Language;
import pl.gov.coi.pomocua.ads.accomodations.AccommodationOffer.LengthOfStay;

import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class AccommodationsResourceTest extends BaseResourceTest<AccommodationOffer> {

    @Autowired
    private AccommodationsRepository repository;

    @Override
    protected Class<AccommodationOffer> getClazz() {
        return AccommodationOffer.class;
    }

    @Override
    protected String getOfferSuffix() {
        return "accommodations";
    }

    @Override
    protected AccommodationOffer sampleOfferRequest() {
        return AccommodationsTestDataGenerator.sampleOffer();
    }

    @Override
    protected CrudRepository<AccommodationOffer, Long> getRepository() {
        return repository;
    }

    @Nested
    class CreatingValidation {
        @ParameterizedTest
        @NullSource
        @MethodSource("pl.gov.coi.pomocua.ads.accomodations.AccommodationsResourceTest#invalidLocations")
        void shouldRejectMissingOrInvalidLocation(Location location) {
            AccommodationOffer offer = sampleOfferRequest();
            offer.location = location;

            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }

        @ParameterizedTest
        @NullSource
        @ValueSource(ints = {-1, 0})
        void shouldRejectMissingOrInvalidGuests(Integer guests) {
            AccommodationOffer offer = sampleOfferRequest();
            offer.guests = guests;

            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }

        @Test
        void shouldRejectMissingLengthOfStay() {
            AccommodationOffer offer = sampleOfferRequest();
            offer.lengthOfStay = null;

            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }

        @ParameterizedTest
        @NullAndEmptySource
        void shouldRejectMissingHostLanguage(List<Language> hostLanguage) {
            AccommodationOffer offer = sampleOfferRequest();
            offer.hostLanguage = hostLanguage;

            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }
    }

    @Nested
    class Searching {
        @Test
        void shouldReturnOffersByFullCriteria() {
            AccommodationOffer response = postSampleOffer();

            String requestParams = "/MAzowIEckie/waRszaWA?capacity=4";
            var offers = listOffers(requestParams);

            assertThat(offers).contains(response);
        }

        @Test
        void shouldNotFindOffersForTooHighCapacity() {
            postSampleOffer();

            String requestParams = "/MAzowIEckie/waRszaWA?capacity=15";
            var offers = listOffers(requestParams);

            assertThat(offers).isEmpty();
        }

        @Test
        void shouldNotFindOffersForWrongLocation() {
            postSampleOffer();

            String requestParams = "/MAłopolskie/Kraków?capacity=1";
            var offers = listOffers(requestParams);

            assertThat(offers).isEmpty();
        }

        @Test
        void shouldReturnOffersByCriteriaWithoutCapacity() {
            AccommodationOffer response = postSampleOffer();

            String requestParams = "/mazowIEckie/WARszaWA";
            var offers = listOffers(requestParams);

            assertThat(offers).contains(response);
        }

        @Test
        void shouldIgnoreDeactivatedOffer() {
            AccommodationOffer offer = postSampleOffer();
            deleteOffer(offer.id);

            String requestParams = "/mazowIEckie/WARszaWA";
            var offers = listOffers(requestParams);

            assertThat(offers).isEmpty();
        }

        @Test
        void shouldReturnOffersByCapacityOnly() {
            AccommodationOffer response = postSampleOffer();

            String requestParams = "?capacity=1";
            var offers = listOffers(requestParams);

            assertThat(offers).contains(response);
        }

        @Test
        void shouldIgnoreDeactivatedOfferByCapacityOnly() {
            AccommodationOffer offer = postSampleOffer();
            deleteOffer(offer.id);

            String requestParams = "?capacity=1";
            var offers = listOffers(requestParams);

            assertThat(offers).isEmpty();
        }
    }

    @Nested
    class Updating {
        @Test
        void shouldUpdateOffer() {
            AccommodationOffer offer = postSampleOffer();
            var updateJson = AccommodationsTestDataGenerator.sampleUpdateJson();

            ResponseEntity<Void> response = updateOffer(offer.id, updateJson);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
            AccommodationOffer updatedOffer = getOfferFromRepository(offer.id);
            assertThat(updatedOffer.title).isEqualTo("new title");
            assertThat(updatedOffer.description).isEqualTo("new description");
            assertThat(updatedOffer.location.region).isEqualTo("Pomorskie");
            assertThat(updatedOffer.location.city).isEqualTo("Gdańsk");
            assertThat(updatedOffer.guests).isEqualTo(14);
            assertThat(updatedOffer.lengthOfStay).isEqualTo(LengthOfStay.MONTH_3);
            assertThat(updatedOffer.hostLanguage).containsExactly(Language.UA);
        }

        @Test
        public void shouldUpdateModifiedDate() {
            testTimeProvider.setCurrentTime(Instant.parse("2022-03-07T15:23:22Z"));
            AccommodationOffer offer = postSampleOffer();
            var updateJson = AccommodationsTestDataGenerator.sampleUpdateJson();

            testTimeProvider.setCurrentTime(Instant.parse("2022-04-01T12:00:00Z"));
            ResponseEntity<Void> response = updateOffer(offer.id, updateJson);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
            AccommodationOffer updatedOffer = getOfferFromRepository(offer.id);
            assertThat(updatedOffer.modifiedDate).isEqualTo(Instant.parse("2022-04-01T12:00:00Z"));
        }

        @Test
        void shouldReturn404WhenOfferDoesNotExist() {
            var updateJson = AccommodationsTestDataGenerator.sampleUpdateJson();

            ResponseEntity<Void> response = updateOffer(123L, updateJson);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        void shouldReturn404WhenOfferDeactivated() {
            AccommodationOffer offer = postSampleOffer();
            deleteOffer(offer.id);
            var updateJson = AccommodationsTestDataGenerator.sampleUpdateJson();

            ResponseEntity<Void> response = updateOffer(offer.id, updateJson);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        void shouldReturn404WhenOfferDoesNotBelongToCurrentUser() {
            testUser.setCurrentUserWithId(new UserId("other-user-2"));
            AccommodationOffer offer = postSampleOffer();
            var updateJson = AccommodationsTestDataGenerator.sampleUpdateJson();

            testUser.setCurrentUserWithId(new UserId("current-user-1"));
            ResponseEntity<Void> response = updateOffer(offer.id, updateJson);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Nested
        class Validation {
            @ParameterizedTest
            @NullAndEmptySource
            @ValueSource(strings = {"<", ">", "(", ")", "%", "@", "\"", "'"})
            void shouldRejectMissingOrInvalidTitle(String title) {
                AccommodationOffer offer = postSampleOffer();
                var updateJson = AccommodationsTestDataGenerator.sampleUpdateJson();
                updateJson.title = title;

                ResponseEntity<Void> response = updateOffer(offer.id, updateJson);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                AccommodationOffer notUpdatedOffer = getOfferFromRepository(offer.id);
                assertThat(notUpdatedOffer.title).isEqualTo(offer.title);
            }

            @Test
            void shouldRejectTooLongTitle() {
                AccommodationOffer offer = postSampleOffer();
                var updateJson = AccommodationsTestDataGenerator.sampleUpdateJson();
                updateJson.title = "a".repeat(81);

                ResponseEntity<Void> response = updateOffer(offer.id, updateJson);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                AccommodationOffer notUpdatedOffer = getOfferFromRepository(offer.id);
                assertThat(notUpdatedOffer.title).isEqualTo(offer.title);
            }

            @ParameterizedTest
            @NullAndEmptySource
            @ValueSource(strings = {"<", ">", "%", "\"", "'"})
            void shouldRejectMissingOrInvalidDescription(String description) {
                AccommodationOffer offer = postSampleOffer();
                var updateJson = AccommodationsTestDataGenerator.sampleUpdateJson();
                updateJson.description = description;

                ResponseEntity<Void> response = updateOffer(offer.id, updateJson);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                AccommodationOffer notUpdatedOffer = getOfferFromRepository(offer.id);
                assertThat(notUpdatedOffer.description).isEqualTo(offer.description);
            }

            @Test
            void shouldRejectTooLongDescription() {
                AccommodationOffer offer = postSampleOffer();
                var updateJson = AccommodationsTestDataGenerator.sampleUpdateJson();
                updateJson.description = "a".repeat(2001);

                ResponseEntity<Void> response = updateOffer(offer.id, updateJson);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                AccommodationOffer notUpdatedOffer = getOfferFromRepository(offer.id);
                assertThat(notUpdatedOffer.description).isEqualTo(offer.description);
            }

            @ParameterizedTest
            @NullSource
            @MethodSource("pl.gov.coi.pomocua.ads.accomodations.AccommodationsResourceTest#invalidLocations")
            void shouldRejectMissingOrInvalidLocation(Location location) {
                AccommodationOffer offer = postSampleOffer();
                var updateJson = AccommodationsTestDataGenerator.sampleUpdateJson();
                updateJson.location = location;

                ResponseEntity<Void> response = updateOffer(offer.id, updateJson);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                AccommodationOffer notUpdatedOffer = getOfferFromRepository(offer.id);
                assertThat(notUpdatedOffer.location).isEqualTo(offer.location);
            }

            @ParameterizedTest
            @NullSource
            @ValueSource(ints = {-1, 0})
            void shouldRejectMissingOrInvalidGuests(Integer guests) {
                AccommodationOffer offer = postSampleOffer();
                var updateJson = AccommodationsTestDataGenerator.sampleUpdateJson();
                updateJson.guests = guests;

                ResponseEntity<Void> response = updateOffer(offer.id, updateJson);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                AccommodationOffer notUpdatedOffer = getOfferFromRepository(offer.id);
                assertThat(notUpdatedOffer.guests).isEqualTo(offer.guests);
            }

            @Test
            void shouldRejectMissingLengthOfStay() {
                AccommodationOffer offer = postSampleOffer();
                var updateJson = AccommodationsTestDataGenerator.sampleUpdateJson();
                updateJson.lengthOfStay = null;

                ResponseEntity<Void> response = updateOffer(offer.id, updateJson);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                AccommodationOffer notUpdatedOffer = getOfferFromRepository(offer.id);
                assertThat(notUpdatedOffer.lengthOfStay).isEqualTo(offer.lengthOfStay);
            }

            @ParameterizedTest
            @NullAndEmptySource
            void shouldRejectMissingHostLanguage(List<Language> hostLanguage) {
                AccommodationOffer offer = postSampleOffer();
                var updateJson = AccommodationsTestDataGenerator.sampleUpdateJson();
                updateJson.hostLanguage = hostLanguage;

                ResponseEntity<Void> response = updateOffer(offer.id, updateJson);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                AccommodationOffer notUpdatedOffer = getOfferFromRepository(offer.id);
                assertThat(notUpdatedOffer.hostLanguage).containsExactlyInAnyOrderElementsOf(offer.hostLanguage);
            }

            @ParameterizedTest
            @ValueSource(strings = {"invalid phone", "+48 invalid phone", "0048123", "+48 123 123", "+48 123", "+48000000000", "0048123456"})
            void shouldRejectInvalidPhoneNumber(String invalidPhoneNumber) {
                AccommodationOffer offer = postSampleOffer();
                var updateJson = AccommodationsTestDataGenerator.sampleUpdateJson();
                updateJson.phoneNumber = invalidPhoneNumber;

                ResponseEntity<Void> response = updateOffer(offer.id, updateJson);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            }

            @Test
            void shouldAcceptMissingPhoneNumber() {
                AccommodationOffer offer = postSampleOffer();
                var updateJson = AccommodationsTestDataGenerator.sampleUpdateJson();
                updateJson.phoneNumber = null;

                ResponseEntity<Void> response = updateOffer(offer.id, updateJson);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
                AccommodationOffer updatedOffer = getOfferFromRepository(offer.id);
                assertThat(updatedOffer.phoneNumber).isNull();
            }

            @ParameterizedTest
            @ValueSource(strings = {"+48123456789", "+48 123 456 789", "+48 123-456-789", "0048 123456789", "0048 (123) 456-789"})
            void shouldAcceptPhoneNumberInVariousFormatsAndNormalizeIt(String phoneNumber) {
                AccommodationOffer offer = postSampleOffer();
                var updateJson = AccommodationsTestDataGenerator.sampleUpdateJson();
                updateJson.phoneNumber = phoneNumber;

                ResponseEntity<Void> response = updateOffer(offer.id, updateJson);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
                AccommodationOffer updatedOffer = getOfferFromRepository(offer.id);
                assertThat(updatedOffer.phoneNumber).isEqualTo("+48123456789");
            }
        }
    }

    static Stream<Location> invalidLocations() {
        return Stream.of(
                new Location(null, "city"),
                new Location("   ", "city"),
                new Location("region", null),
                new Location("region", "   ")
        );
    }
}
