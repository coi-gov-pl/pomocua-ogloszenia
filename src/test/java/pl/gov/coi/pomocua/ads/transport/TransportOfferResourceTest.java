package pl.gov.coi.pomocua.ads.transport;

import org.springframework.core.ParameterizedTypeReference;
import pl.gov.coi.pomocua.ads.BaseResourceTest;
import pl.gov.coi.pomocua.ads.Location;
import pl.gov.coi.pomocua.ads.PageableResponse;

import java.time.LocalDate;

class TransportOfferResourceTest  extends BaseResourceTest<TransportOffer> {

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
}