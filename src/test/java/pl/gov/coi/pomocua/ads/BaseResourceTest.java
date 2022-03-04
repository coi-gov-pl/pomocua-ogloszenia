package pl.gov.coi.pomocua.ads;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
<<<<<<< HEAD
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
=======
>>>>>>> 3cc7a2c ([POM-34] Make tests more generic)
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
<<<<<<< HEAD
public abstract class BaseResourceTest<T extends BaseOffer> {
=======
public class BaseResourceTest<T extends BaseOffer> {
>>>>>>> 3cc7a2c ([POM-34] Make tests more generic)

    @Autowired
    protected TestRestTemplate restTemplate;

    @Test
    void shouldCreateOffer() {
        assertPostResponseStatus(sampleOfferRequest(), HttpStatus.CREATED);
    }

    @Test
    void shouldReturnCreatedOfferOnList() {
        T response = postSampleOffer();

        T[] content = listOffers();
        assertThat(content).contains(response);
    }

    @Test
    void shouldReturnCreatedOffer() {
        T created = postSampleOffer();
        T returned = restTemplate.getForObject("/api/" + getOfferSuffix() + "/{id}", getClazz(), created.id);
        assertThat(returned).isEqualTo(created);
    }

    @Test
    void shouldRejectBlankTitle() {
        T offer = sampleOfferRequest();
        offer.title = "       ";
        assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldRejectTooLongTitle() {
        T offer = sampleOfferRequest();
        offer.title = "x".repeat(100);
        assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
    }

    private void assertPostResponseStatus(T offer, HttpStatus badRequest) {
        ResponseEntity<T> response = restTemplate.postForEntity("/api/secure/" + getOfferSuffix(), offer, getClazz());
        assertThat(response.getStatusCode()).isEqualTo(badRequest);
    }

    @Test
    void shouldIgnoreSuppliedIdOnCreate() {
        T request = sampleOfferRequest();
        request.id = 42L;
        T response = restTemplate.postForObject("/api/secure/" + getOfferSuffix(), request, getClazz());
        assertThat(response.id).isNotNull();
        assertThat(response.id).isNotEqualTo(request.id);
    }

<<<<<<< HEAD
    protected abstract Class<T> getClazz();

    protected abstract String getOfferSuffix();

    protected abstract ParameterizedTypeReference<PageableResponse<T>> getResponseType();

    protected abstract T sampleOfferRequest();

    private T[] listOffers() {
        ResponseEntity<PageableResponse<T>> list = restTemplate.exchange("/api/" + getOfferSuffix(), HttpMethod.GET, null,
                getResponseType());
        return list.getBody().content;
    }

    private T postSampleOffer() {
        T request = sampleOfferRequest();
        T response = restTemplate.postForObject("/api/secure/" + getOfferSuffix(), request, getClazz());
        assertThat(response.id).isNotNull();
        assertThat(response).usingRecursiveComparison().ignoringFields("id").isEqualTo(request);
        return response;
=======
    protected Class<T> getClazz() {
        return null;
    }

    protected String getOfferSuffix() {
        return null;
    }

    protected T[] listOffers() {
        return null;
    }

    protected T postSampleOffer() {
        return null;
    }

    protected T sampleOfferRequest() {
        return null;
>>>>>>> 3cc7a2c ([POM-34] Make tests more generic)
    }
}