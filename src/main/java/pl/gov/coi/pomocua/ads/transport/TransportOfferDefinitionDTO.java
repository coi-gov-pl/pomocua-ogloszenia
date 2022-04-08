package pl.gov.coi.pomocua.ads.transport;

import org.hibernate.validator.constraints.Length;
import pl.gov.coi.pomocua.ads.Location;
import pl.gov.coi.pomocua.ads.phone.PhoneDetails;
import pl.gov.coi.pomocua.ads.phone.PhoneUtil;
import pl.gov.coi.pomocua.ads.configuration.validation.PhoneNumber;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Optional;

import static pl.gov.coi.pomocua.ads.BaseOffer.*;

public class TransportOfferDefinitionDTO {
    @NotBlank
    @Length(max = 80)
    @Pattern(regexp = TITLE_ALLOWED_TEXT)
    public String title;

    @NotBlank
    @Length(max = 2000)
    @Pattern(regexp = DESCRIPTION_ALLOWED_TEXT)
    public String description;

    @Valid
    public Location origin;

    @Valid
    public Location destination;

    @NotNull
    @Min(1)
    @Max(99)
    public Integer capacity;

    @FutureOrPresent
    public LocalDate transportDate;

    @PhoneNumber
    public String phoneNumber;

    public void applyTo(TransportOffer offer) {
        offer.title = title;
        offer.description = description;
        offer.origin = origin;
        offer.destination = destination;
        offer.capacity = capacity;
        offer.transportDate = transportDate;
        offer.phoneNumber = PhoneUtil.getPhoneDetails(phoneNumber).map(PhoneDetails::nationalNumber).orElse(null);
        offer.phoneCountryCode = PhoneUtil.getPhoneDetails(phoneNumber).map(PhoneDetails::countryCode).orElse(null);
    }
}
