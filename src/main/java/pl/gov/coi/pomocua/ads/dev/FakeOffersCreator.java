package pl.gov.coi.pomocua.ads.dev;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pl.gov.coi.pomocua.ads.Location;
import pl.gov.coi.pomocua.ads.UserId;
import pl.gov.coi.pomocua.ads.accomodations.AccommodationOffer;
import pl.gov.coi.pomocua.ads.accomodations.AccommodationsRepository;
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
                new Location("Pomorskie", "Gdańsk"), null, 10
        );

        transportOfferRepository.save(t1);
        transportOfferRepository.save(t2);
    }

    @PostConstruct
    public void accommodation() {
        AccommodationOffer o1 = new AccommodationOffer();
        o1.title = "Mieszkanie w bloku, 2 osoby - Rzeszów, woj. podkarpackie";
        o1.description = "#nocleg #noclegmazowieckie #transport Dolnośląskie, miejscowość Wrocław - ok. 5 km od Dworca głównego. Kawalerka na wyłączność pomieści 2 osoby + zwierzęta są mile widziane. Okres: 2 miesiące, Bezpłatnie....";
        o1.userId = new UserId("1");
        o1.location = new Location("podkarpackie", "Rzeszów");
        o1.hostLanguage = List.of(AccommodationOffer.Language.PL, AccommodationOffer.Language.UA);
        o1.guests = 2;

        AccommodationOffer o2 = new AccommodationOffer();
        o2.title = "Mieszkanie w bloku, 4 osoby - Międzygórze, woj. podlaskie";
        o2.description = "Kawalerka na wyłączność pomieści 2 osoby + zwierzęta są mile widziane. Okres: 2 miesiące, Bezpłatnie....";
        o2.userId = new UserId("2");
        o2.location = new Location("podlaskie", "Międzygórze");
        o2.hostLanguage = List.of(AccommodationOffer.Language.PL, AccommodationOffer.Language.UA);
        o2.guests = 4;

        accommodationsRepository.save(o1);
        accommodationsRepository.save(o2);
    }

}
