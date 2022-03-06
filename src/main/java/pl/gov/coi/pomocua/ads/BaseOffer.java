package pl.gov.coi.pomocua.ads;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@EqualsAndHashCode
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY)
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class BaseOffer {
    protected static final String ALLOWED_TEXT = "^[^<>()%#@\"']*$";

    @Id
    @GeneratedValue(strategy = IDENTITY)
    public Long id;

    @Embedded
    @JsonIgnore
    public UserId userId;

    @NotBlank
    @Length(max = 80)
    @Pattern(regexp = ALLOWED_TEXT)
    public String title;

    @NotBlank
    @Length(max = 1000)
    @Pattern(regexp = ALLOWED_TEXT)
    public String description;
}
