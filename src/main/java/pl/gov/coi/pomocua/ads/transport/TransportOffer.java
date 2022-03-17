package pl.gov.coi.pomocua.ads.transport;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.EqualsAndHashCode;
import org.hibernate.envers.Audited;
import pl.gov.coi.pomocua.ads.BaseOffer;
import pl.gov.coi.pomocua.ads.Location;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Audited
@EqualsAndHashCode(callSuper = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY)
public class TransportOffer extends BaseOffer {

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "region", column = @Column(name = "origin_region")),
            @AttributeOverride(name = "city", column = @Column(name = "origin_city"))
    })
    @Valid
    @NotNull
    public Location origin;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "region", column = @Column(name = "destination_region")),
            @AttributeOverride(name = "city", column = @Column(name = "destination_city"))
    })
    @Valid
    @NotNull
    public Location destination;

    @NotNull
    @Min(1)
    @Max(99)
    public Integer capacity;

    @NotNull
    public LocalDate transportDate;

    @Transient
    public String type = "transport";
}
