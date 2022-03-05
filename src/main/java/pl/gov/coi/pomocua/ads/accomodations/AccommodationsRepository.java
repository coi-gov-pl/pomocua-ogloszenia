package pl.gov.coi.pomocua.ads.accomodations;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccommodationsRepository extends PagingAndSortingRepository<AccommodationOffer, Long> {

    Page<AccommodationOffer> findAllByLocation_RegionIgnoreCaseAndLocation_CityIgnoreCaseAndGuestsIsGreaterThanEqual(String region, String city, int guests, Pageable pageable);

    Page<AccommodationOffer> findAllByGuestsIsGreaterThanEqual(int guests, Pageable pageable);
}
