package pl.gov.coi.pomocua.ads.job;

import lombok.Builder;
import pl.gov.coi.pomocua.ads.Language;
import pl.gov.coi.pomocua.ads.Location;

import java.util.List;
import java.util.Optional;

import pl.gov.coi.pomocua.ads.job.JobOffer.Mode;
import pl.gov.coi.pomocua.ads.job.JobOffer.Industry;
import pl.gov.coi.pomocua.ads.job.JobOffer.WorkTime;
import pl.gov.coi.pomocua.ads.job.JobOffer.ContractType;

public class JobTestDataGenerator {
    public static JobOfferBuilder aJobOffer() {
        return JobTestDataGenerator.builder()
                .title("sample job offer")
                .description("sample job description")
                .location(new Location("Mazowieckie", "Warszawa"))
                .mode(Mode.ONSITE)
                .industry(Industry.FINANCES);
    };

    public static JobOfferDefinitionDTO sampleUpdateJson() {
        var updateJson = new JobOfferDefinitionDTO();
        updateJson.title = "new title";
        updateJson.description = "new description";
        updateJson.mode = Mode.TELEWORK;
        updateJson.location = new Location("Pomorskie", "Gdańsk");
        updateJson.industry = Industry.CONSULTING;
        updateJson.workTime = List.of(WorkTime.FULL_TIME, WorkTime.PART_TIME);
        updateJson.contractType = List.of(ContractType.B2B);
        updateJson.language = List.of(Language.PL, Language.EN);
        return updateJson;
    }

    @Builder
    private static JobOffer jobOfferBuilder(
            String title,
            String description,
            Mode mode,
            Industry industry,
            List<WorkTime> workTime,
            List<ContractType> contractType,
            Location location,
            List<Language> language,
            String phoneNumber
    ) {
        JobOffer offer = new JobOffer();
        offer.title = Optional.ofNullable(title).orElse("some title");
        offer.description = Optional.ofNullable(description).orElse("some description");
        offer.mode = Optional.ofNullable(mode).orElse(Mode.ONSITE);
        offer.industry = Optional.ofNullable(industry).orElse(Industry.FINANCES);
        offer.setWorkTime(Optional.ofNullable(workTime).orElse(List.of(WorkTime.FULL_TIME)));
        offer.setContractType(Optional.ofNullable(contractType).orElse(List.of(ContractType.EMPLOYMENT)));
        offer.location = Optional.ofNullable(location).orElse(new Location("mazowieckie", "warszawa"));
        offer.setLanguage(Optional.ofNullable(language).orElse(List.of(Language.PL, Language.UA)));
        offer.phoneNumber = Optional.ofNullable(phoneNumber).orElse("+48123456789");
        return offer;
    }
}
