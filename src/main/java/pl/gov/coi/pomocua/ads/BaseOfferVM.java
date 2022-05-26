package pl.gov.coi.pomocua.ads;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

@EqualsAndHashCode
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY)
@JsonBaseOfferVMInheritance
@Data
public class BaseOfferVM {

    private Long id;

    @JsonIgnore
    private UserId userId;

    private String userFirstName;

    private String title;

    private String description;

    private String phoneNumber;

    private String phoneCountryCode;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant modifiedDate;

    @JsonIgnore
    private BaseOffer.Status status;

    private Language detectedLanguage;

    protected static void mapBase(BaseOffer offer, Language viewLang, BaseOfferVM offerVM) {
        mapProperties(offer, viewLang, offerVM);
    }

    public static BaseOfferVM from(BaseOffer offer, Language viewLang) {
        BaseOfferVM result = new BaseOfferVM();
        mapProperties(offer, viewLang, result);
        return result;
    }

    private static void mapProperties(BaseOffer offer, Language viewLang, BaseOfferVM offerVM) {
        offerVM.id = offer.id;
        offerVM.userId = offer.userId;
        offerVM.userFirstName = offer.userFirstName;
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
        offerVM.phoneNumber = offer.phoneNumber;
        offerVM.phoneCountryCode = offer.phoneCountryCode;
        offerVM.modifiedDate = offer.modifiedDate;
        offerVM.status = offer.status;
        offerVM.detectedLanguage = offer.detectedLanguage;
    }
}
