package pl.gov.coi.pomocua.ads.transport;

import lombok.Builder;
import pl.gov.coi.pomocua.ads.Location;

import java.time.LocalDate;
import java.util.Optional;

public class TransportTestDataGenerator {
    public static TransportOfferBuilder aTransportOffer() {
        return TransportTestDataGenerator.builder()
                .title("some title")
                .description("some description")
                .capacity(1)
                .origin(new Location("mazowieckie", "warszawa"))
                .destination(new Location("pomorskie", "gdańsk"))
                .transportDate(LocalDate.now())
                ;
    }

    @Builder
    private static TransportOffer transportOfferBuilder(
            String title,
            String description,
            Location origin,
            Location destination,
            Integer capacity,
            LocalDate transportDate
    ) {
        TransportOffer result = new TransportOffer();
        result.title = Optional.ofNullable(title).orElse("some title");
        result.description = Optional.ofNullable(description).orElse("some description");
        result.origin = origin;
        result.destination = destination;
        result.capacity = capacity;
        result.transportDate = transportDate;
        return result;
    }
}
