package pl.gov.coi.pomocua.ads;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;

import static javax.persistence.GenerationType.IDENTITY;

@EqualsAndHashCode()
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY)
@MappedSuperclass
public abstract class BaseOffer {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    public Long id;
    @NotBlank
    @Length(max = 80)
    public String title;

    @NotBlank
    @Length(max = 1000)
    public String description;
}
