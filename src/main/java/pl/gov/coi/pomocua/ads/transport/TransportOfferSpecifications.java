package pl.gov.coi.pomocua.ads.transport;

import org.springframework.data.jpa.domain.Specification;
import pl.gov.coi.pomocua.ads.Location;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class TransportOfferSpecifications {

    public static Specification<TransportOffer> from(TransportOfferSearchCriteria criteria) {
        List<Specification<TransportOffer>> specifications = new LinkedList<>();

        if (criteria.getOrigin() != null) {
            specifications.add(fromLocation("origin", criteria.getOrigin()));
        }
        if (criteria.getDestination() != null) {
            specifications.add(fromLocation("destination", criteria.getDestination()));
        }
        if (criteria.getCapacity() != null) {
            specifications.add(fromCapacity(criteria.getCapacity()));
        }
        if (criteria.getTransportDate() != null) {
            specifications.add(fromTransportDate(criteria.getTransportDate()));
        }
        return joinSpecifications(specifications);
    }

    private static Specification<TransportOffer> fromLocation(String fieldName, Location location) {
        List<Specification<TransportOffer>> specifications = new LinkedList<>();
        if (location.getCity() != null) {
            specifications.add((root, cq, cb) -> cb.equal(cb.lower(root.get(fieldName).get("city")), location.getCity().toLowerCase()));
        }
        if (location.getVoivodeship() != null) {
            specifications.add((root, cq, cb) -> cb.equal(cb.lower(root.get(fieldName).get("voivodeship")), location.getVoivodeship().toLowerCase()));
        }
        return joinSpecifications(specifications);
    }

    private static Specification<TransportOffer> fromCapacity(Integer capacity) {
        return (root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("capacity"), capacity);
    }

    private static Specification<TransportOffer> fromTransportDate(LocalDate transportDate) {
        return (root, cq, cb) -> cb.equal(root.get("transportDate"), transportDate);
    }

    private static Specification<TransportOffer> joinSpecifications(List<Specification<TransportOffer>> specifications) {
        return specifications.stream().reduce(Specification::and).orElse(null);
    }
}
