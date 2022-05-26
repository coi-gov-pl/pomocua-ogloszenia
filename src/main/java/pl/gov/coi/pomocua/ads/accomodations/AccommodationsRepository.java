package pl.gov.coi.pomocua.ads.accomodations;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.gov.coi.pomocua.ads.BaseOffer;
import pl.gov.coi.pomocua.ads.BaseOfferRepository;

public interface AccommodationsRepository extends BaseOfferRepository<AccommodationOffer> {

    Page<AccommodationOffer> findAllByLocation_RegionIgnoreCaseAndLocation_CityIgnoreCaseAndGuestsIsGreaterThanEqualAndStatus(
            String region,
            String city,
            int guests,
            BaseOffer.Status status,
            Pageable pageable
    );

    Page<AccommodationOffer> findAllByGuestsIsGreaterThanEqualAndStatus(
            int guests,
            BaseOffer.Status status,
            Pageable pageable
    );
}
