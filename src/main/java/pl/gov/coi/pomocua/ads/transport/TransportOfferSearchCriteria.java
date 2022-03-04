package pl.gov.coi.pomocua.ads.transport;

import lombok.Data;
import pl.gov.coi.pomocua.ads.Location;

import java.time.LocalDate;

@Data
public class TransportOfferSearchCriteria {
    private Location origin;
    private Location destination;
    private Integer capacity;
    private LocalDate transportDate;

}
