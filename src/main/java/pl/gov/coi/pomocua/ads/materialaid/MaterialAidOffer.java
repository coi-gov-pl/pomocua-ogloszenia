package pl.gov.coi.pomocua.ads.materialaid;

import lombok.EqualsAndHashCode;
import org.hibernate.envers.Audited;
import pl.gov.coi.pomocua.ads.BaseOffer;
import pl.gov.coi.pomocua.ads.BaseOfferVisitor;
import pl.gov.coi.pomocua.ads.Language;
import pl.gov.coi.pomocua.ads.Location;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static javax.persistence.EnumType.STRING;

@EqualsAndHashCode(callSuper = true)
@Entity
@Audited
public class MaterialAidOffer extends BaseOffer<MaterialAidOfferVM> {

    @Enumerated(STRING)
    @NotNull
    public MaterialAidCategory category;

    @Valid
    @NotNull
    @Embedded
    public Location location;

    @NotNull
    @Transient
    public final Type type = Type.MATERIAL_AID;

    @Override
    public MaterialAidOfferVM accept(BaseOfferVisitor visitor, Language viewLang) {
        return visitor.visit(this, viewLang);
    }

    public enum Type {
        MATERIAL_AID
    }
}
