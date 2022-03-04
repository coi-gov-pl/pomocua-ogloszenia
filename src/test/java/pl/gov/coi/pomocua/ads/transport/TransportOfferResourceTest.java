package pl.gov.coi.pomocua.ads.transport;

import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;
import pl.gov.coi.pomocua.ads.BaseResourceTest;
import pl.gov.coi.pomocua.ads.Location;
import pl.gov.coi.pomocua.ads.PageableResponse;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransportOfferResourceTest extends BaseResourceTest<TransportOffer> {

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
        TransportOffer transportOffer1 = sampleOfferRequest();
        transportOffer1.origin = new Location("mazowieckie", "warszawa");
        transportOffer1 = postOffer(transportOffer1);
        TransportOffer transportOffer2 = sampleOfferRequest();
        transportOffer2.origin = new Location("Pomorskie", "Wejherowo");
        postOffer(transportOffer2);
        TransportOffer transportOffer3 = sampleOfferRequest();
        transportOffer3.origin = new Location("Wielkopolskie", "Warszawa");
        postOffer(transportOffer3);

        TransportOfferSearchCriteria searchCriteria = new TransportOfferSearchCriteria();
        searchCriteria.setOrigin(new Location("Mazowieckie", "Warszawa"));

        TransportOffer[] results = searchOffers(searchCriteria);

        assertEquals(1, results.length);
        assertEquals(transportOffer1, results[0]);
    }

    @Test
    void shouldFindByDestination() {
        TransportOffer transportOffer1 = sampleOfferRequest();
        transportOffer1.destination = new Location("pomorskie", "GdyniA");
        transportOffer1 = postOffer(transportOffer1);
        TransportOffer transportOffer2 = sampleOfferRequest();
        transportOffer2.destination = new Location("Pomorskie", "Wejherowo");
        postOffer(transportOffer2);

        TransportOfferSearchCriteria searchCriteria = new TransportOfferSearchCriteria();
        searchCriteria.setDestination(new Location("Pomorskie", "Gdynia"));

        TransportOffer[] results = searchOffers(searchCriteria);

        assertEquals(1, results.length);
        assertEquals(transportOffer1, results[0]);
    }

    /*
    * TODO
    *  test for pagination
    *  test and implementation for searching by capacity and transportDate
    * */

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
        String url = builder.encode().toUriString();

        ResponseEntity<PageableResponse<TransportOffer>> list = restTemplate
                .exchange(url, HttpMethod.GET, null, getResponseType());
        return list.getBody().content;
    }
}