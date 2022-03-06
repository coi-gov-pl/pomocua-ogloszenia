package pl.gov.coi.pomocua.ads;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import pl.gov.coi.pomocua.ads.accomodations.AccommodationOffer;
import pl.gov.coi.pomocua.ads.jobs.JobOffer;
import pl.gov.coi.pomocua.ads.materialaid.MaterialAidOffer;
import pl.gov.coi.pomocua.ads.translations.TranslationOffer;
import pl.gov.coi.pomocua.ads.transport.TransportOffer;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@EqualsAndHashCode
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY)
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION, defaultImpl = BaseOffer.class)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AccommodationOffer.class),
        @JsonSubTypes.Type(value = JobOffer.class),
        @JsonSubTypes.Type(value = MaterialAidOffer.class),
        @JsonSubTypes.Type(value = TranslationOffer.class),
        @JsonSubTypes.Type(value = TransportOffer.class)
})
public abstract class BaseOffer {
    protected static final String ALLOWED_TEXT = "^[^<>()%#@\"']*$";

    @Id
    @GeneratedValue(strategy = SEQUENCE)
    public Long id;

    @Embedded
    @JsonIgnore
    public UserId userId;

    @NotBlank
    @Length(max = 80)
    @Pattern(regexp = ALLOWED_TEXT)
    public String title;

    @NotBlank
    @Length(max = 1000)
    @Pattern(regexp = ALLOWED_TEXT)
    public String description;
}
