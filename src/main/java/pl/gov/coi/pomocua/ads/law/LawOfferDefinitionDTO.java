package pl.gov.coi.pomocua.ads.law;

import org.hibernate.validator.constraints.Length;
import pl.gov.coi.pomocua.ads.Language;
import pl.gov.coi.pomocua.ads.Location;
import pl.gov.coi.pomocua.ads.configuration.validation.PhoneNumber;
import pl.gov.coi.pomocua.ads.law.LawOffer.HelpKind;
import pl.gov.coi.pomocua.ads.law.LawOffer.HelpMode;
import pl.gov.coi.pomocua.ads.phone.PhoneDetails;
import pl.gov.coi.pomocua.ads.phone.PhoneUtil;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Optional;

import static pl.gov.coi.pomocua.ads.BaseOffer.DESCRIPTION_ALLOWED_TEXT;
import static pl.gov.coi.pomocua.ads.BaseOffer.TITLE_ALLOWED_TEXT;

public class LawOfferDefinitionDTO {
    @NotBlank
    @Length(max = 80)
    @Pattern(regexp = TITLE_ALLOWED_TEXT)
    public String title;

    @NotBlank
    @Length(max = 2000)
    @Pattern(regexp = DESCRIPTION_ALLOWED_TEXT)
    public String description;

    public Location location;

    @NotEmpty
    public List<HelpMode> helpMode;

    @NotEmpty
    public List<HelpKind> helpKind;

    @NotEmpty
    public List<Language> language;

    @PhoneNumber
    public String phoneNumber;

    public void applyTo(LawOffer offer) {
        offer.title = title;
        offer.location = location;
        offer.setHelpMode(helpMode);
        offer.setHelpKind(helpKind);
        offer.setLanguage(language);
        offer.description = description;

        Optional<PhoneDetails> phoneDetails = PhoneUtil.getPhoneDetails(phoneNumber);
        offer.phoneNumber = phoneDetails.map(PhoneDetails::nationalNumber).orElse(null);
        offer.phoneCountryCode = phoneDetails.map(PhoneDetails::countryCode).orElse(null);
    }
}
