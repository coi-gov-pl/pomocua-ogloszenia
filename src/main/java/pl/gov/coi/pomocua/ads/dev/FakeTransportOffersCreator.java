package pl.gov.coi.pomocua.ads.dev;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pl.gov.coi.pomocua.ads.Location;
import pl.gov.coi.pomocua.ads.transport.TransportOffer;
import pl.gov.coi.pomocua.ads.transport.TransportOfferRepository;

import javax.annotation.PostConstruct;

@Component
@Profile("dev")
public class FakeTransportOffersCreator {

    private final TransportOfferRepository transportOfferRepository;

    public FakeTransportOffersCreator(TransportOfferRepository transportOfferRepository) {
        this.transportOfferRepository = transportOfferRepository;
    }

    @PostConstruct
    public void create() {
        TransportOffer t1 = TransportOffer.of(
                "Transport busem 8osobowy",
                "Witam, mam busa 8 osobowego jestem wstanie pomóż w transporcie. " +
                        "Mogę też przewieź rzeczy pod granice.",
                new Location("Pomorskie", "Gdynia"), new Location("Pomorskie", "Gdynia"), 11
        );
        TransportOffer t2 = TransportOffer.of(
                "Darmowy transport na granicę i z granicy z Ostrowa i okolic",
                "Darmowy transport z Ostrowa i okolic na granicę z Ukraniną i z granicy " +
                        "mam 4 miejsca mam foteliki dla dzieci najleipiej w weekend",
                new Location("Pomorskie", "Gdańsk"), new Location("Mazowieckie", "Warszawa"), 10
        );

        transportOfferRepository.save(t1);
        transportOfferRepository.save(t2);
    }

}
