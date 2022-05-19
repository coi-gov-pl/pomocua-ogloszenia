package pl.gov.coi.pomocua.ads.other;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import pl.gov.coi.pomocua.ads.UserId;

import java.util.Optional;

public interface OtherOfferRepository extends PagingAndSortingRepository<OtherOffer, Long>,
        JpaSpecificationExecutor<OtherOffer> {

    Optional<OtherOffer> findByIdAndUserId(Long id, UserId userId);
}
