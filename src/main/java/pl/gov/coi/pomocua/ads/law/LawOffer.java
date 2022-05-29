package pl.gov.coi.pomocua.ads.law;

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
public class LawOffer extends BaseOffer<LawOfferVM> {

    @Embedded
    public Location location;

    @NotEmpty
    private String helpMode;

    public void setHelpMode(List<HelpMode> values) {
        if (CollectionUtils.isEmpty(values)) {
            helpMode = "";
        } else {
            helpMode = values.stream().map(Enum::name).collect(Collectors.joining(","));
        }
    }

    public List<HelpMode> getHelpMode() {
        if (!StringUtils.hasText(helpMode)) {
            return Collections.emptyList();
        }
        return Arrays.stream(helpMode.split(",")).map(HelpMode::valueOf).collect(Collectors.toList());
    }

    @NotEmpty
    private String helpKind;

    public void setHelpKind(List<HelpKind> values) {
        if (CollectionUtils.isEmpty(values)) {
            helpKind = "";
        } else {
            helpKind = values.stream().map(Enum::name).collect(Collectors.joining(","));
        }
    }

    public List<HelpKind> getHelpKind() {
        if (!StringUtils.hasText(helpKind)) {
            return Collections.emptyList();
        }
        return Arrays.stream(helpKind.split(",")).map(HelpKind::valueOf).collect(Collectors.toList());
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
    public final Type type = Type.LAW;

    @Override
    public LawOfferVM accept(BaseOfferVisitor visitor, Language viewLang) {
        return visitor.visit(this, viewLang);
    }

    public enum Type {
        LAW
    }

    public enum HelpMode {
        STATIONARY,
        WITH_ACCESS,
        ONLINE,
        BY_EMAIL,
        BY_PHONE
    }

    public enum HelpKind {
        LABOUR_LAW,
        IMMIGRATION_LAW,
        FAMILY_LAW,
        TAX_LAW,
        CIVIL_LAW,
        OTHER
    }
}
