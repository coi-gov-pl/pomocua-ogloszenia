package pl.gov.coi.pomocua.ads.materialaid;

import lombok.Builder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;
import pl.gov.coi.pomocua.ads.BaseResourceTest;
import pl.gov.coi.pomocua.ads.Location;
import pl.gov.coi.pomocua.ads.PageableResponse;

import java.util.Optional;

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

    @BeforeEach
    public void clearDatabase() {
        repository.deleteAll();
    }

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
        MaterialAidOffer[] results = searchOffers(searchCriteria);

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
        MaterialAidOffer[] results = searchOffers(searchCriteria);

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
        PageableResponse<MaterialAidOffer> results = searchOffers(page);

        assertThat(results.totalElements).isEqualTo(6);
        assertThat(results.content)
                .hasSize(2)
                .extracting(r -> r.title)
                .containsExactly("c", "d");
    }

    @Override
    protected ParameterizedTypeReference<PageableResponse<MaterialAidOffer>> getResponseType() {
        return new ParameterizedTypeReference<>() {
        };
    }

    @Override
    protected MaterialAidOffer sampleOfferRequest() {
        MaterialAidOffer request = new MaterialAidOffer();
        request.description = "some description";
        request.title = "some title";
        request.category = MaterialAidCategory.CLOTHING;
        return request;
    }

    private MaterialAidOffer[] searchOffers(MaterialAidOfferSearchCriteria searchCriteria) {
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

        ResponseEntity<PageableResponse<MaterialAidOffer>> list = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                getResponseType()
        );
        return list.getBody().content;
    }

    private PageableResponse<MaterialAidOffer> searchOffers(PageRequest pageRequest) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/api/" + getOfferSuffix());
        builder.queryParam("page", pageRequest.getPageNumber());
        builder.queryParam("size", pageRequest.getPageSize());
        pageRequest.getSort().forEach(sort -> {
            builder.queryParam("sort", "%s,%s".formatted(sort.getProperty(), sort.getDirection()));
        });
        String url = builder.encode().toUriString();

        ResponseEntity<PageableResponse<MaterialAidOffer>> list = restTemplate
                .exchange(url, HttpMethod.GET, null, getResponseType());
        return list.getBody();
    }

    @Builder
    private static MaterialAidOffer materialAidOfferBuilder(
            String title,
            String description,
            MaterialAidCategory category,
            Location location
    ) {
        MaterialAidOffer offer = new MaterialAidOffer();
        offer.title = Optional.ofNullable(title).orElse("some title");
        offer.description = Optional.ofNullable(description).orElse("some description");
        offer.category = category;
        offer.location = location;
        return offer;
    }
}
