package pl.gov.coi.pomocua.ads.translation;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import java.util.List;

import pl.gov.coi.pomocua.ads.BaseOfferDefinitionDTO;
import pl.gov.coi.pomocua.ads.Language;
import pl.gov.coi.pomocua.ads.Location;
import pl.gov.coi.pomocua.ads.translation.TranslationOffer.TranslationMode;

public class TranslationOfferDefinitionDTO extends BaseOfferDefinitionDTO<TranslationOffer> {

    @NotEmpty
    public List<TranslationMode> mode;

    @Valid
    public Location location;

    @NotEmpty
    public List<Language> language;

    @Override
    protected void applyOfferSpecific(TranslationOffer offer) {
        offer.setMode(mode);
        offer.location = location;
        offer.setLanguage(language);
    }
}
