package pl.gov.coi.pomocua.ads;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import pl.gov.coi.pomocua.ads.accomodations.AccommodationOfferVM;
import pl.gov.coi.pomocua.ads.health.HealthOfferVM;
import pl.gov.coi.pomocua.ads.job.JobOfferVM;
import pl.gov.coi.pomocua.ads.law.LawOfferVM;
import pl.gov.coi.pomocua.ads.materialaid.MaterialAidOfferVM;
import pl.gov.coi.pomocua.ads.other.OtherOfferVM;
import pl.gov.coi.pomocua.ads.translation.TranslationOfferVM;
import pl.gov.coi.pomocua.ads.transport.TransportOfferVM;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = AccommodationOfferVM.class, name = "ACCOMMODATION"),
        @JsonSubTypes.Type(value = MaterialAidOfferVM.class, name = "MATERIAL_AID"),
        @JsonSubTypes.Type(value = TransportOfferVM.class, name = "TRANSPORT"),
        @JsonSubTypes.Type(value = JobOfferVM.class, name = "JOB"),
        @JsonSubTypes.Type(value = LawOfferVM.class, name = "LAW"),
        @JsonSubTypes.Type(value = HealthOfferVM.class, name = "HEALTH"),
        @JsonSubTypes.Type(value = TranslationOfferVM.class, name = "TRANSLATION"),
        @JsonSubTypes.Type(value = OtherOfferVM.class, name = "OTHER")
})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
public @interface JsonBaseOfferVMInheritance {
}
