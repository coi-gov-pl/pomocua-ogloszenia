package pl.gov.coi.pomocua.ads.transport;

import org.springframework.data.jpa.domain.Specification;
import pl.gov.coi.pomocua.ads.Location;

import java.util.LinkedList;
import java.util.List;

public class TransportOfferSpecifications {

    public static Specification<TransportOffer> from(TransportOfferSearchCriteria criteria) {
        List<Specification<TransportOffer>> specifications = new LinkedList<>();

        if (criteria.getOrigin() != null) {
            specifications.add(forLocation("origin", criteria.getOrigin()));
        }
        if (criteria.getDestination() != null) {
            specifications.add(forLocation("destination", criteria.getDestination()));
        }

        return joinSpecifications(specifications);
    }

    private static Specification<TransportOffer> forLocation(String fieldName, Location location) {
        List<Specification<TransportOffer>> specifications = new LinkedList<>();
        if (location.getCity() != null) {
            specifications.add((root, cq, cb) -> cb.equal(cb.lower(root.get(fieldName).get("city")), location.getCity().toLowerCase()));
        }
        if (location.getVoivodeship() != null) {
            specifications.add((root, cq, cb) -> cb.equal(cb.lower(root.get(fieldName).get("voivodeship")), location.getVoivodeship().toLowerCase()));
        }
        return joinSpecifications(specifications);
    }

    private static Specification<TransportOffer> joinSpecifications(List<Specification<TransportOffer>> specifications) {
        return specifications.stream().reduce(Specification::and).orElse(null);
    }
}
