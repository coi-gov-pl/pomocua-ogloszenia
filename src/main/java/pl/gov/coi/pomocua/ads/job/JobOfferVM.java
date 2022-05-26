package pl.gov.coi.pomocua.ads.job;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.gov.coi.pomocua.ads.Language;
import pl.gov.coi.pomocua.ads.Location;
import pl.gov.coi.pomocua.ads.BaseOfferVM;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class JobOfferVM extends BaseOfferVM {

    private JobOffer.Mode mode;

    private Location location;

    private JobOffer.Industry industry;

    private List<JobOffer.WorkTime> workTime;

    private List<JobOffer.ContractType> contractType;

    private List<Language> language;

    public final JobOffer.Type type = JobOffer.Type.JOB;

    public static JobOfferVM from(JobOffer offer, Language viewLang) {
        JobOfferVM result = new JobOfferVM();
        mapBase(offer, viewLang, result);
        result.mode = offer.mode;
        result.location = offer.location;
        result.industry = offer.industry;
        result.workTime = offer.getWorkTime();
        result.contractType = offer.getContractType();
        result.language = offer.getLanguage();
        return result;
    }
}
