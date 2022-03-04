package pl.gov.coi.pomocua.ads.assistance;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.EqualsAndHashCode;
import org.springframework.data.repository.PagingAndSortingRepository;
import pl.gov.coi.pomocua.ads.BaseOffer;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.EnumType.STRING;

@EqualsAndHashCode(callSuper = true)
@Entity
public class LegalAssistance extends BaseOffer {
    @Enumerated(STRING)
    public Mode mode;

    public String voivodeship;
    public String city;

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
