package pl.gov.coi.pomocua.ads.job;

import pl.gov.coi.pomocua.ads.BaseOfferDefinitionDTO;
import pl.gov.coi.pomocua.ads.Language;
import pl.gov.coi.pomocua.ads.Location;
import pl.gov.coi.pomocua.ads.job.JobOffer.Mode;
import pl.gov.coi.pomocua.ads.job.JobOffer.Industry;
import pl.gov.coi.pomocua.ads.job.JobOffer.WorkTime;
import pl.gov.coi.pomocua.ads.job.JobOffer.ContractType;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.util.List;

public class JobOfferDefinitionDTO extends BaseOfferDefinitionDTO<JobOffer> {

    @NotNull
    public Mode mode;

    @Valid
    public Location location;

    @NotNull
    public Industry industry;

    @NotEmpty
    public List<WorkTime> workTime;

    @NotEmpty
    public List<ContractType> contractType;

    @NotEmpty
    public List<Language> language;

    @Override
    protected void applyOfferSpecific(JobOffer offer) {
        offer.mode = mode;
        offer.location = location;
        offer.industry = industry;
        offer.setWorkTime(workTime);
        offer.setContractType(contractType);
        offer.setLanguage(language);
    }
}
