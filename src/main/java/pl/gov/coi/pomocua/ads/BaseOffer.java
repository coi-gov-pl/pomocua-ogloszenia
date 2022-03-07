package pl.gov.coi.pomocua.ads;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.ws.rs.DefaultValue;
import java.time.Instant;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Audited
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@EqualsAndHashCode
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY)
@JsonBaseOfferInheritance
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseOffer {
    public static final String ALLOWED_TEXT = "^[^<>()%#@\"']*$";

    @Id
    @GeneratedValue(strategy = SEQUENCE)
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

    @JsonIgnore
    @LastModifiedDate
    public Instant modifiedDate;

    @NotNull
    public Status status = Status.ACTIVE;

    public enum Status {
        ACTIVE, INACTIVE
    }
}
