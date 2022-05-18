package pl.gov.coi.pomocua.ads.transport;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import pl.gov.coi.pomocua.ads.BaseOfferSpecifications;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Component
public class TransportOfferSpecifications extends BaseOfferSpecifications<TransportOffer, TransportOfferSearchCriteria> {

    @Override
    protected List<Specification<TransportOffer>> fromOfferSpecific(TransportOfferSearchCriteria criteria) {
        List<Specification<TransportOffer>> specifications = new LinkedList<>();
        if (criteria.getOrigin() != null) {
            specifications.add(fromLocationNullable("origin", criteria.getOrigin()));
        }
        if (criteria.getDestination() != null) {
            specifications.add(fromLocationNullable("destination", criteria.getDestination()));
        }
        if (criteria.getCapacity() != null) {
            specifications.add(fromCapacity(criteria.getCapacity()));
        }
        if (criteria.getTransportDate() != null) {
            specifications.add(fromTransportDate(criteria.getTransportDate()));
        }
        return specifications;
    }

    private static Specification<TransportOffer> fromCapacity(Integer capacity) {
        return (root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("capacity"), capacity);
    }

    private static Specification<TransportOffer> fromTransportDate(LocalDate transportDate) {
        return (root, cq, cb) ->
                cb.or(cb.isNull(root.get("transportDate")), cb.equal(root.get("transportDate"), transportDate));
    }
}
