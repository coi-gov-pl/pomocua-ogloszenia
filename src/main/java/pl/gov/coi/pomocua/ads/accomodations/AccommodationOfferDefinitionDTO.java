package pl.gov.coi.pomocua.ads.accomodations;

import pl.gov.coi.pomocua.ads.BaseOfferDefinitionDTO;
import pl.gov.coi.pomocua.ads.Language;
import pl.gov.coi.pomocua.ads.Location;
import pl.gov.coi.pomocua.ads.accomodations.AccommodationOffer.LengthOfStay;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.LinkedList;
import java.util.List;

public class AccommodationOfferDefinitionDTO extends BaseOfferDefinitionDTO<AccommodationOffer> {

    @Valid
    @NotNull
    public Location location;

    @Min(1)
    @NotNull
    public Integer guests;

    @NotNull
    public LengthOfStay lengthOfStay;

    @NotEmpty
    public List<Language> hostLanguage;

    @Override
    protected void applyOfferSpecific(AccommodationOffer offer) {
        offer.location = location;
        offer.guests = guests;
        offer.lengthOfStay = lengthOfStay;
        offer.hostLanguage = new LinkedList<>(hostLanguage);
    }
}
