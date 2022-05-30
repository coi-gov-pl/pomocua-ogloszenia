package pl.gov.coi.pomocua.ads.materialaid;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.gov.coi.pomocua.ads.Language;
import pl.gov.coi.pomocua.ads.Location;
import pl.gov.coi.pomocua.ads.BaseOfferVM;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
public class MaterialAidOfferVM extends BaseOfferVM {

    @NotNull
    private MaterialAidCategory category;

    @Valid
    @NotNull
    private Location location;

    @NotNull
    public final MaterialAidOffer.Type type = MaterialAidOffer.Type.MATERIAL_AID;

    public static MaterialAidOfferVM from(MaterialAidOffer offer, Language viewLang) {
        MaterialAidOfferVM result = new MaterialAidOfferVM();
        mapBase(offer, viewLang, result);
        result.category = offer.category;
        result.location = offer.location;
        return result;
    }
}
