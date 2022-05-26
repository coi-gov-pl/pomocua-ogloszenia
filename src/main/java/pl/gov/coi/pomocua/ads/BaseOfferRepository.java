package pl.gov.coi.pomocua.ads;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface BaseOfferRepository<T extends BaseOffer> extends PagingAndSortingRepository<T, Long>,
        JpaSpecificationExecutor<T> {

    Optional<T> findByIdAndUserId(Long id, UserId userId);
}
