package pl.gov.coi.pomocua.ads.transport;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.gov.coi.pomocua.ads.Language;
import pl.gov.coi.pomocua.ads.Location;
import pl.gov.coi.pomocua.ads.BaseOfferVM;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public class TransportOfferVM extends BaseOfferVM {

    private Location origin;

    private Location destination;

    private Integer capacity;

    private LocalDate transportDate;

    public final TransportOffer.Type type = TransportOffer.Type.TRANSPORT;

    public static TransportOfferVM from(TransportOffer offer, Language viewLang) {
        TransportOfferVM result = new TransportOfferVM();
        mapBase(offer, viewLang, result);
        result.origin = offer.origin;
        result.destination = offer.destination;
        result.capacity = offer.capacity;
        result.transportDate = offer.transportDate;
        return result;
    }
}
