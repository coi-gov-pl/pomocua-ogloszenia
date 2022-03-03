package pl.gov.coi.pomocua.ads.translations;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import pl.gov.coi.pomocua.ads.BaseResourceTest;
import pl.gov.coi.pomocua.ads.PageableResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
    protected TranslationOffer[] listOffers() {
        ResponseEntity<PageableResponse<TranslationOffer>> list = restTemplate.exchange("/api/translations", HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {});
        return list.getBody().content;
    }

    @Override
    protected TranslationOffer postSampleOffer() {
        TranslationOffer request = sampleOfferRequest();
        TranslationOffer response = restTemplate.postForObject("/api/secure/translations", request, TranslationOffer.class);
        assertThat(response.id).isNotNull();
        assertThat(response).usingRecursiveComparison().ignoringFields("id").isEqualTo(request);
        return response;
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