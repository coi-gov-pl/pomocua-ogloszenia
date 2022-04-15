package pl.gov.coi.pomocua.ads.health;

import org.hibernate.validator.constraints.Length;
import pl.gov.coi.pomocua.ads.Language;
import pl.gov.coi.pomocua.ads.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import java.util.List;
import java.util.Optional;

import static pl.gov.coi.pomocua.ads.BaseOffer.DESCRIPTION_ALLOWED_TEXT;
import static pl.gov.coi.pomocua.ads.BaseOffer.TITLE_ALLOWED_TEXT;

import pl.gov.coi.pomocua.ads.configuration.validation.PhoneNumber;
import pl.gov.coi.pomocua.ads.health.HealthOffer.HealthCareMode;
import pl.gov.coi.pomocua.ads.phone.PhoneDetails;
import pl.gov.coi.pomocua.ads.phone.PhoneUtil;

public class HealthOfferDefinitionDTO {

    @NotBlank
    @Length(max = 80)
    @Pattern(regexp = TITLE_ALLOWED_TEXT)
    public String title;

    @NotBlank
    @Length(max = 2000)
    @Pattern(regexp = DESCRIPTION_ALLOWED_TEXT)
    public String description;

    @NotEmpty
    public List<HealthCareMode> mode;

    @NotNull
    public HealthCareSpecialization specialization;

    public Location location;

    @NotEmpty
    public List<Language> language;

    @PhoneNumber
    public String phoneNumber;

    public void applyTo(HealthOffer offer) {
        offer.title = title;
        offer.setMode(mode);
        offer.specialization = specialization;
        offer.location = location;
        offer.setLanguage(language);
        offer.description = description;

        Optional<PhoneDetails> phoneDetails = PhoneUtil.getPhoneDetails(phoneNumber);
        offer.phoneNumber = phoneDetails.map(PhoneDetails::nationalNumber).orElse(null);
        offer.phoneCountryCode = phoneDetails.map(PhoneDetails::countryCode).orElse(null);    }
}
