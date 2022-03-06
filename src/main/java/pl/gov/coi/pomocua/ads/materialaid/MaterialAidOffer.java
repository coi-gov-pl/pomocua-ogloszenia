package pl.gov.coi.pomocua.ads.materialaid;

import lombok.EqualsAndHashCode;
import org.hibernate.envers.Audited;
import pl.gov.coi.pomocua.ads.BaseOffer;
import pl.gov.coi.pomocua.ads.Location;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static javax.persistence.EnumType.STRING;

@EqualsAndHashCode(callSuper = true)
@Entity
@Audited
public class MaterialAidOffer extends BaseOffer {

    @NotNull
    @Enumerated(STRING)
    public MaterialAidCategory category;

    @Valid
    @NotNull
    @Embedded
    public Location location;
}
