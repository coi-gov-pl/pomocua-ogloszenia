package pl.gov.coi.pomocua.ads.law;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import pl.gov.coi.pomocua.ads.BaseOfferSpecifications;
import pl.gov.coi.pomocua.ads.law.LawOffer.HelpKind;
import pl.gov.coi.pomocua.ads.law.LawOffer.HelpMode;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Component
public class LawOfferSpecifications extends BaseOfferSpecifications<LawOffer, LawOfferSearchCriteria> {

    @Override
    protected List<Specification<LawOffer>> fromOfferSpecific(LawOfferSearchCriteria criteria) {
        List<Specification<LawOffer>> specifications = new LinkedList<>();

        if (criteria.getLocation() != null) {
            specifications.add(fromLocationNullable("location", criteria.getLocation()));
        }
        if (!CollectionUtils.isEmpty(criteria.getHelpMode())) {
            specifications.add(fromHelpMode(criteria.getHelpMode()));
        }
        if (criteria.getHelpKind() != null) {
            specifications.add(fromHelpKind(criteria.getHelpKind()));
        }
        if (!CollectionUtils.isEmpty(criteria.getLanguage())) {
            specifications.add(fromLanguage(criteria.getLanguage()));
        }
        return specifications;
    }

    private Specification<LawOffer> fromHelpMode(List<HelpMode> helpMode) {
        List<Specification<LawOffer>> specifications = new LinkedList<>();
        helpMode.stream().filter(Objects::nonNull).forEach(mode ->
                specifications.add((root, cq, cb) -> cb.like(root.get("helpMode"), prepareForQuery(mode.name()))));
        return orSpecifications(specifications);

    }

    private Specification<LawOffer> fromHelpKind(HelpKind helpKind) {
        return (root, cq, cb) -> cb.like(root.get("helpKind"), prepareForQuery(helpKind.name()));
    }
}
