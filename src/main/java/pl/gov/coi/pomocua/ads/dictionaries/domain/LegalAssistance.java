package pl.gov.coi.pomocua.ads.dictionaries.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import java.util.List;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY)
public class LegalAssistance {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotBlank
    @Length(max = 80)
    public String title;

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

    @NotBlank
    @Length(max = 1000)
    public String description;

    enum Language {}

    enum Mode {}

    enum HelpType {}
}
