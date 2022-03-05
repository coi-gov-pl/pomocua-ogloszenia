package pl.gov.coi.pomocua.ads.transport;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import pl.gov.coi.pomocua.ads.Location;

import javax.validation.Valid;
import java.time.LocalDate;

@Data
public class TransportOfferSearchCriteria {
    @Valid
    private Location origin;
    @Valid
    private Location destination;
    private Integer capacity;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate transportDate;
}
