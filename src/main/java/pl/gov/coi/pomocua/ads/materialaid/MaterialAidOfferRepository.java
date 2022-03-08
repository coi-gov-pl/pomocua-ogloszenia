package pl.gov.coi.pomocua.ads.materialaid;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import pl.gov.coi.pomocua.ads.BaseOffer;
import pl.gov.coi.pomocua.ads.UserId;

import java.util.Optional;


public interface MaterialAidOfferRepository
        extends PagingAndSortingRepository<MaterialAidOffer, Long>, JpaSpecificationExecutor<MaterialAidOffer> {

    Optional<MaterialAidOffer> findByIdAndUserId(Long id, UserId userId);

    Optional<MaterialAidOffer> findByIdAndUserIdAndStatus(Long id, UserId userId, BaseOffer.Status status);

}
