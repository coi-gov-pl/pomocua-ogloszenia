package pl.gov.coi.pomocua.ads.accomodations;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import pl.gov.coi.pomocua.ads.BaseResourceTest;
import pl.gov.coi.pomocua.ads.UserId;
import pl.gov.coi.pomocua.ads.authentication.TestCurrentUser;

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
        var offers = listOffers(requestParams);

        assertThat(offers).contains(response);
    }

    @Test
    void shouldNotFindOffersForTooHighCapacity() {
        postSampleOffer();

        String requestParams = "/MAzowIEckie/waRszaWA?capacity=15";
        var offers = listOffers(requestParams);

        assertThat(offers).isEmpty();
    }

    @Test
    void shouldNotFindOffersForWrongLocation() {
        postSampleOffer();

        String requestParams = "/MAłopolskie/Kraków?capacity=1";
        var offers = listOffers(requestParams);

        assertThat(offers).isEmpty();
    }

    @Test
    void shouldReturnOffersByCriteriaWithoutCapacity() {
        AccommodationOffer response = postSampleOffer();

        String requestParams = "/mazowIEckie/WARszaWA";
        var offers = listOffers(requestParams);

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
        var offers = listOffers(requestParams);

        assertThat(offers).contains(response);
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
    protected AccommodationOffer sampleOfferRequest() {
        return AccommodationsTestDataGenerator.sampleOffer();
    }

    @Override
    protected CrudRepository<AccommodationOffer, Long> getRepository() {
        return repository;
    }
}
