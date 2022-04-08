package pl.gov.coi.pomocua.ads;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pl.gov.coi.pomocua.ads.phone.PhoneDetails;
import pl.gov.coi.pomocua.ads.phone.PhoneUtil;
import pl.gov.coi.pomocua.ads.users.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.Instant;
import java.util.Optional;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Audited
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@EqualsAndHashCode
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY)
@JsonBaseOfferInheritance
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseOffer {
    public static final String TITLE_ALLOWED_TEXT = "^[^'\"%<>()@]*$";
    public static final String DESCRIPTION_ALLOWED_TEXT = "^[^'\"%<>]*$";

    @Id
    @NotNull
    @GeneratedValue(strategy = SEQUENCE)
    public Long id;

    @Embedded
    @JsonIgnore
    public UserId userId;

    @NotNull
    public String userFirstName;

    @NotBlank
    @Length(max = 80)
    @Pattern(regexp = TITLE_ALLOWED_TEXT)
    public String title;

    @NotBlank
    @Length(max = 2000)
    @Pattern(regexp = DESCRIPTION_ALLOWED_TEXT)
    public String description;

    public String phoneNumber;

    public String phoneCountryCode;

    @LastModifiedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    public Instant modifiedDate;

    @JsonIgnore
    @NotNull
    @Enumerated(EnumType.STRING)
    public Status status = Status.ACTIVE;

    public enum Status {
        ACTIVE, INACTIVE
    }

    public void attachTo(User user) {
        this.userId = user.id();
        this.userFirstName = user.firstName();
    }

    @PostLoad
    private void onLoad() {
        Optional<PhoneDetails> phoneDetailsOpt = PhoneUtil.getPhoneDetails(phoneNumber);
        if (phoneDetailsOpt.isPresent() && StringUtils.isEmpty(phoneCountryCode)) {
            PhoneDetails phoneDetails = phoneDetailsOpt.get();
            phoneCountryCode = phoneDetails.countryCode();
            phoneNumber = phoneDetails.nationalNumber();
        }
    }

    @JsonIgnore
    public boolean isActive() {
        return status == Status.ACTIVE;
    }
}
