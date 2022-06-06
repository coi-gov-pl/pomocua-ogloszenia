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
import pl.gov.coi.pomocua.ads.Language;
import pl.gov.coi.pomocua.ads.Location;
import pl.gov.coi.pomocua.ads.UserId;
import pl.gov.coi.pomocua.ads.accomodations.AccommodationOffer.LengthOfStay;

import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class AccommodationsResourceTest extends BaseResourceTest<AccommodationOffer, AccommodationOfferVM> {

    @Autowired
    private AccommodationsRepository repository;

    @Override
    protected Class<AccommodationOfferVM> getClazz() {
        return AccommodationOfferVM.class;
    }

    @Override
    protected String getOfferSuffix() {
        return "accommodations";
    }

    @Override
    protected AccommodationOfferVM sampleOfferRequest() {
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
            AccommodationOfferVM offer = sampleOfferRequest();
            offer.setLocation(location);

            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }

        @ParameterizedTest
        @NullSource
        @ValueSource(ints = {-1, 0})
        void shouldRejectMissingOrInvalidGuests(Integer guests) {
            AccommodationOfferVM offer = sampleOfferRequest();
            offer.setGuests(guests);

            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }

        @Test
        void shouldRejectMissingLengthOfStay() {
            AccommodationOfferVM offer = sampleOfferRequest();
            offer.setLengthOfStay(null);

            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }

        @ParameterizedTest
        @NullAndEmptySource
        void shouldRejectMissingHostLanguage(List<Language> hostLanguage) {
            AccommodationOfferVM offer = sampleOfferRequest();
            offer.setHostLanguage(hostLanguage);

            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }
    }

    @Nested
    class Searching {
        @Test
        void shouldReturnOffersByFullCriteria() {
            AccommodationOfferVM response = postSampleOffer();

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
            AccommodationOfferVM response = postSampleOffer();

            String requestParams = "/mazowIEckie/WARszaWA";
            var offers = listOffers(requestParams);

            assertThat(offers).contains(response);
        }

        @Test
        void shouldIgnoreDeactivatedOffer() {
            AccommodationOfferVM offer = postSampleOffer();
            deleteOffer(offer.getId());

            String requestParams = "/mazowIEckie/WARszaWA";
            var offers = listOffers(requestParams);

            assertThat(offers).isEmpty();
        }

        @Test
        void shouldReturnOffersByCapacityOnly() {
            AccommodationOfferVM response = postSampleOffer();

            String requestParams = "?capacity=1";
            var offers = listOffers(requestParams);

            assertThat(offers).contains(response);
        }

        @Test
        void shouldIgnoreDeactivatedOfferByCapacityOnly() {
            AccommodationOfferVM offer = postSampleOffer();
            deleteOffer(offer.getId());

            String requestParams = "?capacity=1";
            var offers = listOffers(requestParams);

            assertThat(offers).isEmpty();
        }

        @Test
        void shouldSearchWithManyHostLanguages() {
            AccommodationOfferVM offer = sampleOfferRequest();
            offer.setHostLanguage(List.of(Language.EN, Language.PL));
            postOffer(offer);

            var offers = listOffers();

            assertThat(offers)
                    .hasSize(1)
                    .element(0)
                    .extracting(o -> o.getHostLanguage())
                    .isEqualTo(List.of(Language.EN, Language.PL));
        }

        @Test
        void shouldSearchWithOneHostLanguage() {
            AccommodationOfferVM offer = sampleOfferRequest();
            offer.setHostLanguage(List.of(Language.PL));
            postOffer(offer);

            var offers = listOffers();

            assertThat(offers)
                    .hasSize(1)
                    .element(0)
                    .extracting(o -> o.getHostLanguage())
                    .isEqualTo(List.of(Language.PL));
        }
    }

    @Nested
    class Updating {
        @Test
        void shouldUpdateOffer() {
            AccommodationOfferVM offer = postSampleOffer();
            var updateJson = AccommodationsTestDataGenerator.sampleUpdateJson();

            ResponseEntity<Void> response = updateOffer(offer.getId(), updateJson);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
            AccommodationOffer updatedOffer = getOfferFromRepository(offer.getId());
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
            AccommodationOfferVM offer = postSampleOffer();
            var updateJson = AccommodationsTestDataGenerator.sampleUpdateJson();

            testTimeProvider.setCurrentTime(Instant.parse("2022-04-01T12:00:00Z"));
            ResponseEntity<Void> response = updateOffer(offer.getId(), updateJson);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
            AccommodationOffer updatedOffer = getOfferFromRepository(offer.getId());
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
            AccommodationOfferVM offer = postSampleOffer();
            deleteOffer(offer.getId());
            var updateJson = AccommodationsTestDataGenerator.sampleUpdateJson();

            ResponseEntity<Void> response = updateOffer(offer.getId(), updateJson);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        void shouldReturn404WhenOfferDoesNotBelongToCurrentUser() {
            testUser.setCurrentUserWithId(new UserId("other-user-2"));
            AccommodationOfferVM offer = postSampleOffer();
            var updateJson = AccommodationsTestDataGenerator.sampleUpdateJson();

            testUser.setCurrentUserWithId(new UserId("current-user-1"));
            ResponseEntity<Void> response = updateOffer(offer.getId(), updateJson);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Nested
        class Validation {
            @ParameterizedTest
            @NullAndEmptySource
            @ValueSource(strings = {"<", ">", "(", ")", "%", "@", "\""})
            void shouldRejectMissingOrInvalidTitle(String title) {
                AccommodationOfferVM offer = postSampleOffer();
                var updateJson = AccommodationsTestDataGenerator.sampleUpdateJson();
                updateJson.title = title;

                ResponseEntity<Void> response = updateOffer(offer.getId(), updateJson);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                AccommodationOffer notUpdatedOffer = getOfferFromRepository(offer.getId());
                assertThat(notUpdatedOffer.title).isEqualTo(offer.getTitle());
            }

            @Test
            void shouldRejectTooLongTitle() {
                AccommodationOfferVM offer = postSampleOffer();
                var updateJson = AccommodationsTestDataGenerator.sampleUpdateJson();
                updateJson.title = "a".repeat(81);

                ResponseEntity<Void> response = updateOffer(offer.getId(), updateJson);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                AccommodationOffer notUpdatedOffer = getOfferFromRepository(offer.getId());
                assertThat(notUpdatedOffer.title).isEqualTo(offer.getTitle());
            }

            @ParameterizedTest
            @NullAndEmptySource
            @ValueSource(strings = {"<", ">", "%", "\""})
            void shouldRejectMissingOrInvalidDescription(String description) {
                AccommodationOfferVM offer = postSampleOffer();
                var updateJson = AccommodationsTestDataGenerator.sampleUpdateJson();
                updateJson.description = description;

                ResponseEntity<Void> response = updateOffer(offer.getId(), updateJson);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                AccommodationOffer notUpdatedOffer = getOfferFromRepository(offer.getId());
                assertThat(notUpdatedOffer.description).isEqualTo(offer.getDescription());
            }

            @Test
            void shouldRejectTooLongDescription() {
                AccommodationOfferVM offer = postSampleOffer();
                var updateJson = AccommodationsTestDataGenerator.sampleUpdateJson();
                updateJson.description = "a".repeat(2001);

                ResponseEntity<Void> response = updateOffer(offer.getId(), updateJson);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                AccommodationOffer notUpdatedOffer = getOfferFromRepository(offer.getId());
                assertThat(notUpdatedOffer.description).isEqualTo(offer.getDescription());
            }

            @ParameterizedTest
            @NullSource
            @MethodSource("pl.gov.coi.pomocua.ads.accomodations.AccommodationsResourceTest#invalidLocations")
            void shouldRejectMissingOrInvalidLocation(Location location) {
                AccommodationOfferVM offer = postSampleOffer();
                var updateJson = AccommodationsTestDataGenerator.sampleUpdateJson();
                updateJson.location = location;

                ResponseEntity<Void> response = updateOffer(offer.getId(), updateJson);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                AccommodationOffer notUpdatedOffer = getOfferFromRepository(offer.getId());
                assertThat(notUpdatedOffer.location).isEqualTo(offer.getLocation());
            }

            @ParameterizedTest
            @NullSource
            @ValueSource(ints = {-1, 0})
            void shouldRejectMissingOrInvalidGuests(Integer guests) {
                AccommodationOfferVM offer = postSampleOffer();
                var updateJson = AccommodationsTestDataGenerator.sampleUpdateJson();
                updateJson.guests = guests;

                ResponseEntity<Void> response = updateOffer(offer.getId(), updateJson);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                AccommodationOffer notUpdatedOffer = getOfferFromRepository(offer.getId());
                assertThat(notUpdatedOffer.guests).isEqualTo(offer.getGuests());
            }

            @Test
            void shouldRejectMissingLengthOfStay() {
                AccommodationOfferVM offer = postSampleOffer();
                var updateJson = AccommodationsTestDataGenerator.sampleUpdateJson();
                updateJson.lengthOfStay = null;

                ResponseEntity<Void> response = updateOffer(offer.getId(), updateJson);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                AccommodationOffer notUpdatedOffer = getOfferFromRepository(offer.getId());
                assertThat(notUpdatedOffer.lengthOfStay).isEqualTo(offer.getLengthOfStay());
            }

            @ParameterizedTest
            @NullAndEmptySource
            void shouldRejectMissingHostLanguage(List<Language> hostLanguage) {
                AccommodationOfferVM offer = postSampleOffer();
                var updateJson = AccommodationsTestDataGenerator.sampleUpdateJson();
                updateJson.hostLanguage = hostLanguage;

                ResponseEntity<Void> response = updateOffer(offer.getId(), updateJson);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                AccommodationOffer notUpdatedOffer = getOfferFromRepository(offer.getId());
                assertThat(notUpdatedOffer.hostLanguage).containsExactlyInAnyOrderElementsOf(offer.getHostLanguage());
            }

            @ParameterizedTest
            @ValueSource(strings = {"invalid phone", "+48 invalid phone", "0048123", "+48 123 123", "+48 123", "+48000000000", "0048123456"})
            void shouldRejectInvalidPhoneNumber(String invalidPhoneNumber) {
                AccommodationOfferVM offer = postSampleOffer();
                var updateJson = AccommodationsTestDataGenerator.sampleUpdateJson();
                updateJson.phoneNumber = invalidPhoneNumber;

                ResponseEntity<Void> response = updateOffer(offer.getId(), updateJson);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            }

            @Test
            void shouldAcceptMissingPhoneNumber() {
                AccommodationOfferVM offer = postSampleOffer();
                var updateJson = AccommodationsTestDataGenerator.sampleUpdateJson();
                updateJson.phoneNumber = null;

                ResponseEntity<Void> response = updateOffer(offer.getId(), updateJson);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
                AccommodationOffer updatedOffer = getOfferFromRepository(offer.getId());
                assertThat(updatedOffer.phoneNumber).isNull();
            }

            @ParameterizedTest
            @ValueSource(strings = {"+48123456789", "+48 123 456 789", "+48 123-456-789", "0048 123456789", "0048 (123) 456-789"})
            void shouldAcceptPhoneNumberInVariousFormatsAndNormalizeIt(String phoneNumber) {
                AccommodationOfferVM offer = postSampleOffer();
                var updateJson = AccommodationsTestDataGenerator.sampleUpdateJson();
                updateJson.phoneNumber = phoneNumber;

                ResponseEntity<Void> response = updateOffer(offer.getId(), updateJson);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
                AccommodationOffer updatedOffer = getOfferFromRepository(offer.getId());
                assertThat(updatedOffer.phoneNumber).isEqualTo("123456789");
                assertThat(updatedOffer.phoneCountryCode).isEqualTo("48");
            }

            @ParameterizedTest
            @MethodSource("pl.gov.coi.pomocua.ads.accomodations.AccommodationsResourceTest#validLocations")
            void shouldAcceptValidLocations(Location location) {
                AccommodationOfferVM offer = postSampleOffer();
                var updateJson = AccommodationsTestDataGenerator.sampleUpdateJson();
                updateJson.location = location;

                ResponseEntity<Void> response = updateOffer(offer.getId(), updateJson);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
                AccommodationOffer updatedOffer = getOfferFromRepository(offer.getId());
                assertThat(updatedOffer.location).isEqualTo(location);
            }
        }
    }

    static Stream<Location> invalidLocations() {
        return Stream.of(
                new Location(null, "city"),
                new Location("   ", "city"),
                new Location("region", null),
                new Location("region", "   "),
                new Location("region", "Warblewo<>\"{}$#"),
                new Location("<>\"{}$#woj. pomorskie, pow. słupski, gm. Słupsk", "city")
        );
    }

    static Stream<Location> validLocations() {
        return Stream.of(
                new Location("woj. dolnośląskie, pow. kłodzki, gm. Stronie Śląskie", "Stronie Śląskie (miasto)"),
                new Location("woj. dolnośląskie, pow. dzierżoniowski, gm. Niemcza", "Przerzeczyn-Zdrój"),
                new Location("woj. zachodniopomorskie, pow. policki, gm. Dobra (Szczecińska)", "Wołczkowo"),
                new Location("woj. dolnośląskie, pow. kłodzki, gm. Lądek-Zdrój", "Konradów")
        );
    }
}
