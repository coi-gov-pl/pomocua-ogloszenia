package pl.gov.coi.pomocua.ads.transport;

import pl.gov.coi.pomocua.ads.BaseOfferDefinitionDTO;
import pl.gov.coi.pomocua.ads.Location;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDate;

public class TransportOfferDefinitionDTO extends BaseOfferDefinitionDTO<TransportOffer> {

    @Valid
    public Location origin;

    @Valid
    public Location destination;

    @NotNull
    @Min(1)
    @Max(99)
    public Integer capacity;

    @FutureOrPresent
    public LocalDate transportDate;

    @Override
    protected void applyOfferSpecific(TransportOffer offer) {
        offer.origin = origin;
        offer.destination = destination;
        offer.capacity = capacity;
        offer.transportDate = transportDate;
    }
}
