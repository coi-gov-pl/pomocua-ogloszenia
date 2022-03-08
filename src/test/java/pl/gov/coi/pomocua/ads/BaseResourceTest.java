package pl.gov.coi.pomocua.ads;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.gov.coi.pomocua.ads.users.TestUser;
import pl.gov.coi.pomocua.ads.users.User;

import java.net.URI;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestConfiguration.class)
public abstract class BaseResourceTest<T extends BaseOffer> {

    @Autowired
    protected TestRestTemplate restTemplate;
    @Autowired
    protected TestTimeProvider testTimeProvider;
    @Autowired
    protected TestUser testUser;

    @Autowired
    private Collection<CrudRepository<?, ?>> repositories;

    @BeforeEach
    void clearDatabase() {
        repositories.forEach(CrudRepository::deleteAll);
    }

    @AfterEach
    void tearDown() {
        testUser.setDefault();
        testTimeProvider.reset();
    }

    @Nested
    class Creating {
        @Test
        void shouldCreateOffer() {
            assertPostResponseStatus(sampleOfferRequest(), HttpStatus.CREATED);
        }

        @Test
        void shouldSaveUserData() {
            UserId userId = new UserId("some-current-id");
            testUser.setCurrentUser(new User(userId, "some@email.com", "John"));

            T response = postSampleOffer();

            T createdOffer = getOfferFromRepository(response.id);
            assertThat(createdOffer.userId).isEqualTo(userId);
            assertThat(createdOffer.userFirstName).isEqualTo("John");
        }

        @Test
        void shouldReturnCreatedOfferOnList() {
            T response = postSampleOffer();

            var content = listOffers();
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
            testTimeProvider.setCurrentTime(Instant.parse("2022-03-05T14:20:00Z"));

            T created = postSampleOffer();

            Optional<T> createdEntity = getRepository().findById(created.id);
            assertThat(createdEntity)
                    .isNotEmpty()
                    .get().extracting(e -> e.modifiedDate).isEqualTo(Instant.parse("2022-03-05T14:20:00Z"));
        }

        @Test
        void shouldIgnoreSuppliedIdOnCreate() {
            T request = sampleOfferRequest();
            request.id = 42L;
            T response = restTemplate.postForObject("/api/secure/" + getOfferSuffix(), request, getClazz());
            assertThat(response.id).isNotNull();
            assertThat(response.id).isNotEqualTo(request.id);
        }

        @Nested
        class Validation {
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
        }
    }

    @Nested
    class FetchingSingle {
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
    }

    @Test
    void shouldReturn404OnGetDeletedOffer() {
        T created = postSampleOffer();
        restTemplate.delete("/api/secure/" + getOfferSuffix() + "/{id}", getClazz(), created.id);
        ResponseEntity<T> response = restTemplate.getForEntity("/api/%s/%d".formatted(getOfferSuffix(), 123L), getClazz());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldDeactivateOfferButNotDelete() {
        T created = postSampleOffer();
        Optional<T> createdEntity = getRepository().findById(created.id);
        assertThat(createdEntity)
                .isNotEmpty()
                .get().extracting(e -> e.status).isEqualTo(BaseOffer.Status.ACTIVE);

        restTemplate.delete("/api/secure/" + getOfferSuffix() + "/" + created.id, getClazz());

        createdEntity = getRepository().findById(created.id);
        assertThat(createdEntity)
                .isNotEmpty()
                .get().extracting(e -> e.status).isEqualTo(BaseOffer.Status.INACTIVE);

    }

    @Test
    void shouldReturn204OnDoubleDeletedOffer() {
        T created = postSampleOffer();
        restTemplate.delete("/api/secure/" + getOfferSuffix() + "/{id}", getClazz(), created.id);

        restTemplate.delete("/api/secure/" + getOfferSuffix() + "/{id}", getClazz(), created.id);
    }

    protected abstract Class<T> getClazz();

    protected abstract String getOfferSuffix();

    protected abstract T sampleOfferRequest();

    protected abstract CrudRepository<T, Long> getRepository();

    protected void assertPostResponseStatus(T offer, HttpStatus status) {
        String url = "/api/secure/" + getOfferSuffix();
        var response = status.is2xxSuccessful() ?
                restTemplate.postForEntity(url, offer, getClazz()) :
                restTemplate.postForEntity(url, offer, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(status);
    }

    protected Offers<T> listOffers(URI url) {
        var list = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Offers<BaseOffer>>() {
                });
        return (Offers<T>) list.getBody();

    }

    protected List<T> listOffers(String requestParams) {
        return listOffers(URI.create("/api/" + getOfferSuffix() + requestParams)).content;
    }

    protected List<T> listOffers() {
        return listOffers("");
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
        assertThat(entity).usingRecursiveComparison()
                .ignoringFields("id", "modifiedDate", "userFirstName")
                .isEqualTo(request);
        return entity;
    }

    protected <U> ResponseEntity<Void> updateOffer(Long id, U update) {
        return restTemplate.exchange(
                "/api/secure/%s/%d".formatted(getOfferSuffix(), id),
                HttpMethod.PUT,
                new HttpEntity<>(update),
                Void.class
        );
    }

    protected T getOfferFromRepository(Long id) {
        Optional<T> foundEntity = getRepository().findById(id);
        assertThat(foundEntity).isNotEmpty();
        return foundEntity.get();
    }

}