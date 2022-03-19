package pl.gov.coi.pomocua.ads.transport;

import lombok.Builder;
import pl.gov.coi.pomocua.ads.BaseOffer;
import pl.gov.coi.pomocua.ads.Location;

import java.time.LocalDate;
import java.util.Optional;

public class TransportTestDataGenerator {
    public static TransportOfferBuilder aTransportOffer() {
        return TransportTestDataGenerator.builder()
                .title("some title")
                .description("some description")
                .capacity(1)
                .origin(new Location("mazowieckie", "Warszawa"))
                .destination(new Location("pomorskie", "Gdańsk"))
                .transportDate(LocalDate.of(2022, 2, 25))
                .phoneNumber("481234567890")
                ;
    }

    public static TransportOfferDefinitionDTO sampleUpdateJson() {
        var updateJson = new TransportOfferDefinitionDTO();
        updateJson.title = "new title";
        updateJson.description = "new description";
        updateJson.origin = new Location("dolnośląskie", "Wrocław");
        updateJson.destination = new Location("podlaskie", "Białystok");
        updateJson.capacity = 35;
        updateJson.transportDate = LocalDate.of(2022, 3, 8);
        updateJson.phoneNumber = "481234567890";
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
        result.status = Optional.ofNullable(status).orElse(BaseOffer.Status.ACTIVE);
        return result;
    }
}
