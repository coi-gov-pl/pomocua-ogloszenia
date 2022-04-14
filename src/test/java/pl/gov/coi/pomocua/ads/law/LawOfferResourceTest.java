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
import pl.gov.coi.pomocua.ads.Offers;
import pl.gov.coi.pomocua.ads.law.LawOffer.HelpKind;
import pl.gov.coi.pomocua.ads.law.LawOffer.HelpMode;

import java.net.URI;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.gov.coi.pomocua.ads.law.LawTestDataGenerator.aLawOffer;

public class LawOfferResourceTest extends BaseResourceTest<LawOffer> {

    @Autowired
    private LawOfferRepository repository;


    @Override
    protected Class<LawOffer> getClazz() {
        return LawOffer.class;
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
    protected LawOffer sampleOfferRequest() {
        return aLawOffer().build();
    }

    @Nested
    class Searching {
        @Test
        void shouldSearchByLocationWithIgnoreCase() {
            LawOffer offer1 = postOffer(aLawOffer()
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
        void shouldSearchByHelpMode() {
            LawOffer offer = postOffer(aLawOffer()
                    .helpMode(List.of(HelpMode.BY_PHONE))
                    .build());

            LawOffer offer2 = postOffer(aLawOffer()
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
            LawOffer offer = postOffer(aLawOffer()
                    .helpKind(List.of(HelpKind.IMMIGRATION_LAW, HelpKind.FAMILY_LAW))
                    .build());

            LawOffer offer2 = postOffer(aLawOffer()
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
            LawOffer offer = postOffer(aLawOffer()
                    .language(List.of(Language.PL, Language.UA))
                    .build());

            LawOffer offer2 = postOffer(aLawOffer()
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
            assertThat(results.content).hasSize(2).extracting(r -> r.title).containsExactly("c", "d");
        }

        @Test
        void shouldIgnoreDeactivatedOffer() {
            LawOffer offer = postOffer(aLawOffer().build());
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
        void shouldAcceptNullLocation() {
            LawOffer offer = sampleOfferRequest();
            offer.location = null;
            assertPostResponseStatus(offer, HttpStatus.CREATED);
        }

        @Test
        void shouldRejectEmptyTitle() {
            LawOffer offer = sampleOfferRequest();
            offer.title = "";
            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }

        @Test
        void shouldRejectEmptyDescription() {
            LawOffer offer = sampleOfferRequest();
            offer.description = "";
            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }

        @Test
        void shouldRejectEmptyHelpMode() {
            LawOffer offer = sampleOfferRequest();
            offer.setHelpMode(Collections.emptyList());
            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }

        @Test
        void shouldRejectNullHelpMode() {
            LawOffer offer = sampleOfferRequest();
            offer.setHelpMode(null);
            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }

        @Test
        void shouldRejectEmptyHelpKind() {
            LawOffer offer = sampleOfferRequest();
            offer.setHelpKind(Collections.emptyList());
            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }

        @Test
        void shouldRejectNullHelpKind() {
            LawOffer offer = sampleOfferRequest();
            offer.setHelpKind(null);
            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }

        @Test
        void shouldRejectNullLanguage() {
            LawOffer offer = sampleOfferRequest();
            offer.setLanguage(null);
            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }

        @Test
        void shouldRejectEmptyLanguage() {
            LawOffer offer = sampleOfferRequest();
            offer.setLanguage(Collections.emptyList());
            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }
    }

    @Nested
    class Updating {
        @Test
        void shouldUpdateOffer() {
            LawOffer offer = postSampleOffer();
            var updateJson = LawTestDataGenerator.sampleUpdateJson();

            ResponseEntity<Void> response = updateOffer(offer.id, updateJson);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
            LawOffer updatedOffer = getOfferFromRepository(offer.id);
            assertThat(updatedOffer.title).isEqualTo("new title");
            assertThat(updatedOffer.description).isEqualTo("new description");
            assertThat(updatedOffer.location.region).isEqualTo("Pomorskie");
            assertThat(updatedOffer.location.city).isEqualTo("Gdańsk");
            assertThat(updatedOffer.getHelpMode()).isEqualTo(List.of(HelpMode.ONLINE));
            assertThat(updatedOffer.getHelpKind()).isEqualTo(List.of(HelpKind.IMMIGRATION_LAW, HelpKind.FAMILY_LAW));
            assertThat(updatedOffer.getLanguage()).isEqualTo(List.of(Language.PL, Language.EN));
        }
    }

    private List<LawOffer> searchOffers(LawOfferSearchCriteria searchCriteria) {
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

    private Offers<LawOffer> searchOffers(PageRequest pageRequest) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/api/" + getOfferSuffix());
        builder.queryParam("page", pageRequest.getPageNumber());
        builder.queryParam("size", pageRequest.getPageSize());
        pageRequest.getSort().forEach(sort ->
                builder.queryParam("sort", "%s,%s".formatted(sort.getProperty(), sort.getDirection())));
        String url = builder.encode().toUriString();
        return listOffers(URI.create(url));
    }
}
