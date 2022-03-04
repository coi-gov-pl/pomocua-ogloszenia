package pl.gov.coi.pomocua.ads.accomodations;

import lombok.EqualsAndHashCode;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.gov.coi.pomocua.ads.BaseOffer;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

import static javax.persistence.EnumType.STRING;

@EqualsAndHashCode(callSuper = true)
@Entity
class AccommodationOffer extends BaseOffer {
    public String voivodeship;
    @NotBlank
    public String city;

    @Min(1)
    public int guests;

    @Enumerated(STRING)
    public LengthOfStay lengthOfStay;

    @ElementCollection(targetClass = Language.class)
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

    enum Language {
        UA, PL
    }
}

@Repository
interface AccommodationsRepository extends PagingAndSortingRepository<AccommodationOffer, Long> {

}


