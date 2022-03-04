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
        TransportOffer transportOffer1 = new TransportOffer();
        transportOffer1.id = 1L;
        transportOffer1.description = "Darmowy transport z Ostrowa i okolic na granicę z Ukraniną i z granicy " +
                "mam 4 miejsca mam foteliki dla dzieci najleipiej w weekend";
        transportOffer1.title = "Darmowy transport na granicę i z granicy z Ostrowa i okolic";
        transportOffer1.origin = new Location("Pomorskie", "Gdańsk");
        transportOffer1.capacity = 10;
        transportOfferRepository.save(transportOffer1);

        TransportOffer transportOffer2 = new TransportOffer();
        transportOffer2.id = 2L;
        transportOffer2.description = "Witam, mam busa 8 osobowego jestem wstanie pomóż w transporcie. " +
                "Mogę też przewieź rzeczy pod granice.";
        transportOffer2.title = "Transport busem 8osobowy";
        transportOffer2.origin = new Location("Pomorskie", "Gdynia");
        transportOffer2.capacity = 10;
        transportOfferRepository.save(transportOffer2);
    }
}
