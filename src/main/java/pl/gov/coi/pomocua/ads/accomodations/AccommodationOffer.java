package pl.gov.coi.pomocua.ads.accomodations;

import lombok.EqualsAndHashCode;
import org.hibernate.envers.Audited;
import pl.gov.coi.pomocua.ads.BaseOffer;
import pl.gov.coi.pomocua.ads.Location;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static javax.persistence.EnumType.STRING;

@EqualsAndHashCode(callSuper = true)
@Entity
@Audited
public class AccommodationOffer extends BaseOffer {

    @Embedded
    @Valid
    @NotNull
    public Location location;

    @Min(1)
    @NotNull
    public Integer guests;

    @Enumerated(STRING)
    @NotNull
    public LengthOfStay lengthOfStay;

    @NotEmpty
    @Convert(converter = LanguageConverter.class)
    public List<Language> hostLanguage;

    @NotNull
    @Transient
    public final Type type = Type.ACCOMMODATION;

    public enum Type {
        ACCOMMODATION
    }

    public enum LengthOfStay {
        WEEK_1,
        WEEK_2,
        MONTH_1,
        MONTH_2,
        MONTH_3,
        LONGER
    }

    public enum Language {
        UA, PL, EN, RU
    }

    public static class LanguageConverter implements AttributeConverter<List<Language>, String> {
        @Override
        public String convertToDatabaseColumn(List<Language> values) {
            if (values == null || values.isEmpty()) {
                return "";
            }
            return values.stream().map(Enum::name).collect(joining(","));
        }

        @Override
        public List<Language> convertToEntityAttribute(String value) {
            if (value == null || value.isBlank()) {
                return emptyList();
            }
            return Arrays.stream(value.split(",")).map(Language::valueOf).collect(toList());
        }
    }
}
