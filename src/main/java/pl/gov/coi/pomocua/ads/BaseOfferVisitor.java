package pl.gov.coi.pomocua.ads;

import org.springframework.stereotype.Component;
import pl.gov.coi.pomocua.ads.accomodations.AccommodationOffer;
import pl.gov.coi.pomocua.ads.accomodations.AccommodationOfferVM;
import pl.gov.coi.pomocua.ads.health.HealthOffer;
import pl.gov.coi.pomocua.ads.health.HealthOfferVM;
import pl.gov.coi.pomocua.ads.job.JobOffer;
import pl.gov.coi.pomocua.ads.job.JobOfferVM;
import pl.gov.coi.pomocua.ads.law.LawOffer;
import pl.gov.coi.pomocua.ads.law.LawOfferVM;
import pl.gov.coi.pomocua.ads.materialaid.MaterialAidOffer;
import pl.gov.coi.pomocua.ads.materialaid.MaterialAidOfferVM;
import pl.gov.coi.pomocua.ads.other.OtherOffer;
import pl.gov.coi.pomocua.ads.other.OtherOfferVM;
import pl.gov.coi.pomocua.ads.translation.TranslationOffer;
import pl.gov.coi.pomocua.ads.translation.TranslationOfferVM;
import pl.gov.coi.pomocua.ads.transport.TransportOffer;
import pl.gov.coi.pomocua.ads.transport.TransportOfferVM;

@Component
public class BaseOfferVisitor {

    public AccommodationOfferVM visit(AccommodationOffer accommodationOffer, Language viewLang) {
        return AccommodationOfferVM.from(accommodationOffer, viewLang);
    }

    public HealthOfferVM visit(HealthOffer healthOffer, Language viewLang) {
        return HealthOfferVM.from(healthOffer, viewLang);
    }

    public JobOfferVM visit(JobOffer jobOffer, Language viewLang) {
        return JobOfferVM.from(jobOffer, viewLang);
    }

    public LawOfferVM visit(LawOffer lawOffer, Language viewLang) {
        return LawOfferVM.from(lawOffer, viewLang);
    }

    public MaterialAidOfferVM visit(MaterialAidOffer materialAidOffer, Language viewLang) {
        return MaterialAidOfferVM.from(materialAidOffer, viewLang);
    }

    public OtherOfferVM visit(OtherOffer otherOffer, Language viewLang) {
        return OtherOfferVM.from(otherOffer, viewLang);
    }

    public TranslationOfferVM visit(TranslationOffer translationOffer, Language viewLang) {
        return TranslationOfferVM.from(translationOffer, viewLang);
    }

    public TransportOfferVM visit(TransportOffer transportOffer, Language viewLang) {
        return TransportOfferVM.from(transportOffer, viewLang);
    }

}
