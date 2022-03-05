package pl.gov.coi.pomocua.ads.materialaid;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialAidOfferRepository extends PagingAndSortingRepository<MaterialAidOffer, Long> {
}
