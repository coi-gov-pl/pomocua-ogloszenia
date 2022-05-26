package pl.gov.coi.pomocua.ads.other;

import lombok.Builder;
import pl.gov.coi.pomocua.ads.Language;
import pl.gov.coi.pomocua.ads.Location;

import java.util.Optional;

public class OtherTestDataGenerator {

    public static OtherOfferVMBuilder aOtherOffer() {
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
    private static OtherOfferVM otherOfferVMBuilder(
            String title,
            String description,
            Location location,
            String phoneNumber
    ) {
        OtherOfferVM offer = new OtherOfferVM();
        offer.setTitle(Optional.ofNullable(title).orElse("some title"));
        offer.setDescription(Optional.ofNullable(description).orElse("some description"));
        offer.setLocation(Optional.ofNullable(location).orElse(new Location("mazowieckie", "warszawa")));
        offer.setPhoneNumber(Optional.ofNullable(phoneNumber).orElse("48123456789"));
        return offer;
    }
}
