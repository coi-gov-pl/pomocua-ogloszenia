package pl.gov.coi.pomocua.ads.law;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;
import pl.gov.coi.pomocua.ads.BaseResourceTest;
import pl.gov.coi.pomocua.ads.Language;
import pl.gov.coi.pomocua.ads.Location;
import pl.gov.coi.pomocua.ads.OffersVM;
import pl.gov.coi.pomocua.ads.law.LawOffer.HelpKind;
import pl.gov.coi.pomocua.ads.law.LawOffer.HelpMode;

import java.net.URI;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.gov.coi.pomocua.ads.law.LawTestDataGenerator.aLawOffer;

public class LawOfferResourceTest extends BaseResourceTest<LawOffer, LawOfferVM> {

    @Autowired
    private LawOfferRepository repository;


    @Override
    protected Class<LawOfferVM> getClazz() {
        return LawOfferVM.class;
    }

    @Override
    protected String getOfferSuffix() {
        return "law";
    }

    @Override
    protected CrudRepository<LawOffer, Long> getRepository() {
        return repository;
    }

    @Override
    protected LawOfferVM sampleOfferRequest() {
        return aLawOffer().build();
    }

    @Nested
    class Searching {
        @Test
        void shouldSearchByLocationWithIgnoreCase() {
            LawOfferVM offer1 = postOffer(aLawOffer()
                    .location(new Location("Mazowieckie", "Warszawa"))
                    .build());

            postOffer(aLawOffer()
                    .location(new Location("Pomorskie", "Gdańsk"))
                    .build());

            LawOfferSearchCriteria searchCriteria = new LawOfferSearchCriteria();
            searchCriteria.setLocation(new Location("Mazowieckie", "WARSZAWA"));
            var results = searchOffers(searchCriteria);

            assertThat(results).hasSize(1).contains(offer1);
        }

        @Test
        void shouldSearchByLocationWithNullLocation() {
            LawOfferVM offer1 = postOffer(aLawOffer()
                    .location(null)
                    .build());
            LawOfferVM offer2 = postOffer(aLawOffer()
                    .location(new Location("Mazowieckie", "Warszawa"))
                    .build());
            postOffer(aLawOffer()
                    .location(new Location("Pomorskie", "Gdańsk"))
                    .build());

            LawOfferSearchCriteria searchCriteria = new LawOfferSearchCriteria();
            searchCriteria.setLocation(new Location("Mazowieckie", "WARSZAWA"));
            var results = searchOffers(searchCriteria);

            assertThat(results).hasSize(2).contains(offer1, offer2);
        }

        @Test
        void shouldSearchByHelpMode() {
            LawOfferVM offer = postOffer(aLawOffer()
                    .helpMode(List.of(HelpMode.BY_PHONE))
                    .build());

            LawOfferVM offer2 = postOffer(aLawOffer()
                    .helpMode(List.of(HelpMode.BY_PHONE, HelpMode.BY_EMAIL))
                    .build());

            postOffer(aLawOffer()
                    .helpMode(List.of(HelpMode.ONLINE))
                    .build());

            LawOfferSearchCriteria searchCriteria = new LawOfferSearchCriteria();
            searchCriteria.setHelpMode(List.of(HelpMode.BY_PHONE));
            var results = searchOffers(searchCriteria);

            assertThat(results).hasSize(2).contains(offer, offer2);
        }

        @Test
        void shouldSearchByHelpKind() {
            LawOfferVM offer = postOffer(aLawOffer()
                    .helpKind(List.of(HelpKind.IMMIGRATION_LAW, HelpKind.FAMILY_LAW))
                    .build());

            LawOfferVM offer2 = postOffer(aLawOffer()
                    .helpKind(List.of(HelpKind.IMMIGRATION_LAW))
                    .build());

            postOffer(aLawOffer()
                    .helpKind(List.of(HelpKind.TAX_LAW))
                    .build());

            LawOfferSearchCriteria searchCriteria = new LawOfferSearchCriteria();
            searchCriteria.setHelpKind(HelpKind.IMMIGRATION_LAW);
            var results = searchOffers(searchCriteria);

            assertThat(results).hasSize(2).contains(offer, offer2);
        }

        @Test
        void shouldSearchByLanguage() {
            LawOfferVM offer = postOffer(aLawOffer()
                    .language(List.of(Language.PL, Language.UA))
                    .build());

            LawOfferVM offer2 = postOffer(aLawOffer()
                    .language(List.of(Language.EN))
                    .build());

            postOffer(aLawOffer()
                    .language(List.of(Language.UA, Language.RU))
                    .build());

            LawOfferSearchCriteria searchCriteria = new LawOfferSearchCriteria();
            searchCriteria.setLanguage(List.of(Language.PL, Language.EN));
            var results = searchOffers(searchCriteria);

            assertThat(results).hasSize(2).contains(offer, offer2);
        }

        @Test
        void shouldReturnPageOfData() {
            postOffer(aLawOffer().title("a").build());
            postOffer(aLawOffer().title("b").build());
            postOffer(aLawOffer().title("c").build());
            postOffer(aLawOffer().title("d").build());
            postOffer(aLawOffer().title("e").build());
            postOffer(aLawOffer().title("f").build());

            PageRequest page = PageRequest.of(1, 2);
            var results = searchOffers(page);

            assertThat(results.totalElements).isEqualTo(6);
            assertThat(results.content).hasSize(2).extracting(r -> r.getTitle()).containsExactly("c", "d");
        }

        @Test
        void shouldIgnoreDeactivatedOffer() {
            LawOfferVM offer = postOffer(aLawOffer().build());
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
            LawOfferVM offer = sampleOfferRequest();
            offer.setLocation(null);
            assertPostResponseStatus(offer, HttpStatus.CREATED);
        }

        @Test
        void shouldRejectEmptyTitle() {
            LawOfferVM offer = sampleOfferRequest();
            offer.setTitle("");
            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }

        @Test
        void shouldRejectEmptyDescription() {
            LawOfferVM offer = sampleOfferRequest();
            offer.setDescription("");
            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }

        @Test
        void shouldRejectEmptyHelpMode() {
            LawOfferVM offer = sampleOfferRequest();
            offer.setHelpMode(Collections.emptyList());
            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }

        @Test
        void shouldRejectNullHelpMode() {
            LawOfferVM offer = sampleOfferRequest();
            offer.setHelpMode(null);
            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }

        @Test
        void shouldRejectEmptyHelpKind() {
            LawOfferVM offer = sampleOfferRequest();
            offer.setHelpKind(Collections.emptyList());
            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }

        @Test
        void shouldRejectNullHelpKind() {
            LawOfferVM offer = sampleOfferRequest();
            offer.setHelpKind(null);
            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }

        @Test
        void shouldRejectNullLanguage() {
            LawOfferVM offer = sampleOfferRequest();
            offer.setLanguage(null);
            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }

        @Test
        void shouldRejectEmptyLanguage() {
            LawOfferVM offer = sampleOfferRequest();
            offer.setLanguage(Collections.emptyList());
            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }
    }

    @Nested
    class Updating {
        @Test
        void shouldUpdateOffer() {
            LawOfferVM offer = postSampleOffer();
            var updateJson = LawTestDataGenerator.sampleUpdateJson();

            ResponseEntity<Void> response = updateOffer(offer.getId(), updateJson);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
            LawOffer updatedOffer = getOfferFromRepository(offer.getId());
            assertThat(updatedOffer.title).isEqualTo("new title");
            assertThat(updatedOffer.description).isEqualTo("new description");
            assertThat(updatedOffer.location.region).isEqualTo("Pomorskie");
            assertThat(updatedOffer.location.city).isEqualTo("Gdańsk");
            assertThat(updatedOffer.getHelpMode()).isEqualTo(List.of(HelpMode.ONLINE));
            assertThat(updatedOffer.getHelpKind()).isEqualTo(List.of(HelpKind.IMMIGRATION_LAW, HelpKind.FAMILY_LAW));
            assertThat(updatedOffer.getLanguage()).isEqualTo(List.of(Language.PL, Language.EN));
        }
    }

    private List<LawOfferVM> searchOffers(LawOfferSearchCriteria searchCriteria) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/api/" + getOfferSuffix());
        if (searchCriteria.getLocation() != null) {
            builder
                    .queryParam("location.city", searchCriteria.getLocation().getCity())
                    .queryParam("location.region", searchCriteria.getLocation().getRegion());
        }
        if (!CollectionUtils.isEmpty(searchCriteria.getHelpMode())) {
            builder.queryParam("helpMode", searchCriteria.getHelpMode());
        }
        if (searchCriteria.getHelpKind() != null) {
            builder.queryParam("helpKind", searchCriteria.getHelpKind());
        }
        if (!CollectionUtils.isEmpty(searchCriteria.getLanguage())) {
            builder.queryParam("language", searchCriteria.getLanguage());
        }
        String url = builder.encode().toUriString();
        return listOffers(URI.create(url)).content;
    }

    private OffersVM<LawOfferVM> searchOffers(PageRequest pageRequest) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/api/" + getOfferSuffix());
        builder.queryParam("page", pageRequest.getPageNumber());
        builder.queryParam("size", pageRequest.getPageSize());
        pageRequest.getSort().forEach(sort ->
                builder.queryParam("sort", "%s,%s".formatted(sort.getProperty(), sort.getDirection())));
        String url = builder.encode().toUriString();
        return listOffers(URI.create(url));
    }
}
