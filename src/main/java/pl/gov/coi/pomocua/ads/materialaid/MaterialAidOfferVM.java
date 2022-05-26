package pl.gov.coi.pomocua.ads.materialaid;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.gov.coi.pomocua.ads.Language;
import pl.gov.coi.pomocua.ads.Location;
import pl.gov.coi.pomocua.ads.BaseOfferVM;

import javax.persistence.Embedded;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
public class MaterialAidOfferVM extends BaseOfferVM {

    private MaterialAidCategory category;

    @Embedded
    private Location location;

    @NotNull
    @Transient
    public final MaterialAidOffer.Type type = MaterialAidOffer.Type.MATERIAL_AID;

    public static MaterialAidOfferVM from(MaterialAidOffer offer, Language viewLang) {
        MaterialAidOfferVM result = new MaterialAidOfferVM();
        mapBase(offer, viewLang, result);
        result.category = offer.category;
        result.location = offer.location;
        return result;
    }
}
