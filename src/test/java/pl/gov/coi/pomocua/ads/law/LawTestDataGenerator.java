package pl.gov.coi.pomocua.ads.law;

import lombok.Builder;
import pl.gov.coi.pomocua.ads.Language;
import pl.gov.coi.pomocua.ads.Location;
import pl.gov.coi.pomocua.ads.law.LawOffer.HelpKind;
import pl.gov.coi.pomocua.ads.law.LawOffer.HelpMode;

import java.util.List;
import java.util.Optional;

public class LawTestDataGenerator {
    public static LawOfferVMBuilder aLawOffer() {
        return LawTestDataGenerator.builder()
                .title("sample law offer")
                .description("sample law description")
                .location(new Location("Mazowieckie", "Warszawa"))
                .helpMode(List.of(HelpMode.ONLINE))
                .helpKind(List.of(HelpKind.IMMIGRATION_LAW, HelpKind.FAMILY_LAW));
    }

    public static LawOfferDefinitionDTO sampleUpdateJson() {
        var updateJson = new LawOfferDefinitionDTO();
        updateJson.title = "new title";
        updateJson.description = "new description";
        updateJson.location = new Location("Pomorskie", "Gdańsk");
        updateJson.helpMode = List.of(HelpMode.ONLINE);
        updateJson.helpKind = List.of(HelpKind.IMMIGRATION_LAW, HelpKind.FAMILY_LAW);
        updateJson.language = List.of(Language.PL, Language.EN);
        return updateJson;
    }

    @Builder
    private static LawOfferVM lawOfferVMBuilder(
            String title,
            String description,
            List<HelpMode> helpMode,
            List<HelpKind> helpKind,
            Location location,
            List<Language> language,
            String phoneNumber
    ) {
        LawOfferVM offer = new LawOfferVM();
        offer.setTitle(Optional.ofNullable(title).orElse("some title"));
        offer.setDescription(Optional.ofNullable(description).orElse("some description"));
        offer.setHelpMode(Optional.ofNullable(helpMode).orElse(List.of(HelpMode.ONLINE)));
        offer.setHelpKind(Optional.ofNullable(helpKind).orElse(List.of(HelpKind.IMMIGRATION_LAW)));
        offer.setLocation(Optional.ofNullable(location).orElse(new Location("mazowieckie", "warszawa")));
        offer.setLanguage(Optional.ofNullable(language).orElse(List.of(Language.PL, Language.UA)));
        offer.setPhoneNumber(Optional.ofNullable(phoneNumber).orElse("+48123456789"));
        return offer;
    }
}
