package pl.gov.coi.pomocua.ads;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import pl.gov.coi.pomocua.ads.accomodations.AccommodationOffer;
import pl.gov.coi.pomocua.ads.materialaid.MaterialAidOffer;
import pl.gov.coi.pomocua.ads.transport.TransportOffer;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@JsonTypeInfo(use = Id.NAME, include = As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes({
        @Type(value = AccommodationOffer.class, name = "accommodation"),
        @Type(value = MaterialAidOffer.class, name = "materialAid"),
        @Type(value = TransportOffer.class, name = "transport")
})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
public @interface JsonBaseOfferInheritance {
}
