package pl.gov.coi.pomocua.ads.translations;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import pl.gov.coi.pomocua.ads.BaseResourceTest;
import pl.gov.coi.pomocua.ads.PageableResponse;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TranslationResourceTest extends BaseResourceTest<TranslationOffer> {

    @Override
    protected String getOfferSuffix() {
        return "translations";
    }

    @Override
    protected Class<TranslationOffer> getClazz() {
        return TranslationOffer.class;
    }

    @Override
    protected ParameterizedTypeReference<PageableResponse<TranslationOffer>> getResponseType() {
        return new ParameterizedTypeReference<>() {
        };
    }

    @Override
    protected TranslationOffer sampleOfferRequest() {
        TranslationOffer request = new TranslationOffer();
        request.title = "sample translation";
        request.mode = TranslationOffer.Mode.REMOTE;
        request.city = "Warszawa";
        request.voivodeship = "Mazowieckie";
        request.sworn = true;
        request.language = List.of(TranslationOffer.Language.PL, TranslationOffer.Language.UA);
        request.description = "description";
        return request;
    }
}