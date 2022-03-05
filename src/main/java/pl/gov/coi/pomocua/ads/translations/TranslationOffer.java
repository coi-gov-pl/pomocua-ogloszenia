package pl.gov.coi.pomocua.ads.translations;

import lombok.EqualsAndHashCode;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.gov.coi.pomocua.ads.BaseOffer;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import java.util.List;

import static javax.persistence.EnumType.STRING;

@EqualsAndHashCode(callSuper = true)
@Entity
public class TranslationOffer extends BaseOffer {

    @Enumerated(STRING)
    public Mode mode;

    @ElementCollection(targetClass = Language.class)
    @CollectionTable
    @Enumerated(STRING)
    public List<Language> language;

    public String region;
    public String city;
    public boolean sworn;

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