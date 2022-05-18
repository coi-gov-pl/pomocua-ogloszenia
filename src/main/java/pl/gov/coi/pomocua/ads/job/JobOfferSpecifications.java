package pl.gov.coi.pomocua.ads.job;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import pl.gov.coi.pomocua.ads.BaseOfferSpecifications;

import java.util.LinkedList;
import java.util.List;

import pl.gov.coi.pomocua.ads.job.JobOffer.Industry;
import pl.gov.coi.pomocua.ads.job.JobOffer.WorkTime;
import pl.gov.coi.pomocua.ads.job.JobOffer.ContractType;
import pl.gov.coi.pomocua.ads.job.JobOffer.Mode;

@Component
public class JobOfferSpecifications extends BaseOfferSpecifications<JobOffer, JobOfferSearchCriteria> {

    @Override
    protected List<Specification<JobOffer>> fromOfferSpecific(JobOfferSearchCriteria criteria) {
        List<Specification<JobOffer>> specifications = new LinkedList<>();
        if (criteria.getLocation() != null) {
            specifications.add(fromLocationNullable("location", criteria.getLocation()));
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
        return specifications;
    }

    private Specification<JobOffer> fromIndustry(Industry industry) {
        return (root, cq, cb) -> cb.equal(root.get("industry"), industry);
    }

    private Specification<JobOffer> fromWorkTime(WorkTime workTime) {
        return (root, cq, cb) -> cb.like(root.get("workTime"), prepareForQuery(workTime.name()));
    }

    private Specification<JobOffer> fromContractType(ContractType contractType) {
        return (root, cq, cb) -> cb.like(root.get("contractType"), prepareForQuery(contractType.name()));
    }

    private Specification<JobOffer> fromMode(Mode mode) {
        return (root, cq, cb) -> cb.equal(root.get("mode"), mode);
    }
}
