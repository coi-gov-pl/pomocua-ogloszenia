package pl.gov.coi.pomocua.ads.other;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;
import pl.gov.coi.pomocua.ads.BaseResourceTest;
import pl.gov.coi.pomocua.ads.OffersVM;
import pl.gov.coi.pomocua.ads.translation.TranslationOfferVM;

import java.net.URI;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.gov.coi.pomocua.ads.other.OtherTestDataGenerator.aOtherOffer;
import static pl.gov.coi.pomocua.ads.translation.TranslationTestDataGenerator.aTranslationOffer;

public class OtherOfferResourceTest extends BaseResourceTest<OtherOffer, OtherOfferVM> {

    @Autowired
    private OtherOfferRepository repository;

    @Override
    protected Class<OtherOfferVM> getClazz() {
        return OtherOfferVM.class;
    }

    @Override
    protected String getOfferSuffix() {
        return "other";
    }

    @Override
    protected OtherOfferVM sampleOfferRequest() {
        return aOtherOffer().build();
    }

    @Override
    protected CrudRepository<OtherOffer, Long> getRepository() {
        return repository;
    }

    @Nested
    class Searching {
        @Test
        void shouldSearchBySearchTextWithIgnoreCaseTitle() {
            OtherOfferVM offer1 = postOffer(aOtherOffer()
                    .title("Opieka nad dziećmi")
                    .description("Udzielę pomocy w opiece nad dziećmi")
                    .build());

            postOffer(aOtherOffer().title("jakiś tytuł").description("jakiś opis").build());

            OtherOfferSearchCriteria searchCriteria = new OtherOfferSearchCriteria();
            searchCriteria.setSearchText("opieka");
            var results = searchOffers(searchCriteria);

            assertThat(results).hasSize(1).contains(offer1);
        }

        @Test
        void shouldSearchBySearchTextWithIgnoreCaseDescription() {
            OtherOfferVM offer1 = postOffer(aOtherOffer()
                    .title("Opieka nad dziećmi")
                    .description("Udzielę pomocy w opiece nad dziećmi")
                    .build());

            postOffer(aOtherOffer().title("jakiś tytuł").description("jakiś opis").build());

            OtherOfferSearchCriteria searchCriteria = new OtherOfferSearchCriteria();
            searchCriteria.setSearchText("udzielę");
            var results = searchOffers(searchCriteria);

            assertThat(results).hasSize(1).contains(offer1);
        }

        @Test
        void shouldReturnPageOfData() {
            postOffer(aOtherOffer().title("a").build());
            postOffer(aOtherOffer().title("b").build());
            postOffer(aOtherOffer().title("c").build());
            postOffer(aOtherOffer().title("d").build());
            postOffer(aOtherOffer().title("e").build());
            postOffer(aOtherOffer().title("f").build());

            PageRequest page = PageRequest.of(1, 2);
            var results = searchOffers(page);

            assertThat(results.totalElements).isEqualTo(6);
            assertThat(results.content).hasSize(2).extracting(r -> r.getTitle()).containsExactly("c", "d");
        }

        @Test
        void shouldIgnoreDeactivatedOffer() {
            OtherOfferVM offer = postOffer(aOtherOffer().build());
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
            OtherOfferVM offer = sampleOfferRequest();
            offer.setLocation(null);
            assertPostResponseStatus(offer, HttpStatus.CREATED);
        }
    }

    @Nested
    class Updating {
        @Test
        void shouldUpdateOffer() {
            OtherOfferVM offer = postSampleOffer();
            var updateJson = OtherTestDataGenerator.sampleUpdateJson();

            ResponseEntity<Void> response = updateOffer(offer.getId(), updateJson);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
            OtherOffer updatedOffer = getOfferFromRepository(offer.getId());
            assertThat(updatedOffer.title).isEqualTo("new title");
            assertThat(updatedOffer.description).isEqualTo("new description");
            assertThat(updatedOffer.location.region).isEqualTo("Pomorskie");
            assertThat(updatedOffer.location.city).isEqualTo("Gdańsk");
        }
    }

    private List<OtherOfferVM> searchOffers(OtherOfferSearchCriteria searchCriteria) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/api/" + getOfferSuffix());
        if (StringUtils.hasText(searchCriteria.getSearchText())) {
            builder
                    .queryParam("searchText", searchCriteria.getSearchText());
        }
        if (searchCriteria.getLocation() != null) {
            builder
                    .queryParam("location.city", searchCriteria.getLocation().getCity())
                    .queryParam("location.region", searchCriteria.getLocation().getRegion());
        }
        String url = builder.encode().toUriString();
        return listOffers(URI.create(url)).content;
    }

    private OffersVM<OtherOfferVM> searchOffers(PageRequest pageRequest) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/api/" + getOfferSuffix());
        builder.queryParam("page", pageRequest.getPageNumber());
        builder.queryParam("size", pageRequest.getPageSize());
        pageRequest.getSort().forEach(sort ->
                builder.queryParam("sort", "%s,%s".formatted(sort.getProperty(), sort.getDirection())));
        String url = builder.encode().toUriString();
        return listOffers(URI.create(url));
    }
}
