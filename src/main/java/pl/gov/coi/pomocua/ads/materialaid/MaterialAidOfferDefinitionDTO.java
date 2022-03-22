package pl.gov.coi.pomocua.ads.materialaid;

import org.hibernate.validator.constraints.Length;
import pl.gov.coi.pomocua.ads.Location;
import pl.gov.coi.pomocua.ads.configuration.validation.PhoneNumber;

import javax.persistence.Enumerated;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static javax.persistence.EnumType.STRING;
import static pl.gov.coi.pomocua.ads.BaseOffer.ALLOWED_TEXT;

public class MaterialAidOfferDefinitionDTO {
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

    @Enumerated(STRING)
    @NotNull
    public MaterialAidCategory category;

    @PhoneNumber
    public String phoneNumber;

    public void applyTo(MaterialAidOffer materialAidOffer) {
        materialAidOffer.title = this.title;
        materialAidOffer.description = this.description;
        materialAidOffer.category = this.category;
        materialAidOffer.location = this.location;
        materialAidOffer.phoneNumber = this.phoneNumber;
    }
}
