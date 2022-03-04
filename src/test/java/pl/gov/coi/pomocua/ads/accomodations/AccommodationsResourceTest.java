package pl.gov.coi.pomocua.ads.accomodations;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import pl.gov.coi.pomocua.ads.BaseResourceTest;
import pl.gov.coi.pomocua.ads.PageableResponse;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccommodationsResourceTest extends BaseResourceTest<AccommodationOffer> {

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