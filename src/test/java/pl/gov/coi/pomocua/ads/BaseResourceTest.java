package pl.gov.coi.pomocua.ads;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.gov.coi.pomocua.ads.authentication.TestCurrentUser;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestConfiguration.class)
public abstract class BaseResourceTest<T extends BaseOffer> {

    @Autowired
    protected TestRestTemplate restTemplate;
    @Autowired
    protected TestCurrentUser testCurrentUser;
    @Autowired
    private TestTimeProvider testTimeProvider;

    @AfterEach
    void tearDown() {
        testCurrentUser.setDefault();
        testTimeProvider.reset();
    }

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
    void shouldSetModifiedDateWhenOfferCreated() {
        setCurrentTime(Instant.parse("2022-03-05T14:20:00Z"));

        T created = postSampleOffer();

        Optional<T> createdEntity = getRepository().findById(created.id);
        assertThat(createdEntity)
                .isNotEmpty()
                .get().extracting(e -> e.modifiedDate).isEqualTo(Instant.parse("2022-03-05T14:20:00Z"));
    }

    @Test
    void shouldRejectBlankTitle() {
        T offer = sampleOfferRequest();
        offer.title = "       ";
        assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
    }

    @ParameterizedTest
    @ValueSource(strings = {"<", ">", "(", ")", "%", "#", "@", "\"", "'"})
    void shouldRejectIncorrectTitle(String notAllowedChar) {
        T offer = sampleOfferRequest();
        offer.title = "title" + notAllowedChar;
        assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldRejectTooLongTitle() {
        T offer = sampleOfferRequest();
        offer.title = "x".repeat(100);
        assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldRejectBlankDescription() {
        T offer = sampleOfferRequest();
        offer.description = "       ";
        assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
    }

    @ParameterizedTest
    @ValueSource(strings = {"<", ">", "(", ")", "%", "#", "@", "\"", "'"})
    void shouldRejectIncorrectDescription(String notAllowedChar) {
        T offer = sampleOfferRequest();
        offer.description = "description" + notAllowedChar;
        assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
    }

    protected void assertPostResponseStatus(T offer, HttpStatus status) {
        String url = "/api/secure/" + getOfferSuffix();
        var response = status.is2xxSuccessful() ?
                restTemplate.postForEntity(url, offer, getClazz()) :
                restTemplate.postForEntity(url, offer, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(status);
    }

    @Test
    void shouldIgnoreSuppliedIdOnCreate() {
        T request = sampleOfferRequest();
        request.id = 42L;
        T response = restTemplate.postForObject("/api/secure/" + getOfferSuffix(), request, getClazz());
        assertThat(response.id).isNotNull();
        assertThat(response.id).isNotEqualTo(request.id);
    }

    @Test
    void shouldReturnSingleOffer() {
        T createdOffer = postSampleOffer();

        ResponseEntity<T> response = restTemplate.getForEntity("/api/%s/%d".formatted(getOfferSuffix(), createdOffer.id), getClazz());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).usingRecursiveComparison().isEqualTo(createdOffer);
    }

    @Test
    void shouldReturn404WhenSingleOfferNotFound() {
        ResponseEntity<T> response = restTemplate.getForEntity("/api/%s/%d".formatted(getOfferSuffix(), 123L), getClazz());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    protected abstract Class<T> getClazz();

    protected abstract String getOfferSuffix();

    protected abstract ParameterizedTypeReference<PageableResponse<T>> getResponseType();

    protected abstract T sampleOfferRequest();

    protected abstract CrudRepository<T, Long> getRepository();

    private T[] listOffers() {
        ResponseEntity<PageableResponse<T>> list = restTemplate.exchange("/api/" + getOfferSuffix(), HttpMethod.GET, null,
                getResponseType());
        return list.getBody().content;
    }

    protected T postSampleOffer() {
        T request = sampleOfferRequest();
        return postOffer(request);
    }

    protected T postOffer(T request) {
        ResponseEntity<T> response = restTemplate.postForEntity("/api/secure/" + getOfferSuffix(), request, getClazz());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        T entity = response.getBody();
        assertThat(entity.id).isNotNull();
        assertThat(entity).usingRecursiveComparison().ignoringFields("id").isEqualTo(request);
        return entity;
    }

    protected void setCurrentTime(Instant time) {
        testTimeProvider.setClock(Clock.fixed(time, ZoneOffset.UTC));
    }
}