package pl.gov.coi.pomocua.ads.accomodations;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import pl.gov.coi.pomocua.ads.BaseResourceTest;
import pl.gov.coi.pomocua.ads.Location;
import pl.gov.coi.pomocua.ads.PageableResponse;
import pl.gov.coi.pomocua.ads.UserId;
import pl.gov.coi.pomocua.ads.authentication.TestCurrentUser;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class AccommodationsResourceTest extends BaseResourceTest<AccommodationOffer> {

    @Autowired
    private TestCurrentUser testCurrentUser;

    @Autowired
    private AccommodationsRepository repository;

    @Test
    void shouldReturnOffersByFullCriteria() {
        AccommodationOffer response = postSampleOffer();

        String requestParams = "/MAzowIEckie/waRszaWA?capacity=4";
        AccommodationOffer[] offers = listOffers(requestParams);

        assertThat(offers).contains(response);
    }

    @Test
    void shouldNotFindOffersForTooHighCapacity() {
        postSampleOffer();

        String requestParams = "/MAzowIEckie/waRszaWA?capacity=15";
        AccommodationOffer[] offers = listOffers(requestParams);

        assertThat(offers).isEmpty();
    }

    @Test
    void shouldNotFindOffersForWrongLocation() {
        postSampleOffer();

        String requestParams = "/MAłopolskie/Kraków?capacity=1";
        AccommodationOffer[] offers = listOffers(requestParams);

        assertThat(offers).isEmpty();
    }

    @Test
    void shouldReturnOffersByCriteriaWithoutCapacity() {
        AccommodationOffer response = postSampleOffer();

        String requestParams = "/mazowIEckie/WARszaWA";
        AccommodationOffer[] offers = listOffers(requestParams);

        assertThat(offers).contains(response);
    }

    @Test
    void shouldSetUserIdWhenStoreOfferInDB() {
        UserId userId = new UserId("user with accommodation offer");
        testCurrentUser.setCurrentUserId(userId);

        AccommodationOffer postedOffer = postSampleOffer();

        Optional<AccommodationOffer> storedOffer = repository.findById(postedOffer.id);

        assertThat(storedOffer).isNotEmpty();
        assertThat(storedOffer.get().userId).isEqualTo(userId);
    }

    @Test
    void shouldReturnOffersByCapacityOnly() {
        AccommodationOffer response = postSampleOffer();

        String requestParams = "?capacity=1";
        AccommodationOffer[] offers = listOffers(requestParams);

        assertThat(offers).contains(response);
    }

    private AccommodationOffer[] listOffers(String requestParams) {
        ResponseEntity<PageableResponse<AccommodationOffer>> list = restTemplate.exchange(
                "/api/" + getOfferSuffix() + requestParams, HttpMethod.GET, null, getResponseType()
        );
        return list.getBody().content;
    }

    @Override
    protected Class<AccommodationOffer> getClazz() {
        return AccommodationOffer.class;
    }

    @Override
    protected String getOfferSuffix() {
        return "accommodations";
    }

    @Override
    protected ParameterizedTypeReference<PageableResponse<AccommodationOffer>> getResponseType() {
        return new ParameterizedTypeReference<>() {
        };
    }

    @Override
    protected AccommodationOffer sampleOfferRequest() {
        AccommodationOffer request = new AccommodationOffer();
        request.title = "sample work";
        request.location = new Location("Mazowieckie", "Warszawa");
        request.hostLanguage = List.of(AccommodationOffer.Language.PL, AccommodationOffer.Language.UA);
        request.description = "description";
        request.lengthOfStay = AccommodationOffer.LengthOfStay.MONTH_2;
        request.guests = 5;
        request.modifiedDate = LocalDateTime.parse("2020-10-17T00:00");
        return request;
    }
}