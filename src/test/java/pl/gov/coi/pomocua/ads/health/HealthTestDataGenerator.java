package pl.gov.coi.pomocua.ads.health;

import lombok.Builder;

import pl.gov.coi.pomocua.ads.Language;
import pl.gov.coi.pomocua.ads.Location;
import pl.gov.coi.pomocua.ads.health.HealthOffer.HealthCareMode;

import java.util.List;
import java.util.Optional;

public class HealthTestDataGenerator {
    public static HealthOfferBuilder aHealthOffer() {
        return HealthTestDataGenerator.builder()
                .title("sample health offer")
                .description("sample health offer description")
                .location(new Location("Mazowieckie", "Warszawa"))
                .mode(List.of(HealthCareMode.IN_FACILITY, HealthCareMode.ONLINE))
                .specialization(HealthCareSpecialization.GENERAL);
    }

    public static HealthOfferDefinitionDTO sampleUpdateJson() {
        var updateJson = new HealthOfferDefinitionDTO();
        updateJson.title = "new title";
        updateJson.description = "new description";
        updateJson.location = new Location("Pomorskie", "Gdańsk");
        updateJson.mode = List.of(HealthCareMode.ONLINE);
        updateJson.specialization = HealthCareSpecialization.PEDIATRICS;
        updateJson.language = List.of(Language.PL, Language.EN);
        return updateJson;
    }

    @Builder
    private static HealthOffer healthOfferBuilder(
            String title,
            String description,
            List<HealthCareMode> mode,
            HealthCareSpecialization specialization,
            Location location,
            List<Language> language,
            String phoneNumber
    ) {
        HealthOffer offer = new HealthOffer();
        offer.title = Optional.ofNullable(title).orElse("some title");
        offer.description = Optional.ofNullable(description).orElse("some description");
        offer.setMode(Optional.ofNullable(mode).orElse(List.of(HealthCareMode.IN_FACILITY, HealthCareMode.ONLINE)));
        offer.specialization = Optional.ofNullable(specialization).orElse(HealthCareSpecialization.GENERAL);
        offer.location = Optional.ofNullable(location).orElse(new Location("mazowieckie", "warszawa"));
        offer.setLanguage(Optional.ofNullable(language).orElse(List.of(Language.PL, Language.UA)));
        offer.phoneNumber = Optional.ofNullable(phoneNumber).orElse("+48123456789");
        return offer;
    }
}
