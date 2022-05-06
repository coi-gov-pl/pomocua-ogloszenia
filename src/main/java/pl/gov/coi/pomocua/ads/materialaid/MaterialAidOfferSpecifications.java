package pl.gov.coi.pomocua.ads.materialaid;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import pl.gov.coi.pomocua.ads.BaseOfferSpecifications;

import java.util.LinkedList;
import java.util.List;

@Component
public class MaterialAidOfferSpecifications extends BaseOfferSpecifications<MaterialAidOffer, MaterialAidOfferSearchCriteria> {

    @Override
    protected List<Specification<MaterialAidOffer>> fromOfferSpecific(MaterialAidOfferSearchCriteria criteria) {
        List<Specification<MaterialAidOffer>> specifications = new LinkedList<>();
        if (criteria.getLocation() != null) {
            specifications.add(fromLocation("location", criteria.getLocation()));
        }
        if (criteria.getCategory() != null) {
            specifications.add(fromCategory(criteria.getCategory()));
        }
        return specifications;
    }

    private static Specification<MaterialAidOffer> fromCategory(MaterialAidCategory category) {
        return (root, cq, cb) -> cb.equal(root.get("category"), category);
    }
}
