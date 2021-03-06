package pl.gov.coi.pomocua.ads.translation;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.gov.coi.pomocua.ads.Language;
import pl.gov.coi.pomocua.ads.Location;
import pl.gov.coi.pomocua.ads.BaseOfferVM;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class TranslationOfferVM extends BaseOfferVM {

    @NotEmpty
    private List<TranslationOffer.TranslationMode> mode;

    private Location location;

    @NotEmpty
    private List<Language> language;

    @NotNull
    public final TranslationOffer.Type type = TranslationOffer.Type.TRANSLATION;

    public static TranslationOfferVM from(TranslationOffer offer, Language viewLang) {
        TranslationOfferVM result = new TranslationOfferVM();
        mapBase(offer, viewLang, result);
        result.mode = offer.getMode();
        result.location = offer.location;
        result.language = offer.getLanguage();
        return result;
    }
}
