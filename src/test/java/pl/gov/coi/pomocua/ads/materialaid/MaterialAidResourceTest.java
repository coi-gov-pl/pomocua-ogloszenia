package pl.gov.coi.pomocua.ads.materialaid;

import lombok.Builder;
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
import pl.gov.coi.pomocua.ads.Offers;
import pl.gov.coi.pomocua.ads.UserId;
import pl.gov.coi.pomocua.ads.transport.TransportTestDataGenerator;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class MaterialAidResourceTest extends BaseResourceTest<MaterialAidOffer> {

    @Autowired
    private MaterialAidOfferRepository repository;

    private static MaterialAidOfferBuilder aMaterialAidOffer() {
        return new MaterialAidOfferBuilder();
    }

    @Override
    protected Class<MaterialAidOffer> getClazz() {
        return MaterialAidOffer.class;
    }

    @Override
    protected String getOfferSuffix() {
        return "material-aid";
    }

    @Override
    protected CrudRepository<MaterialAidOffer, Long> getRepository() {
        return repository;
    }

    @Nested
    class Searching {
        @Test
        void shouldSearchByLocation() {
            MaterialAidOffer offer1 = postOffer(aMaterialAidOffer()
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

            MaterialAidOffer offer2 = postOffer(aMaterialAidOffer()
                    .location(new Location("Pomorskie", "Gdańsk"))
                    .category(MaterialAidCategory.FOOD)
                    .build());

            MaterialAidOffer offer3 = postOffer(aMaterialAidOffer()
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
                    .extracting(r -> r.title)
                    .containsExactly("c", "d");
        }

        @Test
        void shouldIgnoreDeactivatedOffer() {
            MaterialAidOffer offer = postOffer(aMaterialAidOffer().build());
            deleteOffer(offer.id);

            PageRequest page = PageRequest.of(0, 10);
            var results = searchOffers(page);

            assertThat(results.totalElements).isEqualTo(0);
            assertThat(results.content).isEmpty();
        }
    }

    @Nested
    class ValidationTest {
        @Test
        void shouldRejectNullCategory() {
            MaterialAidOffer offer = sampleOfferRequest();
            offer.category = null;
            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }

        @Test
        void shouldRejectNullLocation() {
            MaterialAidOffer offer = sampleOfferRequest();
            offer.location = null;
            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }
    }

    @Nested
    class Updating {
        @Test
        void shouldUpdateOffer() {
            MaterialAidOffer offer = postSampleOffer();
            var updateJson = MaterialAidTestDataGenerator.sampleUpdateJson();

            ResponseEntity<Void> response = updateOffer(offer.id, updateJson);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
            MaterialAidOffer updatedOffer = getOfferFromRepository(offer.id);
            assertThat(updatedOffer.title).isEqualTo("new title");
            assertThat(updatedOffer.description).isEqualTo("new description");
            assertThat(updatedOffer.location.region).isEqualTo("Pomorskie");
            assertThat(updatedOffer.location.city).isEqualTo("Gdańsk");
            assertThat(updatedOffer.category).isEqualTo(MaterialAidCategory.FOR_CHILDREN);
        }

        @Test
        public void shouldUpdateModifiedDate() {
            testTimeProvider.setCurrentTime(Instant.parse("2022-03-07T15:23:22Z"));
            MaterialAidOffer offer = postSampleOffer();
            var updateJson = MaterialAidTestDataGenerator.sampleUpdateJson();

            testTimeProvider.setCurrentTime(Instant.parse("2022-04-01T12:00:00Z"));
            ResponseEntity<Void> response = updateOffer(offer.id, updateJson);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
            MaterialAidOffer updatedOffer = getOfferFromRepository(offer.id);
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
            MaterialAidOffer offer = postSampleOffer();
            deleteOffer(offer.id);
            var updateJson = MaterialAidTestDataGenerator.sampleUpdateJson();

            ResponseEntity<Void> response = updateOffer(offer.id, updateJson);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        void shouldReturn404WhenOfferDoesNotBelongToCurrentUser() {
            testUser.setCurrentUserWithId(new UserId("other-user-2"));
            MaterialAidOffer offer = postSampleOffer();
            var updateJson = MaterialAidTestDataGenerator.sampleUpdateJson();

            testUser.setCurrentUserWithId(new UserId("current-user-1"));
            ResponseEntity<Void> response = updateOffer(offer.id, updateJson);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Nested
        class Validation {
            @ParameterizedTest
            @NullAndEmptySource
            @ValueSource(strings = {"<", ">", "(", ")", "%", "#", "@", "\"", "'"})
            void shouldRejectMissingOrInvalidTitle(String title) {
                MaterialAidOffer offer = postSampleOffer();
                var updateJson = MaterialAidTestDataGenerator.sampleUpdateJson();
                updateJson.title = title;

                ResponseEntity<Void> response = updateOffer(offer.id, updateJson);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                MaterialAidOffer notUpdatedOffer = getOfferFromRepository(offer.id);
                assertThat(notUpdatedOffer.title).isEqualTo(offer.title);
            }

            @Test
            void shouldRejectTooLongTitle() {
                MaterialAidOffer offer = postSampleOffer();
                var updateJson = MaterialAidTestDataGenerator.sampleUpdateJson();
                updateJson.title = "a".repeat(81);

                ResponseEntity<Void> response = updateOffer(offer.id, updateJson);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                MaterialAidOffer notUpdatedOffer = getOfferFromRepository(offer.id);
                assertThat(notUpdatedOffer.title).isEqualTo(offer.title);
            }

            @ParameterizedTest
            @NullAndEmptySource
            @ValueSource(strings = {"<", ">", "(", ")", "%", "#", "@", "\"", "'"})
            void shouldRejectMissingOrInvalidDescription(String description) {
                MaterialAidOffer offer = postSampleOffer();
                var updateJson = MaterialAidTestDataGenerator.sampleUpdateJson();
                updateJson.description = description;

                ResponseEntity<Void> response = updateOffer(offer.id, updateJson);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                MaterialAidOffer notUpdatedOffer = getOfferFromRepository(offer.id);
                assertThat(notUpdatedOffer.description).isEqualTo(offer.description);
            }

            @Test
            void shouldRejectTooLongDescription() {
                MaterialAidOffer offer = postSampleOffer();
                var updateJson = MaterialAidTestDataGenerator.sampleUpdateJson();
                updateJson.description = "a".repeat(2001);

                ResponseEntity<Void> response = updateOffer(offer.id, updateJson);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                MaterialAidOffer notUpdatedOffer = getOfferFromRepository(offer.id);
                assertThat(notUpdatedOffer.description).isEqualTo(offer.description);
            }

            @ParameterizedTest
            @NullSource
            @MethodSource("pl.gov.coi.pomocua.ads.materialaid.MaterialAidResourceTest#invalidLocations")
            void shouldRejectMissingOrInvalidLocation(Location location) {
                MaterialAidOffer offer = postSampleOffer();
                var updateJson = MaterialAidTestDataGenerator.sampleUpdateJson();
                updateJson.location = location;

                ResponseEntity<Void> response = updateOffer(offer.id, updateJson);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                MaterialAidOffer notUpdatedOffer = getOfferFromRepository(offer.id);
                assertThat(notUpdatedOffer.location).isEqualTo(offer.location);
            }

            @Test
            void shouldRejectMissingCategory() {
                MaterialAidOffer offer = postSampleOffer();
                var updateJson = MaterialAidTestDataGenerator.sampleUpdateJson();
                updateJson.category = null;

                ResponseEntity<Void> response = updateOffer(offer.id, updateJson);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                MaterialAidOffer notUpdatedOffer = getOfferFromRepository(offer.id);
                assertThat(notUpdatedOffer.category).isEqualTo(offer.category);
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

    @Override
    protected MaterialAidOffer sampleOfferRequest() {
        MaterialAidOffer request = new MaterialAidOffer();
        request.description = "some description";
        request.title = "some title";
        request.category = MaterialAidCategory.CLOTHING;
        request.location = new Location("mazowieckie", "warszawa");
        request.phoneNumber = "481234567890";
        return request;
    }

    private List<MaterialAidOffer> searchOffers(MaterialAidOfferSearchCriteria searchCriteria) {
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

    private Offers<MaterialAidOffer> searchOffers(PageRequest pageRequest) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/api/" + getOfferSuffix());
        builder.queryParam("page", pageRequest.getPageNumber());
        builder.queryParam("size", pageRequest.getPageSize());
        pageRequest.getSort().forEach(sort -> {
            builder.queryParam("sort", "%s,%s".formatted(sort.getProperty(), sort.getDirection()));
        });
        String url = builder.encode().toUriString();

        return listOffers(URI.create(url));
    }

    @Builder
    private static MaterialAidOffer materialAidOfferBuilder(
            String title,
            String description,
            MaterialAidCategory category,
            Location location,
            String phoneNumber
    ) {
        MaterialAidOffer offer = new MaterialAidOffer();
        offer.title = Optional.ofNullable(title).orElse("some title");
        offer.description = Optional.ofNullable(description).orElse("some description");
        offer.category = Optional.ofNullable(category).orElse(MaterialAidCategory.FOOD);
        offer.location = Optional.ofNullable(location).orElse(new Location("mazowieckie", "warszawa"));
        offer.phoneNumber = Optional.ofNullable(phoneNumber).orElse("481234567890");
        return offer;
    }
}
