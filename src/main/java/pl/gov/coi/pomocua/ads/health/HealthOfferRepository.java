package pl.gov.coi.pomocua.ads.health;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import pl.gov.coi.pomocua.ads.UserId;

import java.util.Optional;

public interface HealthOfferRepository extends PagingAndSortingRepository<HealthOffer, Long>,
        JpaSpecificationExecutor<HealthOffer> {

    Optional<HealthOffer> findByIdAndUserId(Long id, UserId userId);
}
