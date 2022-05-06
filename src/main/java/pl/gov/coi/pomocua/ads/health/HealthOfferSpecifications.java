package pl.gov.coi.pomocua.ads.health;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import pl.gov.coi.pomocua.ads.BaseOfferSpecifications;
import pl.gov.coi.pomocua.ads.health.HealthOffer.HealthCareMode;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Component
public class HealthOfferSpecifications extends BaseOfferSpecifications<HealthOffer, HealthOfferSearchCriteria> {

    @Override
    protected List<Specification<HealthOffer>> fromOfferSpecific(HealthOfferSearchCriteria criteria) {
        List<Specification<HealthOffer>> specifications = new LinkedList<>();
        if (criteria.getLocation() != null) {
            specifications.add(fromLocationNullable("location", criteria.getLocation()));
        }
        if (criteria.getSpecialization() != null) {
            specifications.add(fromSpecialization(criteria.getSpecialization()));
        }
        if (!CollectionUtils.isEmpty(criteria.getLanguage())) {
            specifications.add(fromLanguage(criteria.getLanguage()));
        }
        if (!CollectionUtils.isEmpty(criteria.getMode())) {
            specifications.add(fromMode(criteria.getMode()));
        }
        return specifications;
    }

    private Specification<HealthOffer> fromSpecialization(HealthCareSpecialization specialization) {
        return (root, cq, cb) -> cb.equal(root.get("specialization"), specialization);
    }

    private Specification<HealthOffer> fromMode(List<HealthCareMode> mode) {
        List<Specification<HealthOffer>> specifications = new LinkedList<>();
        mode.stream().filter(Objects::nonNull).forEach(lang ->
                specifications.add((root, cq, cb) -> cb.like(root.get("mode"), prepareForQuery(lang.name()))));
        return orSpecifications(specifications);
    }
}
