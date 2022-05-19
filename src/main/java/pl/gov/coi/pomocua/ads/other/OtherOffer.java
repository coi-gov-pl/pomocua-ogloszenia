package pl.gov.coi.pomocua.ads.other;

import lombok.EqualsAndHashCode;
import org.hibernate.envers.Audited;
import pl.gov.coi.pomocua.ads.BaseOffer;
import pl.gov.coi.pomocua.ads.Location;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Entity
@Audited
public class OtherOffer extends BaseOffer {

    @Embedded
    public Location location;

    @NotNull
    @Transient
    public final Type type = Type.OTHER;

    public enum Type {
        OTHER
    }
}
