package pl.gov.coi.pomocua.ads.materialaid;

import lombok.Builder;
import pl.gov.coi.pomocua.ads.Language;
import pl.gov.coi.pomocua.ads.Location;

import java.util.Optional;

public class MaterialAidTestDataGenerator {
    public static MaterialAidOfferVMBuilder aMaterialAidOffer() {
        return MaterialAidTestDataGenerator.builder()
                .title("sample work")
                .category(MaterialAidCategory.CLOTHING)
                .location(new Location("Mazowieckie", "Warszawa"))
                .description("description")
                .phoneNumber("+48123456789");
    }

    public static MaterialAidOfferDefinitionDTO sampleUpdateJson() {
        var updateJson = new MaterialAidOfferDefinitionDTO();
        updateJson.title = "new title";
        updateJson.description = "new description";
        updateJson.location = new Location("Pomorskie", "Gdańsk");
        updateJson.category = MaterialAidCategory.FOR_CHILDREN;
        updateJson.phoneNumber = "+48123456780";
        return updateJson;
    }

    @Builder
    private static MaterialAidOfferVM materialAidOfferVMBuilder(
            String title,
            String description,
            MaterialAidCategory category,
            Location location,
            String phoneNumber
    ) {
        MaterialAidOfferVM offer = new MaterialAidOfferVM();
        offer.setTitle(Optional.ofNullable(title).orElse("some title"));
        offer.setDescription(Optional.ofNullable(description).orElse("some description"));
        offer.setCategory(Optional.ofNullable(category).orElse(MaterialAidCategory.FOOD));
        offer.setLocation(Optional.ofNullable(location).orElse(new Location("mazowieckie", "warszawa")));
        offer.setPhoneNumber(Optional.ofNullable(phoneNumber).orElse("+48123456789"));
        return offer;
    }
}
