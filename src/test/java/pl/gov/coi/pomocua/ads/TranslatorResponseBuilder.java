package pl.gov.coi.pomocua.ads;

import pl.gov.coi.pomocua.ads.translatorservice.TranslatorResponse;

import java.util.EnumMap;

public class TranslatorResponseBuilder {

    private static TranslatorResponse response;

    public TranslatorResponseBuilder(Language detectedLang) {
        this.response = new TranslatorResponse(detectedLang, new EnumMap<>(Language.class));
    }

    public TranslatorResponseBuilder withPlTranslation(String translation) {
        this.response.getTranslations().put(Language.PL, translation);
        return this;
    }

    public TranslatorResponseBuilder withEnTranslation(String translation) {
        this.response.getTranslations().put(Language.EN, translation);
        return this;
    }

    public TranslatorResponseBuilder withUaTranslation(String translation) {
        this.response.getTranslations().put(Language.UA, translation);
        return this;
    }

    public TranslatorResponseBuilder withRuTranslation(String translation) {
        this.response.getTranslations().put(Language.RU, translation);
        return this;
    }

    public TranslatorResponse build() {
        return response;
    }
}
