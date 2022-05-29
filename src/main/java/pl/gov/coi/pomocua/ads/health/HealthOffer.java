package pl.gov.coi.pomocua.ads.health;

import lombok.EqualsAndHashCode;
import org.hibernate.envers.Audited;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import pl.gov.coi.pomocua.ads.BaseOffer;
import pl.gov.coi.pomocua.ads.BaseOfferVisitor;
import pl.gov.coi.pomocua.ads.Language;
import pl.gov.coi.pomocua.ads.Location;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.EnumType.STRING;

@EqualsAndHashCode(callSuper = true)
@Entity
@Audited
public class HealthOffer extends BaseOffer<HealthOfferVM> {

    @NotEmpty
    private String mode;

    public void setMode(List<HealthCareMode> values) {
        if (CollectionUtils.isEmpty(values)) {
            mode = "";
        } else {
            mode = values.stream().map(Enum::name).collect(Collectors.joining(","));
        }
    }

    public List<HealthCareMode> getMode() {
        if (!StringUtils.hasText(mode)) {
            return Collections.emptyList();
        }
        return Arrays.stream(mode.split(",")).map(HealthCareMode::valueOf).collect(Collectors.toList());
    }

    @Enumerated(STRING)
    @NotNull
    public HealthCareSpecialization specialization;

    @Embedded
    public Location location;

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
    public final Type type = Type.HEALTH;

    @Override
    public HealthOfferVM accept(BaseOfferVisitor visitor, Language viewLang) {
        return visitor.visit(this, viewLang);
    }

    public enum Type {
        HEALTH
    }

    public enum HealthCareMode {
        IN_FACILITY,
        AT_HOME,
        ONLINE,
        BY_PHONE
    }

}
