package pl.gov.coi.pomocua.ads.job;

import org.hibernate.validator.constraints.Length;
import pl.gov.coi.pomocua.ads.Language;
import pl.gov.coi.pomocua.ads.Location;
import pl.gov.coi.pomocua.ads.configuration.validation.PhoneNumber;
import pl.gov.coi.pomocua.ads.job.JobOffer.Mode;
import pl.gov.coi.pomocua.ads.job.JobOffer.Industry;
import pl.gov.coi.pomocua.ads.job.JobOffer.WorkTime;
import pl.gov.coi.pomocua.ads.job.JobOffer.ContractType;
import pl.gov.coi.pomocua.ads.phone.PhoneDetails;
import pl.gov.coi.pomocua.ads.phone.PhoneUtil;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import java.util.List;
import java.util.Optional;

import static pl.gov.coi.pomocua.ads.BaseOffer.DESCRIPTION_ALLOWED_TEXT;
import static pl.gov.coi.pomocua.ads.BaseOffer.TITLE_ALLOWED_TEXT;

public class JobOfferDefinitionDTO {
    @NotBlank
    @Length(max = 80)
    @Pattern(regexp = TITLE_ALLOWED_TEXT)
    public String title;

    @NotBlank
    @Length(max = 2000)
    @Pattern(regexp = DESCRIPTION_ALLOWED_TEXT)
    public String description;

    @NotNull
    public Mode mode;

    public Location location;

    @NotNull
    public Industry industry;

    @NotEmpty
    public List<WorkTime> workTime;

    @NotEmpty
    public List<ContractType> contractType;

    @NotEmpty
    public List<Language> language;

    @PhoneNumber
    public String phoneNumber;

    public void applyTo(JobOffer offer) {
        offer.title = title;
        offer.mode = mode;
        offer.location = location;
        offer.industry = industry;
        offer.setWorkTime(workTime);
        offer.setContractType(contractType);
        offer.setLanguage(language);
        offer.description = description;

        Optional<PhoneDetails> phoneDetails = PhoneUtil.getPhoneDetails(phoneNumber);
        offer.phoneNumber = phoneDetails.map(PhoneDetails::nationalNumber).orElse(null);
        offer.phoneCountryCode = phoneDetails.map(PhoneDetails::countryCode).orElse(null);
    }
}
