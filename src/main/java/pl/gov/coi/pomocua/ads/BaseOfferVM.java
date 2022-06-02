package pl.gov.coi.pomocua.ads;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.Instant;

import static pl.gov.coi.pomocua.ads.BaseOffer.DESCRIPTION_ALLOWED_TEXT;
import static pl.gov.coi.pomocua.ads.BaseOffer.TITLE_ALLOWED_TEXT;

@EqualsAndHashCode
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY)
@JsonBaseOfferVMInheritance
@Data
public abstract class BaseOfferVM {

    @NotNull
    private Long id;

    @JsonIgnore
    private UserId userId;

    @NotNull
    private String userFirstName;

    @NotBlank
    @Length(max = 80)
    @Pattern(regexp = TITLE_ALLOWED_TEXT)
    private String title;

    @NotBlank
    @Length(max = 2000)
    @Pattern(regexp = DESCRIPTION_ALLOWED_TEXT)
    private String description;

    private String phoneNumber;

    private String phoneCountryCode;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant modifiedDate;

    @JsonIgnore
    @NotNull
    private BaseOffer.Status status;

    private Language detectedLanguage;

    protected static void mapBase(BaseOffer offer, Language viewLang, BaseOfferVM offerVM) {
        mapProperties(offer, viewLang, offerVM);
    }

    private static void mapProperties(BaseOffer offer, Language viewLang, BaseOfferVM offerVM) {
        offerVM.id = offer.id;
        offerVM.userId = offer.userId;
        offerVM.userFirstName = offer.userFirstName;
        if (offer.detectedLanguage == null) {
            offerVM.title = offer.title;
            offerVM.description = offer.description;
        } else {
            switch (viewLang) {
                case UA -> {
                    offerVM.title = offer.titleUa;
                    offerVM.description = offer.descriptionUa;
                }
                case EN -> {
                    offerVM.title = offer.titleEn;
                    offerVM.description = offer.descriptionEn;
                }
                case RU -> {
                    offerVM.title = offer.titleRu;
                    offerVM.description = offer.descriptionRu;
                }
                default -> {
                    offerVM.title = offer.title;
                    offerVM.description = offer.description;
                }
            }
        }
        offerVM.phoneNumber = offer.phoneNumber;
        offerVM.phoneCountryCode = offer.phoneCountryCode;
        offerVM.modifiedDate = offer.modifiedDate;
        offerVM.status = offer.status;
        offerVM.detectedLanguage = offer.detectedLanguage;
    }
}
