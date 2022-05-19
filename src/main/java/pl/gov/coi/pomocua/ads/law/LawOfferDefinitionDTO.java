package pl.gov.coi.pomocua.ads.law;

import pl.gov.coi.pomocua.ads.BaseOfferDefinitionDTO;
import pl.gov.coi.pomocua.ads.Language;
import pl.gov.coi.pomocua.ads.Location;
import pl.gov.coi.pomocua.ads.law.LawOffer.HelpKind;
import pl.gov.coi.pomocua.ads.law.LawOffer.HelpMode;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

public class LawOfferDefinitionDTO extends BaseOfferDefinitionDTO<LawOffer> {

    @Valid
    public Location location;

    @NotEmpty
    public List<HelpMode> helpMode;

    @NotEmpty
    public List<HelpKind> helpKind;

    @NotEmpty
    public List<Language> language;

    @Override
    protected void applyOfferSpecific(LawOffer offer) {
        offer.location = location;
        offer.setHelpMode(helpMode);
        offer.setHelpKind(helpKind);
        offer.setLanguage(language);
    }
}
