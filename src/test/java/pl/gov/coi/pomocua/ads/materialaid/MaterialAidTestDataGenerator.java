package pl.gov.coi.pomocua.ads.materialaid;

import pl.gov.coi.pomocua.ads.Location;

public class MaterialAidTestDataGenerator {
    public static MaterialAidOffer sampleOffer() {
        MaterialAidOffer request = new MaterialAidOffer();
        request.title = "sample work";
        request.category = MaterialAidCategory.CLOTHING;
        request.location = new Location("Mazowieckie", "Warszawa");
        request.description = "description";
        request.phoneNumber = "481234567890";
        return request;
    }

    public static MaterialAidOfferDefinitionDTO sampleUpdateJson() {
        var updateJson = new MaterialAidOfferDefinitionDTO();
        updateJson.title = "new title";
        updateJson.description = "new description";
        updateJson.location = new Location("Pomorskie", "Gdańsk");
        updateJson.category = MaterialAidCategory.FOR_CHILDREN;
        updateJson.phoneNumber = "481234567890";
        return updateJson;
    }
}
