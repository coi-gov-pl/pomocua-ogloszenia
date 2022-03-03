package pl.gov.coi.pomocua.ads.translations;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

import static javax.persistence.EnumType.STRING;

@Data
@Entity
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY)
public class TranslationOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotBlank
    @Length(max = 80)
    public String title;

    @Enumerated(STRING)
    public Mode mode;

    @ElementCollection(targetClass = Language.class)
    @CollectionTable
    @Enumerated(STRING)
    public List<Language> language;

    public String voivodeship;
    public String city;
    public boolean sworn;

    @NotBlank
    @Length(max = 1000)
    public String description;

    enum Mode {
        REMOTE
    }

    enum Language {
        UA, PL
    }

}

@Repository
interface TranslationOfferRepository extends PagingAndSortingRepository<TranslationOffer, Long> {
}