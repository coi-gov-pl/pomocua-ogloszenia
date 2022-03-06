package pl.gov.coi.pomocua.ads.materialaid;

import pl.gov.coi.pomocua.ads.Location;

public class MaterialAidTestDataGenerator {
    public static MaterialAidOffer sampleOffer() {
        MaterialAidOffer request = new MaterialAidOffer();
        request.title = "sample work";
        request.category = MaterialAidCategory.CLOTHING;
        request.location = new Location("Mazowieckie", "Warszawa");
        request.description = "description";
        return request;
    }
}
