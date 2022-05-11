package pl.gov.coi.pomocua.ads.translation;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.gov.coi.pomocua.ads.BaseResourceTest;
import pl.gov.coi.pomocua.ads.Language;

import java.util.Collections;
import java.util.List;

import pl.gov.coi.pomocua.ads.translation.TranslationOffer.TranslationMode;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.gov.coi.pomocua.ads.translation.TranslationTestDataGenerator.aTranslationOffer;

public class TranslationOfferResourceTest extends BaseResourceTest<TranslationOffer> {

    @Autowired
    private TranslationOfferRepository repository;

    @Override
    protected Class<TranslationOffer> getClazz() {
        return TranslationOffer.class;
    }

    @Override
    protected String getOfferSuffix() {
        return "translation";
    }

    @Override
    protected TranslationOffer sampleOfferRequest() {
        return aTranslationOffer().build();
    }

    @Override
    protected CrudRepository<TranslationOffer, Long> getRepository() {
        return repository;
    }

    //TODO
    @Nested
    class Searching {

    }

    @Nested
    class ValidationTest {
        @Test
        void shouldAcceptNullLocation() {
            TranslationOffer offer = sampleOfferRequest();
            offer.location = null;
            assertPostResponseStatus(offer, HttpStatus.CREATED);
        }

        @Test
        void shouldRejectNullMode() {
            TranslationOffer offer = sampleOfferRequest();
            offer.setMode(null);
            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }

        @Test
        void shouldRejestEmptyMode() {
            TranslationOffer offer = sampleOfferRequest();
            offer.setMode(Collections.emptyList());
            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }

        @Test
        void shouldRejectNullLanguage() {
            TranslationOffer offer = sampleOfferRequest();
            offer.setLanguage(null);
            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }

        @Test
        void shouldRejectEmptyLanguage() {
            TranslationOffer offer = sampleOfferRequest();
            offer.setLanguage(Collections.emptyList());
            assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
        }
    }

    @Nested
    class Updating {
        @Test
        void shouldUpdateOffer() {
            TranslationOffer offer = postSampleOffer();
            var updateJson = TranslationTestDataGenerator.sampleUpdateJson();

            ResponseEntity<Void> response = updateOffer(offer.id, updateJson);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
            TranslationOffer updatedOffer = getOfferFromRepository(offer.id);
            assertThat(updatedOffer.title).isEqualTo("new title");
            assertThat(updatedOffer.description).isEqualTo("new description");
            assertThat(updatedOffer.getMode()).isEqualTo(List.of(TranslationMode.STATIONARY));
            assertThat(updatedOffer.location.region).isEqualTo("Pomorskie");
            assertThat(updatedOffer.location.city).isEqualTo("Gdańsk");
            assertThat(updatedOffer.getLanguage()).isEqualTo(List.of(Language.RU, Language.PL));
        }
    }
}
