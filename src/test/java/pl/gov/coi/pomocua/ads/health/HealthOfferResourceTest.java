package pl.gov.coi.pomocua.ads.health;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.gov.coi.pomocua.ads.health.HealthTestDataGenerator.aHealthOffer;

import pl.gov.coi.pomocua.ads.BaseResourceTest;
import pl.gov.coi.pomocua.ads.Language;
import pl.gov.coi.pomocua.ads.Location;
import pl.gov.coi.pomocua.ads.OffersVM;
import pl.gov.coi.pomocua.ads.health.HealthOffer.HealthCareMode;

public class HealthOfferResourceTest extends BaseResourceTest<HealthOffer, HealthOfferVM> {

    @Autowired
    private HealthOfferRepository repository;

    @Override
    protected Class<HealthOfferVM> getClazz() {
        return HealthOfferVM.class;
    }

    @Override
    protected String getOfferSuffix() {
        return "health-care";
    }

    @Override
    protected HealthOfferVM sampleOfferRequest() {
        return aHealthOffer().build();
    }

    @Override
    protected CrudRepository<HealthOffer, Long> getRepository() {
        return repository;
    }

    @Nested
    class Searching {
        @Test
        void shouldSearchByLocationWithIgnoreCase() {
            HealthOfferVM offer1 = postOffer(aHealthOffer()
                    .location(new Location("Mazowieckie", "Warszawa"))
                    .build());

            postOffer(aHealthOffer()
                    .location(new Location("Pomorskie", "Gdańsk"))
                    .build());

            HealthOfferSearchCriteria searchCriteria = new HealthOfferSearchCriteria();
            searchCriteria.setLocation(new Location("Mazowieckie", "WARSZAWA"));
            var results = searchOffers(searchCriteria);

            assertThat(results).hasSize(1).contains(offer1);
        }

        @Test
        void shouldSearchByLocationWithNullLocation() {
            HealthOfferVM offer1 = postOffer(aHealthOffer()
                    .location(null)
                    .build());
            HealthOfferVM offer2 = postOffer(aHealthOffer()
                    .location(new Location("Mazowieckie", "Warszawa"))
                    .build());
            postOffer(aHealthOffer()
                    .location(new Location("Pomorskie", "Gdańsk"))
                    .build());

            HealthOfferSearchCriteria searchCriteria = new HealthOfferSearchCriteria();
            searchCriteria.setLocation(new Location("Mazowieckie", "WARSZAWA"));
            var results = searchOffers(searchCriteria);

            assertThat(results).hasSize(2).contains(offer1, offer2);
        }

        @Test
        void shouldSearchByMode() {
            postOffer(aHealthOffer()
                    .mode(List.of(HealthCareMode.IN_FACILITY))
                    .build());

            HealthOfferVM offer = postOffer(aHealthOffer()
                    .mode(List.of(HealthCareMode.IN_FACILITY, HealthCareMode.BY_PHONE))
                    .build());

            HealthOfferVM offer2 = postOffer(aHealthOffer()
                    .mode(List.of(HealthCareMode.ONLINE, HealthCareMode.BY_PHONE))
                    .build());

            HealthOfferSearchCriteria searchCriteria = new HealthOfferSearchCriteria();
            searchCriteria.setMode(List.of(HealthCareMode.ONLINE, HealthCareMode.BY_PHONE));
            var results = searchOffers(searchCriteria);

            assertThat(results).hasSize(2).contains(offer, offer2);
        }

        @Test
        void shouldSearchBySpecialization() {
            postOffer(aHealthOffer()
                    .specialization(HealthCareSpecialization.PEDIATRICS)
                    .build());

            HealthOfferVM offer = postOffer(aHealthOffer()
                    .specialization(HealthCareSpecialization.GENERAL)
                    .build());

            postOffer(aHealthOffer()
                    .specialization(HealthCareSpecialization.GYNECOLOGY)
                    .build());

            HealthOfferSearchCriteria searchCriteria = new HealthOfferSearchCriteria();
            searchCriteria.setSpecialization(HealthCareSpecialization.GENERAL);
            var results = searchOffers(searchCriteria);

            assertThat(results).hasSize(1).contains(offer);
        }

        @Test
        void shouldSearchByLanguage() {
            HealthOfferVM offer = postOffer(aHealthOffer()
                    .language(List.of(Language.PL, Language.UA))
                    .build());

            HealthOfferVM offer2 = postOffer(aHealthOffer()
                    .language(List.of(Language.EN))
                    .build());

            postOffer(aHealthOffer()
                    .language(List.of(Language.UA, Language.RU))
                    .build());

            HealthOfferSearchCriteria searchCriteria = new HealthOfferSearchCriteria();
            searchCriteria.setLanguage(List.of(Language.PL, Language.EN));
            var results = searchOffers(searchCriteria);

            assertThat(results).hasSize(2).contains(offer, offer2);
        }

        @Test
        void shouldReturnPageOfData() {
            postOffer(aHealthOffer().title("a").build());
            postOffer(aHealthOffer().title("b").build());
            postOffer(aHealthOffer().title("c").build());
            postOffer(aHealthOffer().title("d").build());
            postOffer(aHealthOffer().title("e").build());
            postOffer(aHealthOffer().title("f").build());

            PageRequest page = PageRequest.of(1, 2);
            var results = searchOffers(page);

            assertThat(results.totalElements).isEqualTo(6);
            assertThat(results.content).hasSize(2).extracting(r -> r.getTitle()).containsExactly("c", "d");
        }

        @Test
        void shouldIgnoreDeactivatedOffer() {
            HealthOfferVM offer = postOffer(aHealthOffer().build());
            deleteOffer(offer.getId());

            PageRequest page = PageRequest.of(0, 10);
            var results = searchOffers(page);

            assertThat(results.totalElements).isEqualTo(0);
            assertThat(results.content).isEmpty();
        }
    }

    @Nested
    class ValidationTest {
        @Test
        void shouldAcceptNullLocation() {
            HealthOfferVM offer = sampleOfferRequest();
            offer.setLocation(null);
            assertPostResponseStatus(offer, HttpStatus.CREATED);
        }

        @Test
        void shouldRejectNullMode() {
            HealthOfferVM offer = sampleOfferRequest();
            offer.setMode(null);
            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }

        @Test
        void shouldRejectEmptyMode() {
            HealthOfferVM offer = sampleOfferRequest();
            offer.setMode(Collections.emptyList());
            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }

        @Test
        void shouldRejectNullSpecialization() {
            HealthOfferVM offer = sampleOfferRequest();
            offer.setSpecialization(null);
            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }

        @Test
        void shouldRejectNullLanguage() {
            HealthOfferVM offer = sampleOfferRequest();
            offer.setLanguage(null);
            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }

        @Test
        void shouldRejectEmptyLanguage() {
            HealthOfferVM offer = sampleOfferRequest();
            offer.setLanguage(Collections.emptyList());
            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }
    }

    @Nested
    class Updating {
        @Test
        void shouldUpdateOffer() {
            HealthOfferVM offer = postSampleOffer();
            var updateJson = HealthTestDataGenerator.sampleUpdateJson();

            ResponseEntity<Void> response = updateOffer(offer.getId(), updateJson);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
            HealthOffer updatedOffer = getOfferFromRepository(offer.getId());
            assertThat(updatedOffer.title).isEqualTo("new title");
            assertThat(updatedOffer.description).isEqualTo("new description");
            assertThat(updatedOffer.getMode()).isEqualTo(List.of(HealthCareMode.ONLINE));
            assertThat(updatedOffer.location.region).isEqualTo("Pomorskie");
            assertThat(updatedOffer.location.city).isEqualTo("Gdańsk");
            assertThat(updatedOffer.specialization).isEqualTo(HealthCareSpecialization.PEDIATRICS);
            assertThat(updatedOffer.getLanguage()).isEqualTo(List.of(Language.PL, Language.EN));
        }
    }

    private List<HealthOfferVM> searchOffers(HealthOfferSearchCriteria searchCriteria) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/api/" + getOfferSuffix());
        if (searchCriteria.getLocation() != null) {
            builder
                    .queryParam("location.city", searchCriteria.getLocation().getCity())
                    .queryParam("location.region", searchCriteria.getLocation().getRegion());
        }
        if (searchCriteria.getSpecialization() != null) {
            builder.queryParam("specialization", searchCriteria.getSpecialization());
        }
        if (!CollectionUtils.isEmpty(searchCriteria.getMode())) {
            builder.queryParam("mode", searchCriteria.getMode());
        }
        if (!CollectionUtils.isEmpty(searchCriteria.getLanguage())) {
            builder.queryParam("language", searchCriteria.getLanguage());
        }
        String url = builder.encode().toUriString();
        return listOffers(URI.create(url)).content;
    }

    private OffersVM<HealthOfferVM> searchOffers(PageRequest pageRequest) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/api/" + getOfferSuffix());
        builder.queryParam("page", pageRequest.getPageNumber());
        builder.queryParam("size", pageRequest.getPageSize());
        pageRequest.getSort().forEach(sort ->
                builder.queryParam("sort", "%s,%s".formatted(sort.getProperty(), sort.getDirection())));
        String url = builder.encode().toUriString();
        return listOffers(URI.create(url));
    }
}
