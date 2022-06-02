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
        return (root, cq, cb) -> cb.like(cb.concat(root.get("title"), root.get("description")), prepareForQuery(searchText));
    }

    private Specification<OtherOffer> searchTextUa(String searchText) {
        return (root, cq, cb) -> cb.or(
                cb.and(
                        cb.isNull(root.get("detectedLanguage")),
                        cb.like(cb.concat(root.get("title"), root.get("description")), prepareForQuery(searchText))
                ),
                cb.and(
                        cb.like(cb.concat(root.get("titleUa"), root.get("descriptionUa")), prepareForQuery(searchText))
                )
        );
    }

    private Specification<OtherOffer> searchTextEn(String searchText) {
        return (root, cq, cb) -> cb.or(
                cb.and(
                        cb.isNull(root.get("detectedLanguage")),
                        cb.like(cb.concat(root.get("title"), root.get("description")), prepareForQuery(searchText))
                ),
                cb.and(
                        cb.like(cb.concat(root.get("titleEn"), root.get("descriptionEn")), prepareForQuery(searchText))
                )
        );
    }

    private Specification<OtherOffer> searchTextRu(String searchText) {
        return (root, cq, cb) -> cb.or(
                cb.and(
                        cb.isNull(root.get("detectedLanguage")),
                        cb.like(cb.concat(root.get("title"), root.get("description")), prepareForQuery(searchText))
                ),
                cb.and(
                        cb.like(cb.concat(root.get("titleRu"), root.get("descriptionRu")), prepareForQuery(searchText))
                )
        );
    }
}
