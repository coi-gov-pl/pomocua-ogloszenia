package pl.gov.coi.pomocua.ads.health;

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
public class HealthOfferVM extends BaseOfferVM {

    @NotEmpty
    private List<HealthOffer.HealthCareMode> mode;

    @NotNull
    private HealthCareSpecialization specialization;

    private Location location;

    @NotEmpty
    private List<Language> language;

    @NotNull
    public final HealthOffer.Type type = HealthOffer.Type.HEALTH;

    public static HealthOfferVM from(HealthOffer offer, Language viewLang) {
        HealthOfferVM result = new HealthOfferVM();
        mapBase(offer, viewLang, result);
        result.mode = offer.getMode();
        result.specialization = offer.specialization;
        result.location = offer.location;
        result.language = offer.getLanguage();
        return result;
    }
}
