package pl.gov.coi.pomocua.ads;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import pl.gov.coi.pomocua.ads.accomodations.AccommodationOffer;
import pl.gov.coi.pomocua.ads.health.HealthOffer;
import pl.gov.coi.pomocua.ads.job.JobOffer;
import pl.gov.coi.pomocua.ads.law.LawOffer;
import pl.gov.coi.pomocua.ads.materialaid.MaterialAidOffer;
import pl.gov.coi.pomocua.ads.translation.TranslationOffer;
import pl.gov.coi.pomocua.ads.transport.TransportOffer;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@JsonTypeInfo(use = Id.NAME, include = As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes({
        @Type(value = AccommodationOffer.class, name = "ACCOMMODATION"),
        @Type(value = MaterialAidOffer.class, name = "MATERIAL_AID"),
        @Type(value = TransportOffer.class, name = "TRANSPORT"),
        @Type(value = JobOffer.class, name = "JOB"),
        @Type(value = LawOffer.class, name = "LAW"),
        @Type(value = HealthOffer.class, name = "HEALTH"),
        @Type(value = TranslationOffer.class, name = "TRANSLATION")
})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
public @interface JsonBaseOfferInheritance {
}
