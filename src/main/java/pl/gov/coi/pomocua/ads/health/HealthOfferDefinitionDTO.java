package pl.gov.coi.pomocua.ads.health;

import pl.gov.coi.pomocua.ads.BaseOfferDefinitionDTO;
import pl.gov.coi.pomocua.ads.Language;
import pl.gov.coi.pomocua.ads.Location;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.util.List;

import pl.gov.coi.pomocua.ads.health.HealthOffer.HealthCareMode;

public class HealthOfferDefinitionDTO extends BaseOfferDefinitionDTO<HealthOffer> {

    @NotEmpty
    public List<HealthCareMode> mode;

    @NotNull
    public HealthCareSpecialization specialization;

    @Valid
    public Location location;

    @NotEmpty
    public List<Language> language;

    @Override
    protected void applyOfferSpecific(HealthOffer offer) {
        offer.setMode(mode);
        offer.specialization = specialization;
        offer.location = location;
        offer.setLanguage(language);
    }
}