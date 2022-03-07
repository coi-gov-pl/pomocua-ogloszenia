package pl.gov.coi.pomocua.ads.accomodations;

import lombok.EqualsAndHashCode;
import org.hibernate.envers.Audited;
import pl.gov.coi.pomocua.ads.BaseOffer;
import pl.gov.coi.pomocua.ads.Location;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

import static javax.persistence.EnumType.STRING;

@EqualsAndHashCode(callSuper = true)
@Entity
@Audited
public class AccommodationOffer extends BaseOffer {

    @Embedded
    @Valid
    public Location location;

    @Min(1)
    public int guests;

    @Enumerated(STRING)
    public LengthOfStay lengthOfStay;

    @ElementCollection(targetClass = Language.class, fetch = FetchType.EAGER)
    @CollectionTable
    @Enumerated(STRING)
    public List<Language> hostLanguage;

    enum LengthOfStay {
        WEEK_1,
        WEEK_2,
        MONTH_1,
        MONTH_2,
        MONTH_3,
        LONGER
    }

    public enum Language {
        UA, PL
    }
}

