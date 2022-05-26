package pl.gov.coi.pomocua.ads.accomodations;

import lombok.Data;
import pl.gov.coi.pomocua.ads.Language;

@Data
public class AccommodationOfferSearchCriteria {
    private Integer capacity = 1;
    private Language lang = Language.PL;
}
