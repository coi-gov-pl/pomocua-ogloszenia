package pl.gov.coi.pomocua.ads.materialaid;

import org.springframework.data.jpa.domain.Specification;
import pl.gov.coi.pomocua.ads.BaseOffer;
import pl.gov.coi.pomocua.ads.Location;
import pl.gov.coi.pomocua.ads.transport.TransportOffer;

import java.util.LinkedList;
import java.util.List;

public class MaterialAidOfferSpecifications {

    public static Specification<MaterialAidOffer> from(MaterialAidOfferSearchCriteria criteria) {
        List<Specification<MaterialAidOffer>> specifications = new LinkedList<>();

        specifications.add(onlyActive());
        if (criteria.getLocation() != null) {
            specifications.add(fromLocation(criteria.getLocation()));
        }
        if (criteria.getCategory() != null) {
            specifications.add(fromCategory(criteria.getCategory()));
        }
        return joinSpecifications(specifications);
    }

    private static Specification<MaterialAidOffer> onlyActive() {
        return (root, cq, cb) -> cb.equal(root.get("status"), BaseOffer.Status.ACTIVE);
    }

    private static Specification<MaterialAidOffer> fromLocation(Location location) {
        List<Specification<MaterialAidOffer>> specifications = new LinkedList<>();
        if (location.getCity() != null) {
            specifications.add((root, cq, cb) -> cb.equal(cb.lower(root.get("location").get("city")), location.getCity().toLowerCase()));
        }
        if (location.getRegion() != null) {
            specifications.add((root, cq, cb) -> cb.equal(cb.lower(root.get("location").get("region")), location.getRegion().toLowerCase()));
        }
        return joinSpecifications(specifications);
    }

    private static Specification<MaterialAidOffer> fromCategory(MaterialAidCategory category) {
        return (root, cq, cb) -> cb.equal(root.get("category"), category);
    }

    private static Specification<MaterialAidOffer> joinSpecifications(List<Specification<MaterialAidOffer>> specifications) {
        return specifications.stream().reduce(Specification::and).orElse(null);
    }
}
