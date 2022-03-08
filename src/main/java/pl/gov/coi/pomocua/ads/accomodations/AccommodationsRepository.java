package pl.gov.coi.pomocua.ads.accomodations;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import pl.gov.coi.pomocua.ads.BaseOffer;
import pl.gov.coi.pomocua.ads.UserId;

import java.util.Optional;

public interface AccommodationsRepository extends PagingAndSortingRepository<AccommodationOffer, Long> {

    Optional<AccommodationOffer> findByIdAndUserId(Long id, UserId userId);

    Optional<AccommodationOffer> findByIdAndUserIdAndStatus(Long id, UserId userId, BaseOffer.Status status);

    Page<AccommodationOffer> findAllByLocation_RegionIgnoreCaseAndLocation_CityIgnoreCaseAndGuestsIsGreaterThanEqual(
            String region,
            String city,
            int guests,
            Pageable pageable
    );

    Page<AccommodationOffer> findAllByGuestsIsGreaterThanEqual(
            int guests,
            Pageable pageable
    );
}
