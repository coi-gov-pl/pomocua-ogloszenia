package pl.gov.coi.pomocua.ads.other;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.gov.coi.pomocua.ads.Language;
import pl.gov.coi.pomocua.ads.Location;
import pl.gov.coi.pomocua.ads.BaseOfferVM;

@EqualsAndHashCode(callSuper = true)
@Data
public class OtherOfferVM extends BaseOfferVM {

    private Location location;

    public final OtherOffer.Type type = OtherOffer.Type.OTHER;

    public static OtherOfferVM from(OtherOffer offer, Language viewLang) {
        OtherOfferVM result = new OtherOfferVM();
        mapBase(offer, viewLang, result);
        result.location = offer.location;
        return result;
    }
}
