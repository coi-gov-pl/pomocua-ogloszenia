package pl.gov.coi.pomocua.ads.materialaid;

import pl.gov.coi.pomocua.ads.BaseOfferDefinitionDTO;
import pl.gov.coi.pomocua.ads.Location;

import javax.persistence.Enumerated;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static javax.persistence.EnumType.STRING;

public class MaterialAidOfferDefinitionDTO extends BaseOfferDefinitionDTO<MaterialAidOffer> {

    @Valid
    @NotNull
    public Location location;

    @Enumerated(STRING)
    @NotNull
    public MaterialAidCategory category;

    @Override
    protected void applyOfferSpecific(MaterialAidOffer offer) {
        offer.category = category;
        offer.location = location;
    }
}
