package pl.gov.coi.pomocua.ads.job;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import pl.gov.coi.pomocua.ads.BaseOffer;
import pl.gov.coi.pomocua.ads.Language;
import pl.gov.coi.pomocua.ads.Location;

import java.util.LinkedList;
import java.util.List;

import pl.gov.coi.pomocua.ads.job.JobOffer.Industry;
import pl.gov.coi.pomocua.ads.job.JobOffer.WorkTime;
import pl.gov.coi.pomocua.ads.job.JobOffer.ContractType;
import pl.gov.coi.pomocua.ads.job.JobOffer.Mode;

public class JobOfferSpecifications {

    public static Specification<JobOffer> from(JobOfferSearchCriteria criteria) {
        List<Specification<JobOffer>> specifications = new LinkedList<>();

        specifications.add(onlyActive());
        if (criteria.getLocation() != null) {
            specifications.add(fromLocation(criteria.getLocation()));
        }
        if (criteria.getIndustry() != null) {
            specifications.add(fromIndustry(criteria.getIndustry()));
        }
        if (criteria.getWorkTime() != null) {
            specifications.add(fromWorkTime(criteria.getWorkTime()));
        }
        if (criteria.getContractType() != null) {
            specifications.add(fromContractType(criteria.getContractType()));
        }
        if (criteria.getMode() != null) {
            specifications.add(fromMode(criteria.getMode()));
        }
        if (!CollectionUtils.isEmpty(criteria.getLanguage())) {
            specifications.add(fromLanguage(criteria.getLanguage()));
        }
        return joinSpecifications(specifications);
    }

    private static Specification<JobOffer> onlyActive() {
        return (root, cq, cb) -> cb.equal(root.get("status"), BaseOffer.Status.ACTIVE);
    }

    private static Specification<JobOffer> fromLocation(Location location) {
        List<Specification<JobOffer>> specifications = new LinkedList<>();
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

    private static Specification<JobOffer> fromIndustry(Industry industry) {
        return (root, cq, cb) -> cb.like(root.get("industry"), prepareForQuery(industry.name()));
    }

    private static Specification<JobOffer> fromWorkTime(WorkTime workTime) {
        return (root, cq, cb) -> cb.like(root.get("workTime"), prepareForQuery(workTime.name()));
    }

    private static Specification<JobOffer> fromContractType(ContractType contractType) {
        return (root, cq, cb) -> cb.like(root.get("contractType"), prepareForQuery(contractType.name()));
    }

    private static Specification<JobOffer> fromMode(Mode mode) {
        return (root, cq, cb) -> cb.equal(root.get("mode"), mode);
    }

    private static Specification<JobOffer> fromLanguage(List<Language> language) {
        List<Specification<JobOffer>> specifications = new LinkedList<>();
        language.forEach(lang ->
                specifications.add((root, cq, cb) -> cb.like(root.get("language"), prepareForQuery(lang.name()))));
        return orSpecifications(specifications);
    }

    private static Specification<JobOffer> joinSpecifications(List<Specification<JobOffer>> specifications) {
        return specifications.stream().reduce(Specification::and).orElse(null);
    }

    private static Specification<JobOffer> orSpecifications(List<Specification<JobOffer>> specifications) {
        return specifications.stream().reduce(Specification::or).orElse(null);
    }

    private static String prepareForQuery(String value) {
        return "%" + value + "%";
    }
}
