package pl.gov.coi.pomocua.ads.other;

import pl.gov.coi.pomocua.ads.BaseOfferDefinitionDTO;
import pl.gov.coi.pomocua.ads.Location;

import javax.validation.Valid;

public class OtherOfferDefinitionDTO extends BaseOfferDefinitionDTO<OtherOffer> {

    @Valid
    public Location location;

    @Override
    protected void applyOfferSpecific(OtherOffer offer) {
        offer.location = location;
    }
}
