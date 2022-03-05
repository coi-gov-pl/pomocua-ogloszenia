package pl.gov.coi.pomocua.ads.legalassistances;

import lombok.EqualsAndHashCode;
import org.springframework.data.repository.PagingAndSortingRepository;
import pl.gov.coi.pomocua.ads.BaseOffer;
import pl.gov.coi.pomocua.ads.Location;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.EnumType.STRING;

@EqualsAndHashCode(callSuper = true)
@Entity
public class LegalAssistanceOffer extends BaseOffer {
    @Enumerated(STRING)
    public Mode mode;

    @Embedded
    public Location location;

    @ElementCollection(targetClass = Type.class)
    @CollectionTable
    @Enumerated(STRING)
    public List<Type> type;

    @ElementCollection(targetClass = Language.class)
    @CollectionTable
    @Enumerated(STRING)
    public List<Language> language;

    enum Language {
        PL
    }

    enum Mode {
        REMOTE
    }

    enum Type {
        TEMPORARY
    }
}

interface LegalAssistanceRepository  extends PagingAndSortingRepository<LegalAssistanceOffer, Long> {
}
