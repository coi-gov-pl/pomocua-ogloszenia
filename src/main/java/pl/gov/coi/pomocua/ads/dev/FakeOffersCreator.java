package pl.gov.coi.pomocua.ads.dev;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pl.gov.coi.pomocua.ads.Location;
import pl.gov.coi.pomocua.ads.UserId;
import pl.gov.coi.pomocua.ads.accomodations.AccommodationOffer;
import pl.gov.coi.pomocua.ads.accomodations.AccommodationsRepository;
import pl.gov.coi.pomocua.ads.materialaid.MaterialAidCategory;
import pl.gov.coi.pomocua.ads.materialaid.MaterialAidOffer;
import pl.gov.coi.pomocua.ads.materialaid.MaterialAidOfferRepository;
import pl.gov.coi.pomocua.ads.transport.TransportOffer;
import pl.gov.coi.pomocua.ads.transport.TransportOfferRepository;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class FakeOffersCreator {

    private final TransportOfferRepository transportOfferRepository;
    private final AccommodationsRepository accommodationsRepository;
    private final MaterialAidOfferRepository materialAidOfferRepository;

    @PostConstruct
    public void transport() {
        TransportOffer t1 = TransportOffer.of(
                "Transport busem 8osobowy",
                "Witam, mam busa 8 osobowego jestem wstanie pomóż w transporcie. " +
                        "Mogę też przewieź rzeczy pod granice.",
                new UserId("1"),
                new Location("Pomorskie", "Gdynia"), new Location("Pomorskie", "Gdynia"), 11
        );
        TransportOffer t2 = TransportOffer.of(
                "Darmowy transport na granicę i z granicy z Ostrowa i okolic",
                "Darmowy transport z Ostrowa i okolic na granicę z Ukraniną i z granicy " +
                        "mam 4 miejsca mam foteliki dla dzieci najleipiej w weekend",
                new UserId("2"),
                new Location("Pomorskie", "Gdańsk"), new Location("Mazowieckie", "Warszawa"), 10
        );

        transportOfferRepository.save(t1);
        transportOfferRepository.save(t2);
    }

    @PostConstruct
    public void accommodation() {
        AccommodationOffer o1 = new AccommodationOffer();
        o1.title = "Mieszkanie w bloku, 2 osoby - Rzeszów, woj. podkarpackie";
        o1.description = "nocleg noclegmazowieckie transport Dolnośląskie, miejscowość Wrocław - ok. 5 km od Dworca głównego. Kawalerka na wyłączność pomieści 2 osoby + zwierzęta są mile widziane. Okres: 2 miesiące, Bezpłatnie....";
        o1.userId = new UserId("1");
        o1.location = new Location("podkarpackie", "Rzeszów");
        o1.hostLanguage = List.of(AccommodationOffer.Language.PL, AccommodationOffer.Language.UA);
        o1.guests = 2;
        o1.lengthOfStay = AccommodationOffer.LengthOfStay.MONTH_2;

        AccommodationOffer o2 = new AccommodationOffer();
        o2.title = "Mieszkanie w bloku, 4 osoby - Międzygórze, woj. podlaskie";
        o2.description = "Kawalerka na wyłączność pomieści 2 osoby + zwierzęta są mile widziane. Okres: 2 miesiące, Bezpłatnie....";
        o2.userId = new UserId("2");
        o2.location = new Location("podlaskie", "Międzygórze");
        o2.hostLanguage = List.of(AccommodationOffer.Language.PL, AccommodationOffer.Language.UA);
        o2.guests = 4;
        o2.lengthOfStay = AccommodationOffer.LengthOfStay.LONGER;

        accommodationsRepository.save(o1);
        accommodationsRepository.save(o2);
    }

    @PostConstruct
    public void materialAid() {
        MaterialAidOffer o1 = new MaterialAidOffer();
        o1.title = "Oddam materac dwuosobowy";
        o1.description = "Materac w bardzo dobrym stanie, do odbioru w Gdańsku";
        o1.userId = new UserId("3");
        o1.category = MaterialAidCategory.HOUSEHOLD_GOODS;
        o1.location = new Location("Pomorskie", "Gdańsk");

        MaterialAidOffer o2 = new MaterialAidOffer();
        o2.title = "Mam do oddania zabawki dziecięce";
        o2.description = "worek zabawek do oddania, wszystkie w dobrym stanie, dla dziecka w wieku 5-10 lat";
        o2.userId = new UserId("1");
        o2.category = MaterialAidCategory.FOR_CHILDREN;
        o2.location = new Location("Mazowieckie", "Warszawa");

        materialAidOfferRepository.save(o1);
        materialAidOfferRepository.save(o2);
    }
}
