package pl.gov.coi.pomocua.ads.work;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;

import static javax.persistence.EnumType.STRING;

@Data
@Entity
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY)
class WorkOffer {
    @Id
    public UUID id;
    @NotBlank
    @Length(max = 80)
    public String title;

    @Enumerated(STRING)
    public Mode mode;
    public String voivodeship;
    public String city;

    @ElementCollection(targetClass = Type.class)
    @CollectionTable
    @Enumerated(STRING)
    public List<Type> type;

    @ElementCollection(targetClass = Language.class)
    @CollectionTable
    @Enumerated(STRING)
    public List<Language> language;

    @NotBlank
    @Length(max = 1000)
    public String description;

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
interface WorkOfferRepository extends PagingAndSortingRepository<WorkOffer, UUID> {

}


