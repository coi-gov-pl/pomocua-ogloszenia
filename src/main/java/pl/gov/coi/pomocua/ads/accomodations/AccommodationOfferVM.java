package pl.gov.coi.pomocua.ads.accomodations;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.gov.coi.pomocua.ads.Language;
import pl.gov.coi.pomocua.ads.Location;
import pl.gov.coi.pomocua.ads.BaseOfferVM;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class AccommodationOfferVM extends BaseOfferVM {

    @Valid
    @NotNull
    private Location location;

    @Min(1)
    @NotNull
    private Integer guests;

    @NotNull
    private AccommodationOffer.LengthOfStay lengthOfStay;

    @NotEmpty
    private List<Language> hostLanguage;

    @NotNull
    public final AccommodationOffer.Type type = AccommodationOffer.Type.ACCOMMODATION;

    public static AccommodationOfferVM from(AccommodationOffer offer, Language viewLang) {
        AccommodationOfferVM result = new AccommodationOfferVM();
        mapBase(offer, viewLang, result);
        result.location = offer.location;
        result.guests = offer.guests;
        result.lengthOfStay = offer.lengthOfStay;
        result.hostLanguage = offer.hostLanguage;
        return  result;
    }
}
