package pl.gov.coi.pomocua.ads.transport;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TransportOfferRepository extends PagingAndSortingRepository<TransportOffer, Long>, JpaSpecificationExecutor<TransportOffer> {
}
