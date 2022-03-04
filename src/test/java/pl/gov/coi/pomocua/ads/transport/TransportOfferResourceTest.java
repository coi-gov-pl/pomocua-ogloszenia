package pl.gov.coi.pomocua.ads.transport;

import lombok.Builder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;
import pl.gov.coi.pomocua.ads.BaseResourceTest;
import pl.gov.coi.pomocua.ads.Location;
import pl.gov.coi.pomocua.ads.PageableResponse;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class TransportOfferResourceTest extends BaseResourceTest<TransportOffer> {

    @Autowired
    private TransportOfferRepository repository;

    @Override
    protected Class<TransportOffer> getClazz() {
        return TransportOffer.class;
    }

    @Override
    protected String getOfferSuffix() {
        return "transport";
    }

    @Override
    protected ParameterizedTypeReference<PageableResponse<TransportOffer>> getResponseType() {
        return new ParameterizedTypeReference<>() {
        };
    }

    @BeforeEach
    public void clearDatabase() {
        repository.deleteAll();
    }

    @Override
    protected TransportOffer sampleOfferRequest() {
        TransportOffer transportOffer = new TransportOffer();
        transportOffer.title = "jade do Pcimia";
        transportOffer.description = "moge zabrac 20 osob";
        transportOffer.destination = new Location("Pomorskie", "Gdańsk");
        transportOffer.origin = new Location("Pomorskie", "Pruszcz Gdański");
        transportOffer.transportDate = LocalDate.of(2022, 4, 1);
        transportOffer.capacity = 28;
        return transportOffer;
    }

    @Test
    void shouldFindByOrigin() {
        TransportOffer transportOffer1 = postOffer(aTransportOffer()
                .origin(new Location("mazowieckie", "warszawa"))
                .build());
        postOffer(aTransportOffer()
                .origin(new Location("Pomorskie", "Wejherowo"))
                .build());
        postOffer(aTransportOffer()
                .origin(new Location("Wielkopolskie", "Warszawa"))
                .build());

        TransportOfferSearchCriteria searchCriteria = new TransportOfferSearchCriteria();
        searchCriteria.setOrigin(new Location("Mazowieckie", "Warszawa"));
        TransportOffer[] results = searchOffers(searchCriteria);

        assertThat(results.length).isEqualTo(1);
        assertThat(results[0]).isEqualTo(transportOffer1);
    }

    @Test
    void shouldFindByDestination() {
        TransportOffer transportOffer1 = postOffer(aTransportOffer()
                .destination(new Location("pomorskie", "GdyniA"))
                .build());
        postOffer(aTransportOffer()
                .destination(new Location("Pomorskie", "Wejherowo"))
                .build());

        TransportOfferSearchCriteria searchCriteria = new TransportOfferSearchCriteria();
        searchCriteria.setDestination(new Location("Pomorskie", "Gdynia"));
        TransportOffer[] results = searchOffers(searchCriteria);

        assertThat(results.length).isEqualTo(1);
        assertThat(results[0]).isEqualTo(transportOffer1);
    }

    @Test
    void shouldFindByCapacity() {
        TransportOffer transportOffer1 = postOffer(aTransportOffer()
                .capacity(10)
                .build());
        TransportOffer transportOffer2 = postOffer(aTransportOffer()
                .capacity(11)
                .build());
        postOffer(aTransportOffer()
                .capacity(1)
                .build());

        TransportOfferSearchCriteria searchCriteria = new TransportOfferSearchCriteria();
        searchCriteria.setCapacity(10);
        TransportOffer[] results = searchOffers(searchCriteria);

        assertThat(results.length).isEqualTo(2);
        assertThat(results[0]).isEqualTo(transportOffer1);
        assertThat(results[1]).isEqualTo(transportOffer2);
    }

    @Test
    void shouldFindByTransportDate() {
        TransportOffer transportOffer1 = postOffer(aTransportOffer()
                .transportDate(LocalDate.of(2022, 3, 21))
                .build());
        postOffer(aTransportOffer()
                .transportDate(LocalDate.of(2022, 3, 22))
                .build());

        TransportOfferSearchCriteria searchCriteria = new TransportOfferSearchCriteria();
        searchCriteria.setTransportDate(LocalDate.of(2022, 3, 21));
        TransportOffer[] results = searchOffers(searchCriteria);

        assertThat(results.length).isEqualTo(1);
        assertThat(results[0]).isEqualTo(transportOffer1);
    }

    @Test
    void shouldReturnPageOfData() {
        postOffer(aTransportOffer().title("a").build());
        postOffer(aTransportOffer().title("b").build());
        postOffer(aTransportOffer().title("c").build());
        postOffer(aTransportOffer().title("d").build());
        postOffer(aTransportOffer().title("e").build());
        postOffer(aTransportOffer().title("f").build());

        PageRequest page = PageRequest.of(1, 2);
        PageableResponse<TransportOffer> results = searchOffers(page);

        assertThat(results.totalElements).isEqualTo(6);
        assertThat(results.content)
                .hasSize(2)
                .extracting(r -> r.title)
                .containsExactly("c", "d");
    }

    @Test
    void shouldSortResults() {
        postOffer(aTransportOffer().title("a").build());
        postOffer(aTransportOffer().title("b").build());
        postOffer(aTransportOffer().title("c").build());

        PageRequest page = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "title"));
        PageableResponse<TransportOffer> results = searchOffers(page);

        assertThat(results.content)
                .extracting(r -> r.title)
                .containsExactly("c", "b", "a");
    }

    private TransportOffer[] searchOffers(TransportOfferSearchCriteria searchCriteria) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/api/" + getOfferSuffix());
        if (searchCriteria.getOrigin() != null) {
            builder
                    .queryParam("origin.city", searchCriteria.getOrigin().getCity())
                    .queryParam("origin.voivodeship", searchCriteria.getOrigin().getVoivodeship());
        }
        if (searchCriteria.getDestination() != null) {
            builder
                    .queryParam("destination.city", searchCriteria.getDestination().getCity())
                    .queryParam("destination.voivodeship", searchCriteria.getDestination().getVoivodeship());
        }
        if (searchCriteria.getCapacity() != null) {
            builder.queryParam("capacity", searchCriteria.getCapacity());
        }
        if (searchCriteria.getTransportDate() != null) {
            builder.queryParam("transportDate", searchCriteria.getTransportDate());
        }
        String url = builder.encode().toUriString();

        ResponseEntity<PageableResponse<TransportOffer>> list = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                getResponseType()
        );
        return list.getBody().content;
    }

    private PageableResponse<TransportOffer> searchOffers(PageRequest pageRequest) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/api/" + getOfferSuffix());
        builder.queryParam("page", pageRequest.getPageNumber());
        builder.queryParam("size", pageRequest.getPageSize());
        pageRequest.getSort().forEach(sort -> {
            builder.queryParam("sort", "%s,%s".formatted(sort.getProperty(), sort.getDirection()));
        });
        String url = builder.encode().toUriString();

        ResponseEntity<PageableResponse<TransportOffer>> list = restTemplate
                .exchange(url, HttpMethod.GET, null, getResponseType());
        return list.getBody();
    }

    private TransportOfferBuilder aTransportOffer() {
        return TransportOfferResourceTest.builder();
    }

    @Builder
    private static TransportOffer transportOfferBuilder(
            String title,
            String description,
            Location origin,
            Location destination,
            Integer capacity,
            LocalDate transportDate
    ) {
        TransportOffer result = new TransportOffer();
        result.title = Optional.ofNullable(title).orElse("some title");
        result.description = Optional.ofNullable(description).orElse("some description");
        result.origin = origin;
        result.destination = destination;
        result.capacity = capacity;
        result.transportDate = transportDate;
        return result;
    }
}