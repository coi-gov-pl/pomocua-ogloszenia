package pl.gov.coi.pomocua.ads.health;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import pl.gov.coi.pomocua.ads.BaseOffer;
import pl.gov.coi.pomocua.ads.Language;
import pl.gov.coi.pomocua.ads.Location;
import pl.gov.coi.pomocua.ads.health.HealthOffer.HealthCareMode;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class HealthOfferSpecifications {

    public static Specification<HealthOffer> from(HealthOfferSearchCriteria criteria) {
        List<Specification<HealthOffer>> specifications = new LinkedList<>();

        specifications.add(onlyActive());
        if (criteria.getLocation() != null) {
            specifications.add(fromLocation(criteria.getLocation()));
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

        return joinSpecifications(specifications);
    }

    private static Specification<HealthOffer> onlyActive() {
        return (root, cq, cb) -> cb.equal(root.get("status"), BaseOffer.Status.ACTIVE);
    }

    private static Specification<HealthOffer> fromLocation(Location location) {
        List<Specification<HealthOffer>> specifications = new LinkedList<>();
        if (location.getCity() != null) {
            specifications.add((root, cq, cb) ->
                    cb.equal(cb.upper(root.get("location").get("city")), location.getCity().toUpperCase()));
        }
        if (location.getRegion() != null) {
            specifications.add((root, cq, cb) ->
                    cb.equal(cb.upper(root.get("location").get("region")), location.getRegion().toUpperCase()));
        }
        return joinSpecifications(specifications);
    }

    private static Specification<HealthOffer> fromSpecialization(HealthCareSpecialization specialization) {
        return (root, cq, cb) -> cb.equal(root.get("specialization"), specialization);
    }

    private static Specification<HealthOffer> fromLanguage(List<Language> language) {
        List<Specification<HealthOffer>> specifications = new LinkedList<>();
        language.stream().filter(Objects::nonNull).forEach(lang ->
                specifications.add((root, cq, cb) -> cb.like(root.get("language"), prepareForQuery(lang.name()))));
        return orSpecifications(specifications);
    }

    private static Specification<HealthOffer> fromMode(List<HealthCareMode> mode) {
        List<Specification<HealthOffer>> specifications = new LinkedList<>();
        mode.stream().filter(Objects::nonNull).forEach(lang ->
                specifications.add((root, cq, cb) -> cb.like(root.get("mode"), prepareForQuery(lang.name()))));
        return orSpecifications(specifications);
    }

    private static Specification<HealthOffer> joinSpecifications(List<Specification<HealthOffer>> specifications) {
        return specifications.stream().reduce(Specification::and).orElse(null);
    }

    private static Specification<HealthOffer> orSpecifications(List<Specification<HealthOffer>> specifications) {
        return specifications.stream().reduce(Specification::or).orElse(null);
    }

    private static String prepareForQuery(String value) {
        return "%" + value + "%";
    }
}
