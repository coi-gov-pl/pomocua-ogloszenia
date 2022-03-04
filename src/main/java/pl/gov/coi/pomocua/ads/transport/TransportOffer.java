package pl.gov.coi.pomocua.ads.transport;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.EqualsAndHashCode;
import pl.gov.coi.pomocua.ads.BaseOffer;
import pl.gov.coi.pomocua.ads.Location;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@EqualsAndHashCode(callSuper = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY)
public class TransportOffer extends BaseOffer {

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "voivodeship", column = @Column(name = "origin_voivodeship")),
            @AttributeOverride(name = "city", column = @Column(name = "origin_city"))
    })
    public Location origin;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "voivodeship", column = @Column(name = "destination_voivodeship")),
            @AttributeOverride(name = "city", column = @Column(name = "destination_city"))
    })
    public Location destination;

    public Integer capacity;

    public LocalDate transportDate;

    public static TransportOffer of(String title, String description, Location origin, Location destination, Integer capacity) {
        TransportOffer transportOffer = new TransportOffer();
        transportOffer.title = title;
        transportOffer.description = description;
        transportOffer.origin = origin;
        transportOffer.destination = destination;
        transportOffer.capacity = capacity;
        transportOffer.transportDate = LocalDate.now();
        return transportOffer;
    }

}
