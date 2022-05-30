package pl.gov.coi.pomocua.ads.transport;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.EqualsAndHashCode;
import org.hibernate.envers.Audited;
import pl.gov.coi.pomocua.ads.BaseOffer;
import pl.gov.coi.pomocua.ads.BaseOfferVisitor;
import pl.gov.coi.pomocua.ads.Language;
import pl.gov.coi.pomocua.ads.Location;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Audited
@EqualsAndHashCode(callSuper = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY)
public class TransportOffer extends BaseOffer<TransportOfferVM> {

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "region", column = @Column(name = "origin_region")),
            @AttributeOverride(name = "city", column = @Column(name = "origin_city"))
    })
    @Valid
    public Location origin;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "region", column = @Column(name = "destination_region")),
            @AttributeOverride(name = "city", column = @Column(name = "destination_city"))
    })
    @Valid
    public Location destination;

    @NotNull
    @Min(1)
    @Max(99)
    public Integer capacity;

    public LocalDate transportDate;

    @NotNull
    @Transient
    public final Type type = Type.TRANSPORT;

    @Override
    public TransportOfferVM accept(BaseOfferVisitor visitor, Language viewLang) {
        return visitor.visit(this, viewLang);
    }

    public enum Type {
        TRANSPORT
    }
}
