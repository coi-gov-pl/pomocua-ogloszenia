package pl.gov.coi.pomocua.ads.materialaid;

import lombok.Data;
import pl.gov.coi.pomocua.ads.Language;
import pl.gov.coi.pomocua.ads.Location;

@Data
public class MaterialAidOfferSearchCriteria {
    private MaterialAidCategory category;
    private Location location;
    private Language lang = Language.PL;
}
