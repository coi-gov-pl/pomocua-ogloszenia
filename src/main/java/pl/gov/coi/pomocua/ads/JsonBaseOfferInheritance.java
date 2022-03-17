package pl.gov.coi.pomocua.ads;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import pl.gov.coi.pomocua.ads.accomodations.AccommodationOffer;
import pl.gov.coi.pomocua.ads.materialaid.MaterialAidOffer;
import pl.gov.coi.pomocua.ads.transport.TransportOffer;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = AccommodationOffer.class, name = "accommodation"),
        @JsonSubTypes.Type(value = MaterialAidOffer.class, name = "materialAid"),
        @JsonSubTypes.Type(value = TransportOffer.class, name = "transport")
})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
public @interface JsonBaseOfferInheritance {
}

