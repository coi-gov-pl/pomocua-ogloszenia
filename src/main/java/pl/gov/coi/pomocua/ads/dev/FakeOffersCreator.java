package pl.gov.coi.pomocua.ads.dev;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pl.gov.coi.pomocua.ads.Language;
import pl.gov.coi.pomocua.ads.Location;
import pl.gov.coi.pomocua.ads.accomodations.AccommodationOffer;
import pl.gov.coi.pomocua.ads.accomodations.AccommodationsRepository;
import pl.gov.coi.pomocua.ads.authentication.CurrentUser;
import pl.gov.coi.pomocua.ads.health.HealthOffer;
import pl.gov.coi.pomocua.ads.health.HealthOfferRepository;
import pl.gov.coi.pomocua.ads.health.HealthCareSpecialization;
import pl.gov.coi.pomocua.ads.job.JobOffer;
import pl.gov.coi.pomocua.ads.job.JobOfferRepository;
import pl.gov.coi.pomocua.ads.law.LawOffer;
import pl.gov.coi.pomocua.ads.law.LawOfferRepository;
import pl.gov.coi.pomocua.ads.materialaid.MaterialAidCategory;
import pl.gov.coi.pomocua.ads.materialaid.MaterialAidOffer;
import pl.gov.coi.pomocua.ads.materialaid.MaterialAidOfferRepository;
import pl.gov.coi.pomocua.ads.transport.TransportOffer;
import pl.gov.coi.pomocua.ads.transport.TransportOfferRepository;
import pl.gov.coi.pomocua.ads.job.JobOffer.Mode;
import pl.gov.coi.pomocua.ads.job.JobOffer.Industry;
import pl.gov.coi.pomocua.ads.job.JobOffer.ContractType;
import pl.gov.coi.pomocua.ads.job.JobOffer.WorkTime;
import pl.gov.coi.pomocua.ads.law.LawOffer.HelpKind;
import pl.gov.coi.pomocua.ads.law.LawOffer.HelpMode;
import pl.gov.coi.pomocua.ads.health.HealthOffer.HealthCareMode;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.List;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class FakeOffersCreator {

    private final TransportOfferRepository transportOfferRepository;
    private final AccommodationsRepository accommodationsRepository;
    private final MaterialAidOfferRepository materialAidOfferRepository;
    private final JobOfferRepository jobOfferRepository;
    private final LawOfferRepository lawOfferRepository;
    private final HealthOfferRepository healthOfferRepository;
    private final CurrentUser currentUser;

    @PostConstruct
    public void transport() {
        TransportOffer o1 = new TransportOffer();
        o1.title = "Transport busem 8osobowy";
        o1.description = "Witam, mam busa 8 osobowego jestem wstanie pomóż w transporcie. " +
                        "Mogę też przewieź rzeczy pod granice.";
        o1.userId = currentUser.getCurrentUserId();
        o1.userFirstName = "Marta";
        o1.origin = new Location("Pomorskie", "Gdynia");
        o1.destination = new Location("Pomorskie", "Gdynia");
        o1.capacity = 11;
        o1.transportDate = LocalDate.now();
        o1.phoneNumber = "+48123456789";

        TransportOffer o2 = new TransportOffer();
        o2.title = "Darmowy transport na granicę i z granicy z Ostrowa i okolic";
        o2.description = "Darmowy transport z Ostrowa i okolic na granicę z Ukraniną i z granicy " +
                        "mam 4 miejsca mam foteliki dla dzieci najleipiej w weekend";
        o2.userId = currentUser.getCurrentUserId();
        o2.userFirstName = "Mariusz";
        o2.origin = new Location("Pomorskie", "Gdańsk");
        o2.destination = new Location("Mazowieckie", "Warszawa");
        o2.capacity = 10;
        o2.transportDate = LocalDate.now();
        o2.phoneNumber = "+48123456780";

        transportOfferRepository.save(o1);
        transportOfferRepository.save(o2);
    }

    @PostConstruct
    public void accommodation() {
        AccommodationOffer o1 = new AccommodationOffer();
        o1.title = "Mieszkanie w bloku, 2 osoby - Rzeszów, woj. podkarpackie";
        o1.description = "nocleg noclegmazowieckie transport Dolnośląskie, miejscowość Wrocław - ok. 5 km od Dworca głównego. Kawalerka na wyłączność pomieści 2 osoby + zwierzęta są mile widziane. Okres: 2 miesiące, Bezpłatnie....";
        o1.userId = currentUser.getCurrentUserId();
        o1.userFirstName = "Basia";
        o1.location = new Location("podkarpackie", "Rzeszów");
        o1.hostLanguage = List.of(Language.PL, Language.UA);
        o1.guests = 2;
        o1.lengthOfStay = AccommodationOffer.LengthOfStay.MONTH_2;
        o1.phoneNumber = "+48123456789";

        AccommodationOffer o2 = new AccommodationOffer();
        o2.title = "Mieszkanie w bloku, 4 osoby - Międzygórze, woj. podlaskie";
        o2.description = "Kawalerka na wyłączność pomieści 2 osoby + zwierzęta są mile widziane. Okres: 2 miesiące, Bezpłatnie....";
        o2.userId = currentUser.getCurrentUserId();
        o2.userFirstName = "Piotr";
        o2.location = new Location("podlaskie", "Międzygórze");
        o2.hostLanguage = List.of(Language.PL, Language.UA);
        o2.guests = 4;
        o2.lengthOfStay = AccommodationOffer.LengthOfStay.LONGER;
        o2.phoneNumber = "+48123456780";

        accommodationsRepository.save(o1);
        accommodationsRepository.save(o2);
    }

    @PostConstruct
    public void materialAid() {
        MaterialAidOffer o1 = new MaterialAidOffer();
        o1.title = "Oddam materac dwuosobowy";
        o1.description = "Materac w bardzo dobrym stanie, do odbioru w Gdańsku";
        o1.userId = currentUser.getCurrentUserId();
        o1.userFirstName = "Krystyna";
        o1.category = MaterialAidCategory.HOUSEHOLD_GOODS;
        o1.location = new Location("Pomorskie", "Gdańsk");
        o1.phoneNumber = "+48123456789";

        MaterialAidOffer o2 = new MaterialAidOffer();
        o2.title = "Mam do oddania zabawki dziecięce";
        o2.description = "worek zabawek do oddania, wszystkie w dobrym stanie, dla dziecka w wieku 5-10 lat";
        o2.userId = currentUser.getCurrentUserId();
        o2.userFirstName = "Maria";
        o2.category = MaterialAidCategory.FOR_CHILDREN;
        o2.location = new Location("Mazowieckie", "Warszawa");
        o2.phoneNumber = "+48123456780";

        materialAidOfferRepository.save(o1);
        materialAidOfferRepository.save(o2);
    }

    @PostConstruct
    public void jobOffer() {
        JobOffer o1 = new JobOffer();
        o1.title = "Praca w księgowości";
        o1.description = "Praca w księgowości na cały lub pół etatu, stacjonarnie - Gdańsk";
        o1.userId = currentUser.getCurrentUserId();
        o1.userFirstName = "Małgorzata";
        o1.mode = Mode.ONSITE;
        o1.setWorkTime(List.of(WorkTime.FULL_TIME, WorkTime.PART_TIME));
        o1.setContractType(List.of(ContractType.EMPLOYMENT));
        o1.industry = Industry.FINANCES;
        o1.setLanguage(List.of(Language.PL, Language.EN));
        o1.location = new Location("Pomorskie", "Gdańsk");
        o1.phoneNumber = "+48789234567";

        JobOffer o2 = new JobOffer();
        o2.title = "Praca w salonie medycyny estetycznej";
        o2.description = "Praca w salonie medycyny estetycznej, cały etat, Warszawa";
        o2.userId = currentUser.getCurrentUserId();
        o2.userFirstName = "Ewelina";
        o2.mode = Mode.ONSITE;
        o2.setWorkTime(List.of(WorkTime.FULL_TIME));
        o2.setContractType(List.of(ContractType.EMPLOYMENT));
        o2.industry = Industry.BEAUTY;
        o2.setLanguage(List.of(Language.PL, Language.EN, Language.UA));
        o2.location = new Location("Mazowieckie", "Warszawa");
        o2.phoneNumber = "+48654321778";

        jobOfferRepository.save(o1);
        jobOfferRepository.save(o2);
    }

    @PostConstruct
    public void lawOffer() {
        LawOffer o1 = new LawOffer();
        o1.title = "Pomoc prawna dla uchodźców";
        o1.description = "Pomoc prawna w zakresie prawa pracy";
        o1.userId = currentUser.getCurrentUserId();
        o1.userFirstName = "Małgorzata";
        o1.setHelpMode(List.of(HelpMode.ONLINE, HelpMode.BY_PHONE));
        o1.setHelpKind(List.of(HelpKind.LABOUR_LAW));
        o1.setLanguage(List.of(Language.PL, Language.EN));
        o1.location = new Location("Pomorskie", "Gdańsk");
        o1.phoneNumber = "+48789234567";

        LawOffer o2 = new LawOffer();
        o2.title = "Pomoc prawna - telefonicznie";
        o2.description = "Telefonicznie udzielę pomocy prawnej";
        o2.userId = currentUser.getCurrentUserId();
        o2.userFirstName = "Ewelina";
        o2.setHelpMode(List.of(HelpMode.BY_PHONE));
        o2.setHelpKind(List.of(HelpKind.IMMIGRATION_LAW, HelpKind.FAMILY_LAW));
        o2.setLanguage(List.of(Language.PL, Language.EN, Language.UA));
        o2.location = new Location("Mazowieckie", "Warszawa");
        o2.phoneNumber = "+48654321778";

        lawOfferRepository.save(o1);
        lawOfferRepository.save(o2);
    }

    @PostConstruct
    public void healthOffer() {
        HealthOffer o1 = new HealthOffer();
        o1.title = "Pomoc zdrowotna - medycyna ogólna";
        o1.description = "Pomoc zdrowotna w zakresie medycyny ogólnej, stacjonarnie Gdańsk lub telefonicznie";
        o1.userId = currentUser.getCurrentUserId();
        o1.userFirstName = "Małgorzata";
        o1.specialization = HealthCareSpecialization.GENERAL;
        o1.setMode(List.of(HealthCareMode.IN_FACILITY, HealthCareMode.BY_PHONE));
        o1.setLanguage(List.of(Language.PL, Language.EN));
        o1.location = new Location("Pomorskie", "Gdańsk");
        o1.phoneNumber = "+48789234567";

        HealthOffer o2 = new HealthOffer();
        o2.title = "Pomoc zdrowotna - pediatria";
        o2.description = "Pomoc zdrowotna w zakresie pediatrii, stacjonarnie Warszawa, online, telefonicznie";
        o2.userId = currentUser.getCurrentUserId();
        o2.userFirstName = "Ewelina";
        o2.specialization = HealthCareSpecialization.PEDIATRICS;
        o2.setMode(List.of(HealthCareMode.AT_HOME, HealthCareMode.IN_FACILITY, HealthCareMode.ONLINE));
        o2.setLanguage(List.of(Language.PL, Language.EN, Language.UA));
        o2.location = new Location("Mazowieckie", "Warszawa");
        o2.phoneNumber = "+48654321778";

        healthOfferRepository.save(o1);
        healthOfferRepository.save(o2);
    }
}
