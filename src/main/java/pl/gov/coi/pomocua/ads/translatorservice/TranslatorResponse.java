package pl.gov.coi.pomocua.ads.translatorservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.gov.coi.pomocua.ads.Language;

import java.util.EnumMap;

@Data
@AllArgsConstructor
public class TranslatorResponse {
    private Language detectedLanguage;
    private EnumMap<Language, String> translations;
}
