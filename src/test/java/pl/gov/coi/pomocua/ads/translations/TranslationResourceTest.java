package pl.gov.coi.pomocua.ads.translations;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.gov.coi.pomocua.ads.PageableResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TranslationResourceTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldCreateTranslationOffer() {
        assertPostResponseStatus(sampleTranslationOfferRequest(), HttpStatus.CREATED);
    }

    @Test
    void shouldReturnCreatedTranslationOfferOnList() {
        TranslationOffer response = postSampleTranslationOffer();

        TranslationOffer[] content = listTranslationOffer();
        assertThat(content).contains(response);
    }

    @Test
    void shouldReturnCreatedTranslationOffer() {
        TranslationOffer created = postSampleTranslationOffer();
        TranslationOffer returned = restTemplate.getForObject("/api/translations/{id}", TranslationOffer.class, created.id);
        assertThat(returned).isEqualTo(created);
    }

    @Test
    void shouldRejectBlankTitle() {
        TranslationOffer translationOffer = sampleTranslationOfferRequest();
        translationOffer.title = "       ";
        assertPostResponseStatus(translationOffer, HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldRejectTooLongTitle() {
        TranslationOffer translationOffer = sampleTranslationOfferRequest();
        translationOffer.title = "x".repeat(100);
        assertPostResponseStatus(translationOffer, HttpStatus.BAD_REQUEST);
    }

    private void assertPostResponseStatus(TranslationOffer translationOffer, HttpStatus badRequest) {
        ResponseEntity<TranslationOffer> response = restTemplate.postForEntity("/api/secure/translations", translationOffer, TranslationOffer.class);
        assertThat(response.getStatusCode()).isEqualTo(badRequest);
    }

    @Test
    void shouldIgnoreSuppliedIdOnCreate() {
        TranslationOffer request = sampleTranslationOfferRequest();
        request.id = 42L;
        TranslationOffer response = restTemplate.postForObject("/api/secure/translations", request, TranslationOffer.class);
        assertThat(response.id).isNotNull();
        assertThat(response.id).isNotEqualTo(request.id);
    }

    private TranslationOffer[] listTranslationOffer() {
        ResponseEntity<PageableResponse<TranslationOffer>> list = restTemplate.exchange("/api/translations", HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {});
        return list.getBody().content;
    }

    private TranslationOffer postSampleTranslationOffer() {
        TranslationOffer request = sampleTranslationOfferRequest();
        TranslationOffer response = restTemplate.postForObject("/api/secure/translations", request, TranslationOffer.class);
        assertThat(response.id).isNotNull();
        assertThat(response).usingRecursiveComparison().ignoringFields("id").isEqualTo(request);
        return response;
    }

    private TranslationOffer sampleTranslationOfferRequest() {
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