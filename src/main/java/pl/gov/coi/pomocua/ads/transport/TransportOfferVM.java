package pl.gov.coi.pomocua.ads.transport;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.gov.coi.pomocua.ads.Language;
import pl.gov.coi.pomocua.ads.Location;
import pl.gov.coi.pomocua.ads.BaseOfferVM;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public class TransportOfferVM extends BaseOfferVM {

    @Valid
    private Location origin;

    @Valid
    private Location destination;

    @NotNull
    @Min(1)
    @Max(99)
    private Integer capacity;

    private LocalDate transportDate;

    @NotNull
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
