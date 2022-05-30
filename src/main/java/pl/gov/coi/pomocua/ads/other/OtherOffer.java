package pl.gov.coi.pomocua.ads.other;

import lombok.EqualsAndHashCode;
import org.hibernate.envers.Audited;
import pl.gov.coi.pomocua.ads.BaseOffer;
import pl.gov.coi.pomocua.ads.BaseOfferVisitor;
import pl.gov.coi.pomocua.ads.Language;
import pl.gov.coi.pomocua.ads.Location;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Entity
@Audited
public class OtherOffer extends BaseOffer<OtherOfferVM> {

    @Embedded
    public Location location;

    @NotNull
    @Transient
    public final Type type = Type.OTHER;

    @Override
    public OtherOfferVM accept(BaseOfferVisitor visitor, Language viewLang) {
        return visitor.visit(this, viewLang);
    }

    public enum Type {
        OTHER
    }
}
