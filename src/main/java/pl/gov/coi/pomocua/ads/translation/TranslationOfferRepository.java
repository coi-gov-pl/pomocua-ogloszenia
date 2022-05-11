package pl.gov.coi.pomocua.ads.translation;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import pl.gov.coi.pomocua.ads.UserId;

import java.util.Optional;

public interface TranslationOfferRepository extends PagingAndSortingRepository<TranslationOffer, Long>,
        JpaSpecificationExecutor<TranslationOffer> {

    Optional<TranslationOffer> findByIdAndUserId(Long id, UserId userId);
}
