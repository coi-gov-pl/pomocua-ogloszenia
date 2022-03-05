package pl.gov.coi.pomocua.ads.assistance;

import lombok.EqualsAndHashCode;
import org.springframework.data.repository.PagingAndSortingRepository;
import pl.gov.coi.pomocua.ads.BaseOffer;
import pl.gov.coi.pomocua.ads.Location;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.EnumType.STRING;

@EqualsAndHashCode(callSuper = true)
@Entity
public class LegalAssistance extends BaseOffer {
    @Enumerated(STRING)
    public Mode mode;

    public Location location;

    @ElementCollection(targetClass = HelpType.class)
    @CollectionTable
    @Enumerated(STRING)
    public List<HelpType> typeOfHelp;

    @ElementCollection(targetClass = Language.class)
    @CollectionTable
    @Enumerated(STRING)
    public List<Language> language;

    enum Language {}

    enum Mode {}

    enum HelpType {}
}

interface LegalAssistanceRepository  extends PagingAndSortingRepository<LegalAssistance, Long> {
}
