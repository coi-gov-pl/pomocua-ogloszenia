package pl.gov.coi.pomocua.ads.transport;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.gov.coi.pomocua.ads.Language;
import pl.gov.coi.pomocua.ads.Location;
import pl.gov.coi.pomocua.ads.BaseOfferVM;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public class TransportOfferVM extends BaseOfferVM {

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "region", column = @Column(name = "origin_region")),
            @AttributeOverride(name = "city", column = @Column(name = "origin_city"))
    })
    private Location origin;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "region", column = @Column(name = "destination_region")),
            @AttributeOverride(name = "city", column = @Column(name = "destination_city"))
    })
    private Location destination;

    private Integer capacity;

    private LocalDate transportDate;

    @NotNull
    @Transient
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
