package pl.gov.coi.pomocua.ads.law;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import pl.gov.coi.pomocua.ads.BaseOffer;
import pl.gov.coi.pomocua.ads.Language;
import pl.gov.coi.pomocua.ads.Location;
import pl.gov.coi.pomocua.ads.law.LawOffer.HelpKind;
import pl.gov.coi.pomocua.ads.law.LawOffer.HelpMode;

import java.util.LinkedList;
import java.util.List;

public class LawOfferSpecifications {

    public static Specification<LawOffer> from(LawOfferSearchCriteria criteria) {
        List<Specification<LawOffer>> specifications = new LinkedList<>();

        specifications.add(onlyActive());
        if (criteria.getLocation() != null) {
            specifications.add(fromLocation(criteria.getLocation()));
        }
        if (criteria.getHelpMode() != null) {
            specifications.add(fromHelpMode(criteria.getHelpMode()));
        }
        if (criteria.getHelpKind() != null) {
            specifications.add(fromHelpKind(criteria.getHelpKind()));
        }
        if (!CollectionUtils.isEmpty(criteria.getLanguage())) {
            specifications.add(fromLanguage(criteria.getLanguage()));
        }
        return joinSpecifications(specifications);
    }

    private static Specification<LawOffer> onlyActive() {
        return (root, cq, cb) -> cb.equal(root.get("status"), BaseOffer.Status.ACTIVE);
    }

    private static Specification<LawOffer> fromLocation(Location location) {
        List<Specification<LawOffer>> specifications = new LinkedList<>();
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

    private static Specification<LawOffer> fromHelpMode(List<HelpMode> helpMode) {
        List<Specification<LawOffer>> specifications = new LinkedList<>();
        helpMode.forEach(mode ->
                specifications.add((root, cq, cb) -> cb.like(root.get("helpMode"), prepareForQuery(mode.name()))));
        return orSpecifications(specifications);

    }

    private static Specification<LawOffer> fromHelpKind(HelpKind helpKind) {
        return (root, cq, cb) -> cb.like(root.get("helpKind"), prepareForQuery(helpKind.name()));
    }

    private static Specification<LawOffer> fromLanguage(List<Language> language) {
        List<Specification<LawOffer>> specifications = new LinkedList<>();
        language.forEach(lang ->
                specifications.add((root, cq, cb) -> cb.like(root.get("language"), prepareForQuery(lang.name()))));
        return orSpecifications(specifications);
    }

    private static Specification<LawOffer> joinSpecifications(List<Specification<LawOffer>> specifications) {
        return specifications.stream().reduce(Specification::and).orElse(null);
    }

    private static Specification<LawOffer> orSpecifications(List<Specification<LawOffer>> specifications) {
        return specifications.stream().reduce(Specification::or).orElse(null);
    }

    private static String prepareForQuery(String value) {
        return "%" + value + "%";
    }
}
