package pl.gov.coi.pomocua.ads;

import org.hibernate.validator.constraints.Length;
import pl.gov.coi.pomocua.ads.configuration.validation.PhoneNumber;
import pl.gov.coi.pomocua.ads.phone.PhoneDetails;
import pl.gov.coi.pomocua.ads.phone.PhoneUtil;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import java.util.Optional;

import static pl.gov.coi.pomocua.ads.BaseOffer.DESCRIPTION_ALLOWED_TEXT;
import static pl.gov.coi.pomocua.ads.BaseOffer.TITLE_ALLOWED_TEXT;

public abstract class BaseOfferDefinitionDTO<T extends BaseOffer> {

    @NotBlank
    @Length(max = 80)
    @Pattern(regexp = TITLE_ALLOWED_TEXT)
    public String title;

    @NotBlank
    @Length(max = 2000)
    @Pattern(regexp = DESCRIPTION_ALLOWED_TEXT)
    public String description;

    @PhoneNumber
    public String phoneNumber;

    public void applyTo(T offer) {
        offer.title = title;
        offer.description = description;
        applyOfferSpecific(offer);

        Optional<PhoneDetails> phoneDetails = PhoneUtil.getPhoneDetails(phoneNumber);
        offer.phoneNumber = phoneDetails.map(PhoneDetails::nationalNumber).orElse(null);
        offer.phoneCountryCode = phoneDetails.map(PhoneDetails::countryCode).orElse(null);
    }

    protected abstract void applyOfferSpecific(T offer);
}
