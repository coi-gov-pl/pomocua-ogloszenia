package pl.gov.coi.pomocua.ads.accomodations;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.gov.coi.pomocua.ads.Language;
import pl.gov.coi.pomocua.ads.Location;
import pl.gov.coi.pomocua.ads.BaseOfferVM;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class AccommodationOfferVM extends BaseOfferVM {

    private Location location;

    private Integer guests;

    private AccommodationOffer.LengthOfStay lengthOfStay;

    private List<Language> hostLanguage;

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
