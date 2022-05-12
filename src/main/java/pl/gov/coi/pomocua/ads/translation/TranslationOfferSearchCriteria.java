package pl.gov.coi.pomocua.ads.translation;

import lombok.Data;
import pl.gov.coi.pomocua.ads.Language;
import pl.gov.coi.pomocua.ads.Location;

import java.util.List;

import pl.gov.coi.pomocua.ads.translation.TranslationOffer.TranslationMode;

@Data
public class TranslationOfferSearchCriteria {
    private Location location;
    private List<TranslationMode> mode;
    private List<Language> language;
}
