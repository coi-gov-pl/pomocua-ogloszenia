package pl.gov.coi.pomocua.ads.other;

import lombok.Builder;
import pl.gov.coi.pomocua.ads.Location;

import java.util.Optional;

public class OtherTestDataGenerator {

    public static OtherOfferBuilder aOtherOffer() {
        return OtherTestDataGenerator.builder()
                .title("sample other offer")
                .description("sample other offer description")
                .location(new Location("Mazowiecie", "Warszawa"));
    }

    public static OtherOfferDefinitionDTO sampleUpdateJson() {
        var updateJson = new OtherOfferDefinitionDTO();
        updateJson.title = "new title";
        updateJson.description = "new description";
        updateJson.location = new Location("Pomorskie", "Gdańsk");
        return updateJson;
    }

    @Builder
    private static OtherOffer otherOfferBuilder(
            String title,
            String description,
            Location location,
            String phoneNumber
    ) {
        OtherOffer offer = new OtherOffer();
        offer.title = Optional.ofNullable(title).orElse("some title");
        offer.description = Optional.ofNullable(description).orElse("some description");
        offer.location = Optional.ofNullable(location).orElse(new Location("mazowieckie", "warszawa"));
        offer.phoneNumber = Optional.ofNullable(phoneNumber).orElse("48123456789");
        return offer;
    }
}
