package pl.gov.coi.pomocua.ads.materialaid;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.gov.coi.pomocua.ads.UserId;

import java.util.Optional;

@Repository
public interface MaterialAidOfferRepository
        extends PagingAndSortingRepository<MaterialAidOffer, Long>, JpaSpecificationExecutor<MaterialAidOffer> {

    Optional<MaterialAidOffer> findByIdAndUserId(Long id, UserId currentUserId);
}
