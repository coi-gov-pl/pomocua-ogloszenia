package pl.gov.coi.pomocua.ads.job;

import lombok.Data;
import pl.gov.coi.pomocua.ads.Language;
import pl.gov.coi.pomocua.ads.Location;

import java.util.List;

import pl.gov.coi.pomocua.ads.job.JobOffer.Industry;
import pl.gov.coi.pomocua.ads.job.JobOffer.WorkTime;
import pl.gov.coi.pomocua.ads.job.JobOffer.ContractType;
import pl.gov.coi.pomocua.ads.job.JobOffer.Mode;

@Data
public class JobOfferSearchCriteria {
    private Location location;
    private Industry industry;
    private WorkTime workTime;
    private ContractType contractType;
    private Mode mode;
    private List<Language> language;
}
