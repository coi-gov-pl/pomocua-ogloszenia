package pl.gov.coi.pomocua.ads.transport;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import pl.gov.coi.pomocua.ads.BaseOffer;
import pl.gov.coi.pomocua.ads.UserId;
import pl.gov.coi.pomocua.ads.accomodations.AccommodationOffer;

import java.util.Optional;

public interface TransportOfferRepository extends PagingAndSortingRepository<TransportOffer, Long>, JpaSpecificationExecutor<TransportOffer> {
    Optional<TransportOffer> findByIdAndUserId(Long id, UserId userId);

    Optional<TransportOffer> findByIdAndUserIdAndStatus(Long id, UserId userId, BaseOffer.Status status);
}
