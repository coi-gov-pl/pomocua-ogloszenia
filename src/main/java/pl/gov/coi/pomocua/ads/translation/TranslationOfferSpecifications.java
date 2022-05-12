package pl.gov.coi.pomocua.ads.translation;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import pl.gov.coi.pomocua.ads.BaseOfferSpecifications;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import pl.gov.coi.pomocua.ads.translation.TranslationOffer.TranslationMode;

@Component
public class TranslationOfferSpecifications
        extends BaseOfferSpecifications<TranslationOffer, TranslationOfferSearchCriteria> {

    @Override
    protected List<Specification<TranslationOffer>> fromOfferSpecific(TranslationOfferSearchCriteria criteria) {
        List<Specification<TranslationOffer>> specifications = new LinkedList<>();
        if (criteria.getLocation() != null) {
            specifications.add(fromLocationNullable("location", criteria.getLocation()));
        }
        if (!CollectionUtils.isEmpty(criteria.getMode())) {
            specifications.add(fromMode(criteria.getMode()));
        }
        if (!CollectionUtils.isEmpty(criteria.getLanguage())) {
            specifications.add(fromLanguage(criteria.getLanguage()));
        }
        return specifications;
    }

    private Specification<TranslationOffer> fromMode(List<TranslationMode> mode) {
        List<Specification<TranslationOffer>> specifications = new LinkedList<>();
        mode.stream().filter(Objects::nonNull).forEach(entry ->
                specifications.add((root, cq, cb) -> cb.like(root.get("mode"), prepareForQuery(entry.name()))));
        return orSpecifications(specifications);
    }
}
