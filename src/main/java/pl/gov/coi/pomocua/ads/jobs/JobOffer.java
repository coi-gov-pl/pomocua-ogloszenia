package pl.gov.coi.pomocua.ads.jobs;

import lombok.EqualsAndHashCode;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.gov.coi.pomocua.ads.BaseOffer;
import pl.gov.coi.pomocua.ads.Location;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.List;

import static javax.persistence.EnumType.STRING;

@EqualsAndHashCode(callSuper = true)
@Entity
public class JobOffer extends BaseOffer {
    @Enumerated(STRING)
    public Mode mode;

    @Embedded
    @Valid
    public Location location;

    @ElementCollection(targetClass = Type.class)
    @CollectionTable
    @Enumerated(STRING)
    public List<Type> type;

    @ElementCollection(targetClass = Language.class)
    @CollectionTable
    @Enumerated(STRING)
    public List<Language> language;

    enum Mode {
        REMOTE
    }

    enum Type {
        TEMPORARY
    }

    enum Language {
        UA, PL
    }

}

@Repository
interface JobsRepository extends PagingAndSortingRepository<JobOffer, Long> {

}
