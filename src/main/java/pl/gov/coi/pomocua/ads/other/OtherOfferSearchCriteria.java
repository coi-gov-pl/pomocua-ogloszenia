package pl.gov.coi.pomocua.ads.other;

import lombok.Data;
import pl.gov.coi.pomocua.ads.Language;
import pl.gov.coi.pomocua.ads.Location;

@Data
public class OtherOfferSearchCriteria {
    private Location location;
    private String searchText;
    private Language lang = Language.PL;
}
