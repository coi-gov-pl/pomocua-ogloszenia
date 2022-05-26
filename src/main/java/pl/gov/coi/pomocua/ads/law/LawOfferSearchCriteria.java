package pl.gov.coi.pomocua.ads.law;

import lombok.Data;
import pl.gov.coi.pomocua.ads.Language;
import pl.gov.coi.pomocua.ads.Location;
import pl.gov.coi.pomocua.ads.law.LawOffer.HelpKind;
import pl.gov.coi.pomocua.ads.law.LawOffer.HelpMode;

import java.util.List;

@Data
public class LawOfferSearchCriteria {
    private Location location;
    private List<HelpMode> helpMode;
    private HelpKind helpKind;
    private List<Language> language;
    private Language lang = Language.PL;
}
