package pl.gov.coi.pomocua.ads;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.gov.coi.pomocua.ads.job.JobOffer;
import pl.gov.coi.pomocua.ads.myoffers.MyOffersRepository;
import pl.gov.coi.pomocua.ads.translatorservice.TranslatorResponse;
import pl.gov.coi.pomocua.ads.translatorservice.TranslatorService;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OffersTranslationUtilTest {

    private MyOffersRepository myOffersRepository;

    private TranslatorService translatorService;

    private OffersTranslationUtil translationUtil;

    private final static String TITLE_EN = "some title";
    private final static String TITLE_PL = "some title in PL";
    private final static String TITLE_UA = "some title in UA";
    private final static String TITLE_RU = "some title in RU";
    private final static String DESCRIPTION_EN = "some description";
    private final static String DESCRIPTION_PL = "some description in PL";
    private final static String DESCRIPTION_UA = "some description in UA";
    private final static String DESCRIPTION_RU = "some description in RU";

    @BeforeEach
    void setUp() {
        myOffersRepository = mock(MyOffersRepository.class);
        translatorService = mock(TranslatorService.class);
        translationUtil = new OffersTranslationUtil(myOffersRepository, translatorService);
    }

    @Test
    @DisplayName("Should not translate, empty response from translator")
    void test_withErrors() {
        JobOffer offer = sampleJobOffer(TITLE_PL, DESCRIPTION_PL);
        when(translatorService.translate(anyString(), anyString())).thenReturn(Collections.emptyList());
        translationUtil.translateOffer(offer);
        assertThat(offer.detectedLanguage).isNull();
        assertThat(offer.translationErrorCounter).isEqualTo((byte)1);
        assertThat(offer.title).isEqualTo(TITLE_PL);
        assertThat(offer.description).isEqualTo(DESCRIPTION_PL);
        assertThat(offer.titleEn).isNull();
        assertThat(offer.titleUa).isNull();
        assertThat(offer.titleRu).isNull();
        assertThat(offer.descriptionEn).isNull();
        assertThat(offer.descriptionUa).isNull();
        assertThat(offer.descriptionRu).isNull();
    }

    @Test
    @DisplayName("Should translate, EN detected for both")
    void test_noErrorsDetectedEn() {
        JobOffer offer = sampleJobOffer(TITLE_EN, DESCRIPTION_EN);
        List<TranslatorResponse> translatorResponseList = List.of(
                new TranslatorResponseBuilder(Language.EN)
                        .withEnTranslation(TITLE_EN)
                        .withPlTranslation(TITLE_PL)
                        .withUaTranslation(TITLE_UA)
                        .withRuTranslation(TITLE_RU)
                        .build(),
                new TranslatorResponseBuilder(Language.EN)
                        .withEnTranslation(DESCRIPTION_EN)
                        .withPlTranslation(DESCRIPTION_PL)
                        .withUaTranslation(DESCRIPTION_UA)
                        .withRuTranslation(DESCRIPTION_RU)
                        .build());
        when(translatorService.translate(anyString(), anyString())).thenReturn(translatorResponseList);
        translationUtil.translateOffer(offer);

        assertThat(offer.detectedLanguage).isEqualTo(Language.EN);
        assertThat(offer.translationErrorCounter).isEqualTo((byte)0);
        assertOfferTranslations(offer);
    }

    @Test
    @DisplayName("Should translate, PL detected for both")
    void test_noErrorsDetectedPl() {
        JobOffer offer = sampleJobOffer(TITLE_PL, DESCRIPTION_PL);
        List<TranslatorResponse> translatorResponseList = List.of(
                new TranslatorResponseBuilder(Language.PL)
                        .withEnTranslation(TITLE_EN)
                        .withPlTranslation(TITLE_PL)
                        .withUaTranslation(TITLE_UA)
                        .withRuTranslation(TITLE_RU)
                        .build(),
                new TranslatorResponseBuilder(Language.PL)
                        .withEnTranslation(DESCRIPTION_EN)
                        .withPlTranslation(DESCRIPTION_PL)
                        .withUaTranslation(DESCRIPTION_UA)
                        .withRuTranslation(DESCRIPTION_RU)
                        .build());
        when(translatorService.translate(anyString(), anyString())).thenReturn(translatorResponseList);
        translationUtil.translateOffer(offer);

        assertThat(offer.detectedLanguage).isEqualTo(Language.PL);
        assertThat(offer.translationErrorCounter).isEqualTo((byte)0);
        assertOfferTranslations(offer);
    }

    @Test
    @DisplayName("Should translate, UA detected for both")
    void test_noErrorsDetectedUa() {
        JobOffer offer = sampleJobOffer(TITLE_UA, DESCRIPTION_UA);
        List<TranslatorResponse> translatorResponseList = List.of(
                new TranslatorResponseBuilder(Language.UA)
                        .withEnTranslation(TITLE_EN)
                        .withPlTranslation(TITLE_PL)
                        .withUaTranslation(TITLE_UA)
                        .withRuTranslation(TITLE_RU)
                        .build(),
                new TranslatorResponseBuilder(Language.UA)
                        .withEnTranslation(DESCRIPTION_EN)
                        .withPlTranslation(DESCRIPTION_PL)
                        .withUaTranslation(DESCRIPTION_UA)
                        .withRuTranslation(DESCRIPTION_RU)
                        .build());
        when(translatorService.translate(anyString(), anyString())).thenReturn(translatorResponseList);
        translationUtil.translateOffer(offer);

        assertThat(offer.detectedLanguage).isEqualTo(Language.UA);
        assertThat(offer.translationErrorCounter).isEqualTo((byte)0);
        assertOfferTranslations(offer);
    }

    @Test
    @DisplayName("Should translate, RU detected for both")
    void test_noErrorsDetectedRu() {
        JobOffer offer = sampleJobOffer(TITLE_RU, DESCRIPTION_RU);
        List<TranslatorResponse> translatorResponseList = List.of(
                new TranslatorResponseBuilder(Language.RU)
                        .withEnTranslation(TITLE_EN)
                        .withPlTranslation(TITLE_PL)
                        .withUaTranslation(TITLE_UA)
                        .withRuTranslation(TITLE_RU)
                        .build(),
                new TranslatorResponseBuilder(Language.RU)
                        .withEnTranslation(DESCRIPTION_EN)
                        .withPlTranslation(DESCRIPTION_PL)
                        .withUaTranslation(DESCRIPTION_UA)
                        .withRuTranslation(DESCRIPTION_RU)
                        .build());
        when(translatorService.translate(anyString(), anyString())).thenReturn(translatorResponseList);
        translationUtil.translateOffer(offer);

        assertThat(offer.detectedLanguage).isEqualTo(Language.RU);
        assertThat(offer.translationErrorCounter).isEqualTo((byte)0);
        assertOfferTranslations(offer);
    }

    @Test
    @DisplayName("Should translate, different language detected")
    void test_noErrorsDifferentLanguage() {
        JobOffer offer = sampleJobOffer(TITLE_PL, DESCRIPTION_EN);
        List<TranslatorResponse> translatorResponseList = List.of(
                new TranslatorResponseBuilder(Language.PL)
                        .withEnTranslation(TITLE_EN)
                        .withPlTranslation(TITLE_PL)
                        .withUaTranslation(TITLE_UA)
                        .withRuTranslation(TITLE_RU)
                        .build(),
                new TranslatorResponseBuilder(Language.EN)
                        .withEnTranslation(DESCRIPTION_EN)
                        .withPlTranslation(DESCRIPTION_PL)
                        .withUaTranslation(DESCRIPTION_UA)
                        .withRuTranslation(DESCRIPTION_RU)
                        .build());
        when(translatorService.translate(anyString(), anyString())).thenReturn(translatorResponseList);
        translationUtil.translateOffer(offer);

        assertThat(offer.detectedLanguage).isEqualTo(Language.EN);
        assertThat(offer.translationErrorCounter).isEqualTo((byte)0);
        assertOfferTranslations(offer);
    }

    @Test
    @DisplayName("Should translate, PL detected for both, not zero error counter")
    void test_noErrorsNotZeroErrorCounter() {
        JobOffer offer = sampleJobOffer(TITLE_PL, DESCRIPTION_PL);
        offer.translationErrorCounter++;
        List<TranslatorResponse> translatorResponseList = List.of(
                new TranslatorResponseBuilder(Language.PL)
                        .withEnTranslation(TITLE_EN)
                        .withPlTranslation(TITLE_PL)
                        .withUaTranslation(TITLE_UA)
                        .withRuTranslation(TITLE_RU)
                        .build(),
                new TranslatorResponseBuilder(Language.PL)
                        .withEnTranslation(DESCRIPTION_EN)
                        .withPlTranslation(DESCRIPTION_PL)
                        .withUaTranslation(DESCRIPTION_UA)
                        .withRuTranslation(DESCRIPTION_RU)
                        .build());
        when(translatorService.translate(anyString(), anyString())).thenReturn(translatorResponseList);
        translationUtil.translateOffer(offer);

        assertThat(offer.detectedLanguage).isEqualTo(Language.PL);
        assertThat(offer.translationErrorCounter).isEqualTo((byte)0);
        assertOfferTranslations(offer);
    }

    private void assertOfferTranslations(JobOffer offer) {
        assertThat(offer.title).isEqualTo(TITLE_PL);
        assertThat(offer.titleEn).isEqualTo(TITLE_EN);
        assertThat(offer.titleUa).isEqualTo(TITLE_UA);
        assertThat(offer.titleRu).isEqualTo(TITLE_RU);
        assertThat(offer.description).isEqualTo(DESCRIPTION_PL);
        assertThat(offer.descriptionEn).isEqualTo(DESCRIPTION_EN);
        assertThat(offer.descriptionUa).isEqualTo(DESCRIPTION_UA);
        assertThat(offer.descriptionRu).isEqualTo(DESCRIPTION_RU);
    }

    private JobOffer sampleJobOffer(String title, String description) {
        JobOffer offer = new JobOffer();
        offer.title = title;
        offer.description = description;
        offer.mode = JobOffer.Mode.ONSITE;
        offer.industry = JobOffer.Industry.BANKING;
        offer.setWorkTime(List.of(JobOffer.WorkTime.FULL_TIME));
        offer.setContractType(List.of(JobOffer.ContractType.EMPLOYMENT));
        offer.setLanguage(List.of(Language.PL));
        return offer;
    }
}
