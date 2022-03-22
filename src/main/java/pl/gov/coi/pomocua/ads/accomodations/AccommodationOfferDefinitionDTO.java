package pl.gov.coi.pomocua.ads.accomodations;

import org.hibernate.validator.constraints.Length;
import pl.gov.coi.pomocua.ads.Location;
import pl.gov.coi.pomocua.ads.accomodations.AccommodationOffer.Language;
import pl.gov.coi.pomocua.ads.accomodations.AccommodationOffer.LengthOfStay;
import pl.gov.coi.pomocua.ads.configuration.validation.PhoneNumber;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.LinkedList;
import java.util.List;

import static pl.gov.coi.pomocua.ads.BaseOffer.ALLOWED_TEXT;

public class AccommodationOfferDefinitionDTO {
    @NotBlank
    @Length(max = 80)
    @Pattern(regexp = ALLOWED_TEXT)
    public String title;

    @NotBlank
    @Length(max = 2000)
    @Pattern(regexp = ALLOWED_TEXT)
    public String description;

    @Valid
    @NotNull
    public Location location;

    @Min(1)
    @NotNull
    public Integer guests;

    @NotNull
    public LengthOfStay lengthOfStay;

    @NotEmpty
    public List<Language> hostLanguage;

    @PhoneNumber
    public String phoneNumber;

    public void applyTo(AccommodationOffer offer) {
        offer.title = title;
        offer.description = description;
        offer.location = location;
        offer.guests = guests;
        offer.lengthOfStay = lengthOfStay;
        offer.hostLanguage = new LinkedList<>(hostLanguage);
        offer.phoneNumber = phoneNumber;
    }
}
