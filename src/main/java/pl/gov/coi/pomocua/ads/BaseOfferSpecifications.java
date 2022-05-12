package pl.gov.coi.pomocua.ads;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public abstract class BaseOfferSpecifications<T extends BaseOffer, U> {

    public Specification<T> from(U criteria) {
        List<Specification<T>> specifications = new LinkedList<>();
        specifications.add(onlyActive());
        specifications.addAll(fromOfferSpecific(criteria));
        return joinSpecifications(specifications);
    }

    protected abstract List<Specification<T>> fromOfferSpecific(U criteria);

    private Specification<T> onlyActive() {
        return (root, cq, cb) -> cb.equal(root.get("status"), BaseOffer.Status.ACTIVE);
    }

    protected Specification<T> fromLocation(String fieldName, Location location) {
        List<Specification<T>> specifications = new LinkedList<>();
        if (StringUtils.hasText(location.getCity())) {
            specifications.add((root, cq, cb) ->
                    cb.equal(cb.upper(root.get(fieldName).get("city")), location.getCity().toUpperCase()));
        }
        if (StringUtils.hasText(location.getRegion())) {
            specifications.add((root, cq, cb) ->
                    cb.equal(cb.upper(root.get(fieldName).get("region")), location.getRegion().toUpperCase()));
        }
        return joinSpecifications(specifications);
    }

    protected Specification<T> fromLocationNullable(String fieldName, Location location) {
        List<Specification<T>> specifications = new LinkedList<>();
        if (StringUtils.hasText(location.getCity())) {
            specifications.add((root, cq, cb) ->
                    cb.or(
                            cb.isNull(root.get(fieldName).get("city")),
                            cb.equal(cb.upper(root.get(fieldName).get("city")), location.getCity().toUpperCase())));
        }
        if (StringUtils.hasText(location.getRegion())) {
            specifications.add((root, cq, cb) ->
                    cb.or(
                            cb.isNull(root.get(fieldName).get("region")),
                            cb.equal(cb.upper(root.get(fieldName).get("region")), location.getRegion().toUpperCase())));
        }
        return joinSpecifications(specifications);
    }

    protected Specification<T> fromLanguage(List<Language> language) {
        List<Specification<T>> specifications = new LinkedList<>();
        language.stream().filter(Objects::nonNull).forEach(lang ->
                specifications.add((root, cq, cb) -> cb.like(root.get("language"), prepareForQuery(lang.name()))));
        return orSpecifications(specifications);
    }

    protected Specification<T> joinSpecifications(List<Specification<T>> specifications) {
        return specifications.stream().reduce(Specification::and).orElse(null);
    }

    protected Specification<T> orSpecifications(List<Specification<T>> specifications) {
        return specifications.stream().reduce(Specification::or).orElse(null);
    }

    protected String prepareForQuery(String value) {
        return "%" + value + "%";
    }
}
