package pl.gov.coi.pomocua.ads.health;

import lombok.Data;
import pl.gov.coi.pomocua.ads.Language;
import pl.gov.coi.pomocua.ads.Location;

import pl.gov.coi.pomocua.ads.health.HealthOffer.HealthCareMode;

import java.util.List;

@Data
public class HealthOfferSearchCriteria {
    private Location location;
    private HealthCareSpecialization specialization;
    private List<Language> language;
    private List<HealthCareMode> mode;
    private Language lang = Language.PL;
}
