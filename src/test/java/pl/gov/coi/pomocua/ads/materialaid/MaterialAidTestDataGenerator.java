package pl.gov.coi.pomocua.ads.materialaid;

import lombok.Builder;
import pl.gov.coi.pomocua.ads.Location;

import java.util.Optional;

public class MaterialAidTestDataGenerator {
    public static MaterialAidOfferBuilder aMaterialAidOffer() {
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
    private static MaterialAidOffer materialAidOfferBuilder(
            String title,
            String description,
            MaterialAidCategory category,
            Location location,
            String phoneNumber
    ) {
        MaterialAidOffer offer = new MaterialAidOffer();
        offer.title = Optional.ofNullable(title).orElse("some title");
        offer.description = Optional.ofNullable(description).orElse("some description");
        offer.category = Optional.ofNullable(category).orElse(MaterialAidCategory.FOOD);
        offer.location = Optional.ofNullable(location).orElse(new Location("mazowieckie", "warszawa"));
        offer.phoneNumber = Optional.ofNullable(phoneNumber).orElse("+48123456789");
        return offer;
    }
}
