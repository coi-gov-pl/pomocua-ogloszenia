package pl.gov.coi.pomocua.ads.translation;

import lombok.Builder;
import pl.gov.coi.pomocua.ads.Language;
import pl.gov.coi.pomocua.ads.Location;
import pl.gov.coi.pomocua.ads.translation.TranslationOffer.TranslationMode;

import java.util.List;
import java.util.Optional;

public class TranslationTestDataGenerator {

    public static TranslationOfferBuilder aTranslationOffer() {
        return TranslationTestDataGenerator.builder()
                .title("sample translation offer")
                .description("sample translation offer description")
                .location(new Location("Mazowiecie", "Warszawa"))
                .mode(List.of(TranslationMode.BY_EMAIL, TranslationMode.ONLINE))
                .language(List.of(Language.UA, Language.PL));
    }

    public static TranslationOfferDefinitionDTO sampleUpdateJson() {
        var updateJson = new TranslationOfferDefinitionDTO();
        updateJson.title = "new title";
        updateJson.description = "new description";
        updateJson.location = new Location("Pomorskie", "Gdańsk");
        updateJson.mode = List.of(TranslationMode.STATIONARY);
        updateJson.language = List.of(Language.RU, Language.PL);
        return updateJson;
    }

    @Builder
    private static TranslationOffer translationOfferBuilder(
            String title,
            String description,
            List<TranslationMode> mode,
            Location location,
            List<Language> language,
            String phoneNumber
    ) {
        TranslationOffer offer = new TranslationOffer();
        offer.title = Optional.ofNullable(title).orElse("some title");
        offer.description = Optional.ofNullable(description).orElse("some description");
        offer.setMode(Optional.ofNullable(mode).orElse(List.of(TranslationMode.BY_EMAIL, TranslationMode.BY_PHONE)));
        offer.location = Optional.ofNullable(location).orElse(new Location("mazowieckie", "warszawa"));
        offer.setLanguage(Optional.ofNullable(language).orElse(List.of(Language.PL, Language.UA)));
        offer.phoneNumber = Optional.ofNullable(phoneNumber).orElse("48123456789");
        return offer;
    }
}
