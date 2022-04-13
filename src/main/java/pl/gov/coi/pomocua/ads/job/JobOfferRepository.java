package pl.gov.coi.pomocua.ads.job;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import pl.gov.coi.pomocua.ads.UserId;

import java.util.Optional;

public interface JobOfferRepository extends PagingAndSortingRepository<JobOffer, Long>,
        JpaSpecificationExecutor<JobOffer> {

    Optional<JobOffer> findByIdAndUserId(Long id, UserId userId);
}
