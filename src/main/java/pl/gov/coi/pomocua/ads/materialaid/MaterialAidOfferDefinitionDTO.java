package pl.gov.coi.pomocua.ads.materialaid;

import org.hibernate.validator.constraints.Length;
import pl.gov.coi.pomocua.ads.Location;
import pl.gov.coi.pomocua.ads.phone.PhoneDetails;
import pl.gov.coi.pomocua.ads.phone.PhoneUtil;
import pl.gov.coi.pomocua.ads.configuration.validation.PhoneNumber;

import javax.persistence.Enumerated;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Optional;

import static javax.persistence.EnumType.STRING;
import static pl.gov.coi.pomocua.ads.BaseOffer.*;

public class MaterialAidOfferDefinitionDTO {
    @NotBlank
    @Length(max = 80)
    @Pattern(regexp = TITLE_ALLOWED_TEXT)
    public String title;

    @NotBlank
    @Length(max = 2000)
    @Pattern(regexp = DESCRIPTION_ALLOWED_TEXT)
    public String description;

    @Valid
    @NotNull
    public Location location;

    @Enumerated(STRING)
    @NotNull
    public MaterialAidCategory category;

    @PhoneNumber
    public String phoneNumber;

    public void applyTo(MaterialAidOffer offer) {
        offer.title = title;
        offer.description = description;
        offer.category = category;
        offer.location = location;
        offer.phoneNumber = PhoneUtil.getPhoneDetails(phoneNumber).map(PhoneDetails::nationalNumber).orElse(null);
        offer.phoneCountryCode = PhoneUtil.getPhoneDetails(phoneNumber).map(PhoneDetails::countryCode).orElse(null);
    }
}
