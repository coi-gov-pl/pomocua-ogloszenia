package pl.gov.coi.pomocua.ads.translation;

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
public class TranslationOffer extends BaseOffer<TranslationOfferVM> {

    @NotEmpty
    private String mode;

    public void setMode(List<TranslationMode> values) {
        if (CollectionUtils.isEmpty(values)) {
            mode = "";
        } else {
            mode = values.stream().map(Enum::name).collect(Collectors.joining(","));
        }
    }

    public List<TranslationMode> getMode() {
        if (!StringUtils.hasText(mode)) {
            return Collections.emptyList();
        }
        return Arrays.stream(mode.split(",")).map(TranslationMode::valueOf).collect(Collectors.toList());
    }

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
    public final Type type = Type.TRANSLATION;

    @Override
    public TranslationOfferVM accept(BaseOfferVisitor visitor, Language viewLang) {
        return visitor.visit(this, viewLang);
    }

    public enum Type {
        TRANSLATION
    }

    public enum TranslationMode {
        STATIONARY,
        WITH_ACCESS,
        ONLINE,
        BY_EMAIL,
        BY_PHONE
    }
}
