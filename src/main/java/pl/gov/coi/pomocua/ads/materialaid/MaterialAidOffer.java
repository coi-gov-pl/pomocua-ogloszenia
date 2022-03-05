package pl.gov.coi.pomocua.ads.materialaid;

import lombok.EqualsAndHashCode;
import pl.gov.coi.pomocua.ads.BaseOffer;
import pl.gov.coi.pomocua.ads.Location;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;

import static javax.persistence.EnumType.STRING;

@EqualsAndHashCode(callSuper = true)
@Entity
public class MaterialAidOffer extends BaseOffer {

    @Enumerated(STRING)
    public MaterialAidCategory category;

    @Embedded
    public Location location;
}
