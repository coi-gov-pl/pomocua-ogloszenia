package pl.gov.coi.pomocua.ads.law;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.gov.coi.pomocua.ads.Language;
import pl.gov.coi.pomocua.ads.Location;
import pl.gov.coi.pomocua.ads.BaseOfferVM;

import javax.persistence.Embedded;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class LawOfferVM extends BaseOfferVM {

    @Embedded
    private Location location;

    private List<LawOffer.HelpMode> helpMode;

    private List<LawOffer.HelpKind> helpKind;

    private List<Language> language;

    @NotNull
    @Transient
    public final LawOffer.Type type = LawOffer.Type.LAW;

    public static LawOfferVM from(LawOffer offer, Language viewLang) {
        LawOfferVM result = new LawOfferVM();
        mapBase(offer, viewLang, result);
        result.location = offer.location;
        result.helpMode = offer.getHelpMode();
        result.helpKind = offer.getHelpKind();
        result.language = offer.getLanguage();
        return result;
    }
}
