package pl.gov.coi.pomocua.ads.other;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import pl.gov.coi.pomocua.ads.BaseOfferSpecifications;

import java.util.LinkedList;
import java.util.List;

@Component
public class OtherOfferSpecifications extends BaseOfferSpecifications<OtherOffer, OtherOfferSearchCriteria> {

    @Override
    protected List<Specification<OtherOffer>> fromOfferSpecific(OtherOfferSearchCriteria criteria) {
        List<Specification<OtherOffer>> specifications = new LinkedList<>();
        if (criteria.getLocation() != null) {
            specifications.add(fromLocationNullable("location", criteria.getLocation()));
        }
        if (StringUtils.hasText(criteria.getSearchText())) {
            switch (criteria.getLang()) {
                case UA -> specifications.add(searchTextUa(criteria.getSearchText()));
                case EN -> specifications.add(searchTextEn(criteria.getSearchText()));
                case RU -> specifications.add(searchTextRu(criteria.getSearchText()));
                default -> specifications.add(searchText(criteria.getSearchText()));
            }
        }
        return specifications;
    }

    private Specification<OtherOffer> searchText(String searchText) {
        return (root, cq, cb) -> cb.isTrue(cb.function("fts", Boolean.class, cb.literal(searchText)));
    }

    private Specification<OtherOffer> searchTextUa(String searchText) {
        return (root, cq, cb) -> cb.isTrue(cb.function("fts_ua", Boolean.class, cb.literal(searchText)));
    }

    private Specification<OtherOffer> searchTextEn(String searchText) {
        return (root, cq, cb) -> cb.isTrue(cb.function("fts_en", Boolean.class, cb.literal(searchText)));
    }

    private Specification<OtherOffer> searchTextRu(String searchText) {
        return (root, cq, cb) -> cb.isTrue(cb.function("fts_ru", Boolean.class, cb.literal(searchText)));
    }
}
