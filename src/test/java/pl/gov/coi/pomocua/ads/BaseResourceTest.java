package pl.gov.coi.pomocua.ads;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(CleanDatabaseExtension.class)
@Import(TestConfiguration.class)
public abstract class BaseResourceTest<T extends BaseOffer, V extends BaseOfferVM> {

    @Autowired
    protected TestRestTemplate restTemplate;
    @Autowired
    protected TestTimeProvider testTimeProvider;
    @Autowired
    protected TestUser testUser;
    @MockBean
    OffersTranslationUtil translationUtil;

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

            V response = postSampleOffer();

            T createdOffer = getOfferFromRepository(response.getId());
            assertThat(createdOffer.userId).isEqualTo(userId);
            assertThat(createdOffer.userFirstName).isEqualTo("John");
        }

        @Test
        void shouldSetActiveStatus() {
            V created = postSampleOffer();

            T createdEntity = getOfferFromRepository(created.getId());
            assertThat(createdEntity.status).isEqualTo(BaseOffer.Status.ACTIVE);
        }

        @Test
        void shouldReturnCreatedOfferOnList() {
            V response = postSampleOffer();

            var content = listOffers();
            assertThat(content).contains(response);
        }

        @Test
        void shouldReturnCreatedOffer() {
            V created = postSampleOffer();
            V returned = restTemplate.getForObject("/api/" + getOfferSuffix() + "/{id}", getClazz(), created.getId());
            assertThat(returned).isEqualTo(created);
        }

        @Test
        void shouldSetModifiedDateWhenOfferCreated() {
            testTimeProvider.setCurrentTime(Instant.parse("2022-03-05T14:20:00Z"));

            V created = postSampleOffer();

            Optional<T> createdEntity = getRepository().findById(created.getId());
            assertThat(createdEntity)
                    .isNotEmpty()
                    .get().extracting(e -> e.modifiedDate).isEqualTo(Instant.parse("2022-03-05T14:20:00Z"));
        }

        @Test
        void shouldIgnoreSuppliedIdOnCreate() {
            V request = sampleOfferRequest();
            request.setId(422L);
            V response = restTemplate.postForObject("/api/secure/" + getOfferSuffix(), request, getClazz());
            assertThat(response.getId()).isNotNull();
            assertThat(response.getId()).isNotEqualTo(request.getId());
        }

        @Nested
        class Validation {
            @Test
            void shouldRejectBlankTitle() {
                V offer = sampleOfferRequest();
                offer.setTitle("       ");
                assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
            }

            @ParameterizedTest
            @ValueSource(strings = {"<", ">", "(", ")", "%", "@", "\"", "'"})
            void shouldRejectIncorrectTitle(String notAllowedChar) {
                V offer = sampleOfferRequest();
                offer.setTitle("title" + notAllowedChar);
                assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
            }

            @Test
            void shouldRejectTooLongTitle() {
                V offer = sampleOfferRequest();
                offer.setTitle("x".repeat(100));
                assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
            }

            @Test
            void shouldRejectBlankDescription() {
                V offer = sampleOfferRequest();
                offer.setDescription("       ");
                assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
            }

            @ParameterizedTest
            @ValueSource(strings = {"<", ">", "%", "\"", "'"})
            void shouldRejectIncorrectDescription(String notAllowedChar) {
                V offer = sampleOfferRequest();
                offer.setDescription("description" + notAllowedChar);
                assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
            }

            @ParameterizedTest
            @ValueSource(strings = {"invalid phone", "+48 invalid phone", "0048123", "+48 123 123", "+48 123", "+48000000000", "0048123456"})
            void shouldRejectInvalidPhoneNumber(String invalidPhoneNumber) {
                V offer = sampleOfferRequest();
                offer.setPhoneNumber(invalidPhoneNumber);
                assertPostResponseStatus(offer, HttpStatus.BAD_REQUEST);
            }

            @Test
            void shouldAcceptMissingPhoneNumber() {
                V offer = sampleOfferRequest();
                offer.setPhoneNumber(null);
                assertPostResponseStatus(offer, HttpStatus.CREATED);
            }

            @ParameterizedTest
            @ValueSource(strings = {"+48123456789", "+48 123 456 789", "+48 123-456-789", "0048 123456789", "0048 (123) 456-789"})
            void shouldAcceptPhoneNumberInVariousFormats(String phoneNumber) {
                V offer = sampleOfferRequest();
                offer.setPhoneNumber(phoneNumber);
                assertPostResponseStatus(offer, HttpStatus.CREATED);
            }

            @ParameterizedTest
            @ValueSource(strings = {"+48123456789", "+48 123 456 789", "+48 123-456-789", "0048 123456789", "0048 (123) 456-789"})
            void shouldSaveNormalizedPhoneNumber(String phoneNumber) {
                V offer = sampleOfferRequest();
                offer.setPhoneNumber(phoneNumber);

                V createdOffer = postOffer(offer);

                T offerFromRepository = getOfferFromRepository(createdOffer.getId());
                assertThat(offerFromRepository.phoneNumber).isEqualTo("123456789");
                assertThat(offerFromRepository.phoneCountryCode).isEqualTo("48");
            }
        }
    }

    @Nested
    class FetchingSingle {
        @Test
        void shouldReturnSingleOffer() {
            V createdOffer = postSampleOffer();

            ResponseEntity<V> response = fetchOffer(createdOffer.getId());

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).usingRecursiveComparison().isEqualTo(createdOffer);
        }

        @Test
        void shouldReturn404WhenSingleOfferNotFound() {
            ResponseEntity<V> response = fetchOffer(123L);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        void shouldReturn404WhenOfferDeactivated() {
            V offer = postSampleOffer();
            deleteOffer(offer.getId());

            ResponseEntity<V> response = fetchOffer(offer.getId());

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        private ResponseEntity<V> fetchOffer(Long id) {
            return restTemplate.getForEntity("/api/%s/%d".formatted(getOfferSuffix(), id), getClazz());
        }
    }

    @Nested
    class Deactivating {
        @Test
        void shouldDeactivateOffer() {
            V offer = postSampleOffer();

            deleteOffer(offer.getId());

            T deactivatedOffer = getOfferFromRepository(offer.getId());
            assertThat(deactivatedOffer.status).isEqualTo(BaseOffer.Status.INACTIVE);
        }

        @Test
        void deactivatingIsIdempotent() {
            V created = postSampleOffer();
            deleteOffer(created.getId());

            ResponseEntity<Void> secondResponse = deleteOffer(created.getId());

            assertThat(secondResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        }

        @Test
        void shouldReturn404WhenTryingToDeactivateNonExistingOffer() {
            ResponseEntity<Void> response = deleteOffer(123L);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        void shouldReturn404WhenTryingToDeactivateOfferBelongingToAnotherUser() {
            testUser.setCurrentUserWithId(new UserId("user-1"));
            V offer = postSampleOffer();

            testUser.setCurrentUserWithId(new UserId("user-2"));
            ResponseEntity<Void> response = deleteOffer(offer.getId());

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            T notDeactivatedOffer = getOfferFromRepository(offer.getId());
            assertThat(notDeactivatedOffer.status).isEqualTo(BaseOffer.Status.ACTIVE);
        }
    }

    protected abstract Class<V> getClazz();

    protected abstract String getOfferSuffix();

    protected abstract V sampleOfferRequest();

    protected abstract CrudRepository<T, Long> getRepository();

    protected void assertPostResponseStatus(V offer, HttpStatus status) {
        String url = "/api/secure/" + getOfferSuffix();
        var response = status.is2xxSuccessful() ?
                restTemplate.postForEntity(url, offer, getClazz()) :
                restTemplate.postForEntity(url, offer, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(status);
    }

    protected OffersVM<V> listOffers(URI url) {
        var list = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<OffersVM<BaseOfferVM>>() {
                });
        return (OffersVM<V>) list.getBody();

    }

    protected List<V> listOffers(String requestParams) {
        return listOffers(URI.create("/api/" + getOfferSuffix() + requestParams)).content;
    }

    protected List<V> listOffers() {
        return listOffers("");
    }

    protected V postSampleOffer() {
        V request = sampleOfferRequest();
        return postOffer(request);
    }

    protected V postOffer(V request) {
        ResponseEntity<V> response = restTemplate.postForEntity("/api/secure/" + getOfferSuffix(), request, getClazz());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        V entity = response.getBody();
        assertThat(entity.getId()).isNotNull();
        assertThat(entity).usingRecursiveComparison()
                .ignoringFields("id", "modifiedDate", "userFirstName", "phoneNumber", "phoneCountryCode", "status")
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

    protected ResponseEntity<Void> deleteOffer(Long id) {
        return restTemplate.exchange("/api/secure/%s/%d".formatted(getOfferSuffix(), id), HttpMethod.DELETE, null, Void.class);
    }

    protected T getOfferFromRepository(Long id) {
        Optional<T> foundEntity = getRepository().findById(id);
        assertThat(foundEntity).isNotEmpty();
        return foundEntity.get();
    }
}