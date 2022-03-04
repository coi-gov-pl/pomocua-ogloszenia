package pl.gov.coi.pomocua.ads.accomodations;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import pl.gov.coi.pomocua.ads.BaseResourceTest;
import pl.gov.coi.pomocua.ads.PageableResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccommodationsResourceTest extends BaseResourceTest<AccommodationOffer> {

    @Test
    void shouldReturnOffersByFullCriteria() {
        AccommodationOffer response = postSampleOffer();

        String requestParams = "/MAzowIEckie/waRszaWA?noOfPeople=5";
        AccommodationOffer[] offers = listOffers(requestParams);

        assertThat(offers).contains(response);
    }

    @Test
    void shouldReturnOffersByCriteriaWithoutNoOfPeople() {
        AccommodationOffer response = postSampleOffer();

        String requestParams = "/mazowIEckie/WARszaWA";
        AccommodationOffer[] offers = listOffers(requestParams);

        assertThat(offers).contains(response);
    }

    @Test
    void shouldReturnOffersByNoOfPeopleOnly() {
        AccommodationOffer response = postSampleOffer();

        String requestParams = "?noOfPeople=1";
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
        request.city = "Warszawa";
        request.voivodeship = "Mazowieckie";
        request.hostLanguage = List.of(AccommodationOffer.Language.PL, AccommodationOffer.Language.UA);
        request.description = "description";
        request.lengthOfStay = AccommodationOffer.LengthOfStay.MONTH_2;
        request.guests = 5;
        return request;
    }
}