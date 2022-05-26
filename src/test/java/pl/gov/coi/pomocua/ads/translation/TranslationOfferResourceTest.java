package pl.gov.coi.pomocua.ads.translation;

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

import pl.gov.coi.pomocua.ads.BaseResourceTest;
import pl.gov.coi.pomocua.ads.Language;
import pl.gov.coi.pomocua.ads.Location;
import pl.gov.coi.pomocua.ads.OffersVM;
import pl.gov.coi.pomocua.ads.translation.TranslationOffer.TranslationMode;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.gov.coi.pomocua.ads.translation.TranslationTestDataGenerator.aTranslationOffer;

public class TranslationOfferResourceTest extends BaseResourceTest<TranslationOffer, TranslationOfferVM> {

    @Autowired
    private TranslationOfferRepository repository;

    @Override
    protected Class<TranslationOfferVM> getClazz() {
        return TranslationOfferVM.class;
    }

    @Override
    protected String getOfferSuffix() {
        return "translation";
    }

    @Override
    protected TranslationOfferVM sampleOfferRequest() {
        return aTranslationOffer().build();
    }

    @Override
    protected CrudRepository<TranslationOffer, Long> getRepository() {
        return repository;
    }

    @Nested
    class Searching {
        @Test
        void shouldSearchByLocationWithIgnoreCase() {
            TranslationOfferVM offer1 = postOffer(aTranslationOffer()
                    .location(new Location("Mazowieckie", "Warszawa"))
                    .build());

            postOffer(aTranslationOffer()
                    .location(new Location("Pomorskie", "Gdańsk"))
                    .build());

            TranslationOfferSearchCriteria searchCriteria = new TranslationOfferSearchCriteria();
            searchCriteria.setLocation(new Location("Mazowieckie", "WARSZAWA"));
            var results = searchOffers(searchCriteria);

            assertThat(results).hasSize(1).contains(offer1);
        }

        @Test
        void shouldSearchByLocationWithNullLocation() {
            TranslationOfferVM offer1 = postOffer(aTranslationOffer()
                    .location(null)
                    .build());

            TranslationOfferVM offer2 = postOffer(aTranslationOffer()
                    .location(new Location("Mazowieckie", "Warszawa"))
                    .build());

            postOffer(aTranslationOffer()
                    .location(new Location("Pomorskie", "Gdańsk"))
                    .build());

            TranslationOfferSearchCriteria searchCriteria = new TranslationOfferSearchCriteria();
            searchCriteria.setLocation(new Location("Mazowieckie", "WARSZAWA"));
            var results = searchOffers(searchCriteria);

            assertThat(results).hasSize(2).contains(offer1, offer2);
        }

        @Test
        void shouldSearchByMode() {
            postOffer(aTranslationOffer()
                    .mode(List.of(TranslationMode.ONLINE))
                    .build());

            TranslationOfferVM offer = postOffer(aTranslationOffer()
                    .mode(List.of(TranslationMode.ONLINE, TranslationMode.BY_EMAIL))
                    .build());

            TranslationOfferVM offer2 = postOffer(aTranslationOffer()
                    .mode(List.of(TranslationMode.STATIONARY, TranslationMode.BY_EMAIL))
                    .build());

            TranslationOfferSearchCriteria searchCriteria = new TranslationOfferSearchCriteria();
            searchCriteria.setMode(List.of(TranslationMode.STATIONARY, TranslationMode.BY_EMAIL));
            var results = searchOffers(searchCriteria);

            assertThat(results).hasSize(2).contains(offer, offer2);
        }

        @Test
        void shouldSearchByLanguage() {
            TranslationOfferVM offer = postOffer(aTranslationOffer()
                    .language(List.of(Language.PL, Language.UA))
                    .build());

            TranslationOfferVM offer2 = postOffer(aTranslationOffer()
                    .language(List.of(Language.EN))
                    .build());

            postOffer(aTranslationOffer()
                    .language(List.of(Language.RU, Language.UA))
                    .build());

            TranslationOfferSearchCriteria searchCriteria = new TranslationOfferSearchCriteria();
            searchCriteria.setLanguage(List.of(Language.PL, Language.EN));
            var results = searchOffers(searchCriteria);

            assertThat(results).hasSize(2).contains(offer, offer2);
        }

        @Test
        void shouldReturnPageOfData() {
            postOffer(aTranslationOffer().title("a").build());
            postOffer(aTranslationOffer().title("b").build());
            postOffer(aTranslationOffer().title("c").build());
            postOffer(aTranslationOffer().title("d").build());
            postOffer(aTranslationOffer().title("e").build());
            postOffer(aTranslationOffer().title("f").build());

            PageRequest page = PageRequest.of(1, 2);
            var results = searchOffers(page);

            assertThat(results.totalElements).isEqualTo(6);
            assertThat(results.content).hasSize(2).extracting(r -> r.getTitle()).containsExactly("c", "d");
        }

        @Test
        void shouldIgnoreDeactivatedOffer() {
            TranslationOfferVM offer = postOffer(aTranslationOffer().build());
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
            TranslationOfferVM offer = sampleOfferRequest();
            offer.setLocation(null);
            assertPostResponseStatus(offer, HttpStatus.CREATED);
        }

        @Test
        void shouldRejectNullMode() {
            TranslationOfferVM offer = sampleOfferRequest();
            offer.setMode(null);
            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }

        @Test
        void shouldRejestEmptyMode() {
            TranslationOfferVM offer = sampleOfferRequest();
            offer.setMode(Collections.emptyList());
            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }

        @Test
        void shouldRejectNullLanguage() {
            TranslationOfferVM offer = sampleOfferRequest();
            offer.setLanguage(null);
            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }

        @Test
        void shouldRejectEmptyLanguage() {
            TranslationOfferVM offer = sampleOfferRequest();
            offer.setLanguage(Collections.emptyList());
            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }
    }

    @Nested
    class Updating {
        @Test
        void shouldUpdateOffer() {
            TranslationOfferVM offer = postSampleOffer();
            var updateJson = TranslationTestDataGenerator.sampleUpdateJson();

            ResponseEntity<Void> response = updateOffer(offer.getId(), updateJson);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
            TranslationOffer updatedOffer = getOfferFromRepository(offer.getId());
            assertThat(updatedOffer.title).isEqualTo("new title");
            assertThat(updatedOffer.description).isEqualTo("new description");
            assertThat(updatedOffer.getMode()).isEqualTo(List.of(TranslationMode.STATIONARY));
            assertThat(updatedOffer.location.region).isEqualTo("Pomorskie");
            assertThat(updatedOffer.location.city).isEqualTo("Gdańsk");
            assertThat(updatedOffer.getLanguage()).isEqualTo(List.of(Language.RU, Language.PL));
        }
    }

    private List<TranslationOfferVM> searchOffers(TranslationOfferSearchCriteria searchCriteria) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/api/" + getOfferSuffix());
        if (searchCriteria.getLocation() != null) {
            builder
                    .queryParam("location.city", searchCriteria.getLocation().getCity())
                    .queryParam("location.region", searchCriteria.getLocation().getRegion());
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

    private OffersVM<TranslationOfferVM> searchOffers(PageRequest pageRequest) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/api/" + getOfferSuffix());
        builder.queryParam("page", pageRequest.getPageNumber());
        builder.queryParam("size", pageRequest.getPageSize());
        pageRequest.getSort().forEach(sort ->
                builder.queryParam("sort", "%s,%s".formatted(sort.getProperty(), sort.getDirection())));
        String url = builder.encode().toUriString();
        return listOffers(URI.create(url));
    }
}
