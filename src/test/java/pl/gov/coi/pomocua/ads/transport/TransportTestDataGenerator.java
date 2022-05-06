package pl.gov.coi.pomocua.ads.transport;

import lombok.Builder;
import pl.gov.coi.pomocua.ads.BaseOffer;
import pl.gov.coi.pomocua.ads.Location;

import java.time.LocalDate;
import java.util.Optional;

public class TransportTestDataGenerator {

    public static final LocalDate TRANSPORT_DATE = LocalDate.now().plusDays(2L);

    public static TransportOfferBuilder aTransportOffer() {
        return TransportTestDataGenerator.builder()
                .title("some title")
                .description("some description")
                .capacity(1)
                .origin(new Location("mazowieckie", "Warszawa"))
                .destination(new Location("pomorskie", "Gdańsk"))
                .transportDate(TRANSPORT_DATE)
                .phoneNumber("+48123456789")
                ;
    }

    public static TransportOfferDefinitionDTO sampleUpdateJson() {
        var updateJson = new TransportOfferDefinitionDTO();
        updateJson.title = "new title";
        updateJson.description = "new description";
        updateJson.origin = new Location("dolnośląskie", "Wrocław");
        updateJson.destination = new Location("podlaskie", "Białystok");
        updateJson.capacity = 35;
        updateJson.transportDate = TRANSPORT_DATE;
        updateJson.phoneNumber = "+48123456780";
        return updateJson;
    }

    @Builder
    private static TransportOffer transportOfferBuilder(
            String title,
            String description,
            Location origin,
            Location destination,
            Integer capacity,
            LocalDate transportDate,
            String phoneNumber,
            String phoneCountryCode,
            BaseOffer.Status status
    ) {
        TransportOffer result = new TransportOffer();
        result.title = Optional.ofNullable(title).orElse("some title");
        result.description = Optional.ofNullable(description).orElse("some description");
        result.origin = origin;
        result.destination = destination;
        result.capacity = capacity;
        result.transportDate = transportDate;
        result.phoneNumber = phoneNumber;
        result.phoneCountryCode = phoneCountryCode;
        result.status = Optional.ofNullable(status).orElse(BaseOffer.Status.ACTIVE);
        return result;
    }
}
