package pl.gov.coi.pomocua.ads.law;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import pl.gov.coi.pomocua.ads.UserId;

import java.util.Optional;

public interface LawOfferRepository extends PagingAndSortingRepository<LawOffer, Long>,
        JpaSpecificationExecutor<LawOffer> {

    Optional<LawOffer> findByIdAndUserId(Long id, UserId userId);
}
