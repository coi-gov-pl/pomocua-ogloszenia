package pl.gov.coi.pomocua.ads.translatorservice;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestOperations;
import pl.gov.coi.pomocua.ads.Language;

import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class TranslatorService {

    private final TranslatorServiceProperties properties;
    private final RestOperations restTemplate;

    public List<TranslatorResponse> translate(String... text) {
        String url = properties.getEndpoint() + "/translate?api-version=3.0&to=uk&to=pl&to=en&to=ru";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Ocp-Apim-Subscription-Key", properties.getKey());
        headers.add("Ocp-Apim-Subscription-Region", properties.getRegion());
        HttpEntity<List<TranslatorPayload>> request =
                new HttpEntity<>(
                        Stream.of(text).map(e -> new TranslatorPayload(e)).collect(Collectors.toList()),
                        headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            return mapTranslationResponse(response.getBody());
        } catch (RestClientResponseException e) {
            return Collections.emptyList();
        }
    }

    private List<TranslatorResponse> mapTranslationResponse(String jsonText) {
        List<TranslatorResponse> responseList = new LinkedList<>();
        if (StringUtils.hasText(jsonText)) {
            JsonElement jsonElement = JsonParser.parseString(jsonText);
            if (jsonElement.isJsonArray()) {
                responseList = mapResponseFromJsonArray(jsonElement.getAsJsonArray());
            }
        }
        return responseList;
    }

    private List<TranslatorResponse> mapResponseFromJsonArray(JsonArray jsonArray) {
        List<TranslatorResponse> responseList = new LinkedList<>();
        for (JsonElement e : jsonArray) {
            JsonObject jsonDetectedLanguage = e.getAsJsonObject().get("detectedLanguage").getAsJsonObject();
            String detectedLanguage = jsonDetectedLanguage.get("language").getAsString();
            EnumMap<Language, String> translationMap =
                    mapTranslationsFromJsonArray(e.getAsJsonObject().get("translations").getAsJsonArray(),
                            detectedLanguage);
            responseList.add(new TranslatorResponse(
                    Language.fromIsoCode(detectedLanguage),
                    translationMap));
        }
        return responseList;
    }

    private EnumMap<Language, String> mapTranslationsFromJsonArray(JsonArray jsonArray, String detectedLanguage) {
        EnumMap<Language, String> translationMap = new EnumMap<>(Language.class);
        for (JsonElement t : jsonArray) {
            String translationLanguage = t.getAsJsonObject().get("to").getAsString();
            if (!translationLanguage.equals(detectedLanguage)) {
                String translation = t.getAsJsonObject().get("text").getAsString();
                translationMap.put(Language.fromIsoCode(translationLanguage), translation);
            }
        }
        return translationMap;
    }

    @Data
    @AllArgsConstructor
    private class TranslatorPayload {
        private String text;
    }
}
