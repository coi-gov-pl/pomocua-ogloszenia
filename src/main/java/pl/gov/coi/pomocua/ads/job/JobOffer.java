package pl.gov.coi.pomocua.ads.job;

import lombok.EqualsAndHashCode;
import org.hibernate.envers.Audited;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import pl.gov.coi.pomocua.ads.BaseOffer;
import pl.gov.coi.pomocua.ads.Language;
import pl.gov.coi.pomocua.ads.Location;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Entity
@Audited
public class JobOffer extends BaseOffer {

    @Enumerated(EnumType.STRING)
    @NotNull
    public Mode mode;

    @Embedded
    public Location location;

    @NotEmpty
    private String industry;

    public void setIndustry(List<Industry> values) {
        if (CollectionUtils.isEmpty(values)) {
            industry = "";
        } else {
            industry = values.stream().map(Enum::name).collect(Collectors.joining(","));
        }
    }

    public List<Industry> getIndustry() {
        if (!StringUtils.hasText(industry)) {
            return Collections.emptyList();
        }
        return Arrays.stream(industry.split(",")).map(Industry::valueOf).collect(Collectors.toList());
    }

    @NotEmpty
    private String workTime;

    public void setWorkTime(List<WorkTime> values) {
        if (CollectionUtils.isEmpty(values)) {
            workTime = "";
        } else {
            workTime = values.stream().map(Enum::name).collect(Collectors.joining(","));
        }
    }

    public List<WorkTime> getWorkTime() {
        if (!StringUtils.hasText(workTime)) {
            return Collections.emptyList();
        }
        return Arrays.stream(workTime.split(",")).map(WorkTime::valueOf).collect(Collectors.toList());
    }

    @NotEmpty
    private String contractType;

    public void setContractType(List<ContractType> values) {
        if (CollectionUtils.isEmpty(values)) {
            contractType = "";
        } else {
            contractType = values.stream().map(Enum::name).collect(Collectors.joining(","));
        }
    }

    public List<ContractType> getContractType() {
        if (!StringUtils.hasText(contractType)) {
            return Collections.emptyList();
        }
        return Arrays.stream(contractType.split(",")).map(ContractType::valueOf).collect(Collectors.toList());
    }

    @NotEmpty
    private String language;

    public void setLanguage(List<Language> values) {
        if (CollectionUtils.isEmpty(values)) {
            language = "";
        } else {
            language = values.stream().map(Enum::name).collect(Collectors.joining(","));
        }
    }

    public List<Language> getLanguage() {
        if (language == null || language.isBlank()) {
            return Collections.emptyList();
        }
        return Arrays.stream(language.split(",")).map(Language::valueOf).collect(Collectors.toList());
    }

    @NotNull
    @Transient
    public final Type type = Type.JOB;

    public enum Type {
        JOB
    }

    public enum Mode {
        ONSITE,
        TELEWORK,
        MIXED
    }

    public enum Industry {
        FINANCES,
        HEALTH_AND_SAFETY,
        ENGINEERING,
        EDUCATION,
        HOTEL,
        INTERNET,
        IT,
        ENTERTAINMENT,
        REAL_ASSETS,
        CUSTOMER_SERVICE,
        DESK_JOB,
        MANUAL_JOB,
        CONSULTING,
        MANUFACTURING,
        MARKETING,
        PUBLIC_SECTOR,
        LOGISTICS,
        TRADE,
        HEALTH_AND_BEAUTY,
        MISC
    }

    public enum WorkTime {
        FULL_TIME,
        PART_TIME,
        TEMPORARY
    }

    public enum ContractType {
        EMPLOYMENT,
        SPECIFIC_TASK,
        MANDATE,
        B2B
    }
}
