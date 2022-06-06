package pl.gov.coi.pomocua.ads.materialaid;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;
import pl.gov.coi.pomocua.ads.BaseResourceTest;
import pl.gov.coi.pomocua.ads.Location;
import pl.gov.coi.pomocua.ads.OffersVM;
import pl.gov.coi.pomocua.ads.UserId;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.gov.coi.pomocua.ads.materialaid.MaterialAidTestDataGenerator.aMaterialAidOffer;

class MaterialAidResourceTest extends BaseResourceTest<MaterialAidOffer, MaterialAidOfferVM> {

    @Autowired
    private MaterialAidOfferRepository repository;

    @Override
    protected Class<MaterialAidOfferVM> getClazz() {
        return MaterialAidOfferVM.class;
    }

    @Override
    protected String getOfferSuffix() {
        return "material-aid";
    }

    @Override
    protected CrudRepository<MaterialAidOffer, Long> getRepository() {
        return repository;
    }

    @Override
    protected MaterialAidOfferVM sampleOfferRequest() {
        return aMaterialAidOffer().build();
    }

    @Nested
    class Searching {
        @Test
        void shouldSearchByLocation() {
            MaterialAidOfferVM offer1 = postOffer(aMaterialAidOffer()
                    .location(new Location("Mazowieckie", "Warszawa"))
                    .category(MaterialAidCategory.CLOTHING)
                    .build());

            postOffer(aMaterialAidOffer()
                    .location(new Location("Pomorskie", "Gdańsk"))
                    .category(MaterialAidCategory.CLOTHING)
                    .build());

            MaterialAidOfferSearchCriteria searchCriteria = new MaterialAidOfferSearchCriteria();
            searchCriteria.setLocation(new Location("Mazowieckie", "Warszawa"));
            var results = searchOffers(searchCriteria);

            assertThat(results).hasSize(1).contains(offer1);
        }

        @Test
        void shouldSearchByCategory() {
            postOffer(aMaterialAidOffer()
                    .location(new Location("Mazowieckie", "Warszawa"))
                    .category(MaterialAidCategory.CLOTHING)
                    .build());

            MaterialAidOfferVM offer2 = postOffer(aMaterialAidOffer()
                    .location(new Location("Pomorskie", "Gdańsk"))
                    .category(MaterialAidCategory.FOOD)
                    .build());

            MaterialAidOfferVM offer3 = postOffer(aMaterialAidOffer()
                    .location(new Location("Pomorskie", "Gdynia"))
                    .category(MaterialAidCategory.FOOD)
                    .build());

            MaterialAidOfferSearchCriteria searchCriteria = new MaterialAidOfferSearchCriteria();
            searchCriteria.setCategory(MaterialAidCategory.FOOD);
            var results = searchOffers(searchCriteria);

            assertThat(results).hasSize(2).contains(offer2, offer3);
        }

        @Test
        void shouldReturnPageOfData() {
            postOffer(aMaterialAidOffer().title("a").build());
            postOffer(aMaterialAidOffer().title("b").build());
            postOffer(aMaterialAidOffer().title("c").build());
            postOffer(aMaterialAidOffer().title("d").build());
            postOffer(aMaterialAidOffer().title("e").build());
            postOffer(aMaterialAidOffer().title("f").build());

            PageRequest page = PageRequest.of(1, 2);
            var results = searchOffers(page);

            assertThat(results.totalElements).isEqualTo(6);
            assertThat(results.content)
                    .hasSize(2)
                    .extracting(r -> r.getTitle())
                    .containsExactly("c", "d");
        }

        @Test
        void shouldIgnoreDeactivatedOffer() {
            MaterialAidOfferVM offer = postOffer(aMaterialAidOffer().build());
            deleteOffer(offer.getId());

            PageRequest page = PageRequest.of(0, 10);
            var results = searchOffers(page);

            assertThat(results.totalElements).isEqualTo(0);
            assertThat(results.content).isEmpty();
        }

        @Test
        void shouldIgnoreCaseWhenSearchByLocation() {
            MaterialAidOfferVM offer1 = postOffer(aMaterialAidOffer()
                .location(new Location("Mazowieckie", "Warszawa"))
                .category(MaterialAidCategory.CLOTHING)
                .build());

            postOffer(aMaterialAidOffer()
                .location(new Location("Pomorskie", "Gdańsk"))
                .category(MaterialAidCategory.CLOTHING)
                .build());

            MaterialAidOfferSearchCriteria searchCriteria = new MaterialAidOfferSearchCriteria();
            searchCriteria.setLocation(new Location("Mazowieckie", "WarSZAwa"));
            var results = searchOffers(searchCriteria);

            assertThat(results).hasSize(1).contains(offer1);
        }
    }

    @Nested
    class ValidationTest {
        @Test
        void shouldRejectNullCategory() {
            MaterialAidOfferVM offer = sampleOfferRequest();
            offer.setCategory(null);
            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }

        @Test
        void shouldRejectNullLocation() {
            MaterialAidOfferVM offer = sampleOfferRequest();
            offer.setLocation(null);
            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }
    }

    @Nested
    class Updating {
        @Test
        void shouldUpdateOffer() {
            MaterialAidOfferVM offer = postSampleOffer();
            var updateJson = MaterialAidTestDataGenerator.sampleUpdateJson();

            ResponseEntity<Void> response = updateOffer(offer.getId(), updateJson);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
            MaterialAidOffer updatedOffer = getOfferFromRepository(offer.getId());
            assertThat(updatedOffer.title).isEqualTo("new title");
            assertThat(updatedOffer.description).isEqualTo("new description");
            assertThat(updatedOffer.location.region).isEqualTo("Pomorskie");
            assertThat(updatedOffer.location.city).isEqualTo("Gdańsk");
            assertThat(updatedOffer.category).isEqualTo(MaterialAidCategory.FOR_CHILDREN);
        }

        @Test
        public void shouldUpdateModifiedDate() {
            testTimeProvider.setCurrentTime(Instant.parse("2022-03-07T15:23:22Z"));
            MaterialAidOfferVM offer = postSampleOffer();
            var updateJson = MaterialAidTestDataGenerator.sampleUpdateJson();

            testTimeProvider.setCurrentTime(Instant.parse("2022-04-01T12:00:00Z"));
            ResponseEntity<Void> response = updateOffer(offer.getId(), updateJson);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
            MaterialAidOffer updatedOffer = getOfferFromRepository(offer.getId());
            assertThat(updatedOffer.modifiedDate).isEqualTo(Instant.parse("2022-04-01T12:00:00Z"));
        }

        @Test
        void shouldReturn404WhenOfferDoesNotExist() {
            var updateJson = MaterialAidTestDataGenerator.sampleUpdateJson();

            ResponseEntity<Void> response = updateOffer(123L, updateJson);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        void shouldReturn404WhenOfferDeactivated() {
            MaterialAidOfferVM offer = postSampleOffer();
            deleteOffer(offer.getId());
            var updateJson = MaterialAidTestDataGenerator.sampleUpdateJson();

            ResponseEntity<Void> response = updateOffer(offer.getId(), updateJson);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        void shouldReturn404WhenOfferDoesNotBelongToCurrentUser() {
            testUser.setCurrentUserWithId(new UserId("other-user-2"));
            MaterialAidOfferVM offer = postSampleOffer();
            var updateJson = MaterialAidTestDataGenerator.sampleUpdateJson();

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
                MaterialAidOfferVM offer = postSampleOffer();
                var updateJson = MaterialAidTestDataGenerator.sampleUpdateJson();
                updateJson.title = title;

                ResponseEntity<Void> response = updateOffer(offer.getId(), updateJson);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                MaterialAidOffer notUpdatedOffer = getOfferFromRepository(offer.getId());
                assertThat(notUpdatedOffer.title).isEqualTo(offer.getTitle());
            }

            @Test
            void shouldRejectTooLongTitle() {
                MaterialAidOfferVM offer = postSampleOffer();
                var updateJson = MaterialAidTestDataGenerator.sampleUpdateJson();
                updateJson.title = "a".repeat(81);

                ResponseEntity<Void> response = updateOffer(offer.getId(), updateJson);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                MaterialAidOffer notUpdatedOffer = getOfferFromRepository(offer.getId());
                assertThat(notUpdatedOffer.title).isEqualTo(offer.getTitle());
            }

            @ParameterizedTest
            @NullAndEmptySource
            @ValueSource(strings = {"<", ">", "%", "\""})
            void shouldRejectMissingOrInvalidDescription(String description) {
                MaterialAidOfferVM offer = postSampleOffer();
                var updateJson = MaterialAidTestDataGenerator.sampleUpdateJson();
                updateJson.description = description;

                ResponseEntity<Void> response = updateOffer(offer.getId(), updateJson);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                MaterialAidOffer notUpdatedOffer = getOfferFromRepository(offer.getId());
                assertThat(notUpdatedOffer.description).isEqualTo(offer.getDescription());
            }

            @Test
            void shouldRejectTooLongDescription() {
                MaterialAidOfferVM offer = postSampleOffer();
                var updateJson = MaterialAidTestDataGenerator.sampleUpdateJson();
                updateJson.description = "a".repeat(2001);

                ResponseEntity<Void> response = updateOffer(offer.getId(), updateJson);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                MaterialAidOffer notUpdatedOffer = getOfferFromRepository(offer.getId());
                assertThat(notUpdatedOffer.description).isEqualTo(offer.getDescription());
            }

            @ParameterizedTest
            @NullSource
            @MethodSource("pl.gov.coi.pomocua.ads.materialaid.MaterialAidResourceTest#invalidLocations")
            void shouldRejectMissingOrInvalidLocation(Location location) {
                MaterialAidOfferVM offer = postSampleOffer();
                var updateJson = MaterialAidTestDataGenerator.sampleUpdateJson();
                updateJson.location = location;

                ResponseEntity<Void> response = updateOffer(offer.getId(), updateJson);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                MaterialAidOffer notUpdatedOffer = getOfferFromRepository(offer.getId());
                assertThat(notUpdatedOffer.location).isEqualTo(offer.getLocation());
            }

            @Test
            void shouldRejectMissingCategory() {
                MaterialAidOfferVM offer = postSampleOffer();
                var updateJson = MaterialAidTestDataGenerator.sampleUpdateJson();
                updateJson.category = null;

                ResponseEntity<Void> response = updateOffer(offer.getId(), updateJson);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                MaterialAidOffer notUpdatedOffer = getOfferFromRepository(offer.getId());
                assertThat(notUpdatedOffer.category).isEqualTo(offer.getCategory());
            }

            @ParameterizedTest
            @ValueSource(strings = {"invalid phone", "+48 invalid phone", "0048123", "+48 123 123", "+48 123", "+48000000000", "0048123456"})
            void shouldRejectInvalidPhoneNumber(String invalidPhoneNumber) {
                MaterialAidOfferVM offer = postSampleOffer();
                var updateJson = MaterialAidTestDataGenerator.sampleUpdateJson();
                updateJson.phoneNumber = invalidPhoneNumber;

                ResponseEntity<Void> response = updateOffer(offer.getId(), updateJson);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            }

            @Test
            void shouldAcceptMissingPhoneNumber() {
                MaterialAidOfferVM offer = postSampleOffer();
                var updateJson = MaterialAidTestDataGenerator.sampleUpdateJson();
                updateJson.phoneNumber = null;

                ResponseEntity<Void> response = updateOffer(offer.getId(), updateJson);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
                MaterialAidOffer updatedOffer = getOfferFromRepository(offer.getId());
                assertThat(updatedOffer.phoneNumber).isNull();
            }

            @ParameterizedTest
            @ValueSource(strings = {"+48123456789", "+48 123 456 789", "+48 123-456-789", "0048 123456789", "0048 (123) 456-789"})
            void shouldAcceptPhoneNumberInVariousFormatsAndNormalizeIt(String phoneNumber) {
                MaterialAidOfferVM offer = postSampleOffer();
                var updateJson = MaterialAidTestDataGenerator.sampleUpdateJson();
                updateJson.phoneNumber = phoneNumber;

                ResponseEntity<Void> response = updateOffer(offer.getId(), updateJson);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
                MaterialAidOffer updatedOffer = getOfferFromRepository(offer.getId());
                assertThat(updatedOffer.phoneNumber).isEqualTo("123456789");
                assertThat(updatedOffer.phoneCountryCode).isEqualTo("48");
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

    private List<MaterialAidOfferVM> searchOffers(MaterialAidOfferSearchCriteria searchCriteria) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/api/" + getOfferSuffix());
        if (searchCriteria.getLocation() != null) {
            builder
                    .queryParam("location.city", searchCriteria.getLocation().getCity())
                    .queryParam("location.region", searchCriteria.getLocation().getRegion());
        }
        if (searchCriteria.getCategory() != null) {
            builder.queryParam("category", searchCriteria.getCategory());
        }
        String url = builder.encode().toUriString();
        return listOffers(URI.create(url)).content;
    }

    private OffersVM<MaterialAidOfferVM> searchOffers(PageRequest pageRequest) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/api/" + getOfferSuffix());
        builder.queryParam("page", pageRequest.getPageNumber());
        builder.queryParam("size", pageRequest.getPageSize());
        pageRequest.getSort().forEach(sort -> {
            builder.queryParam("sort", "%s,%s".formatted(sort.getProperty(), sort.getDirection()));
        });
        String url = builder.encode().toUriString();

        return listOffers(URI.create(url));
    }
}
