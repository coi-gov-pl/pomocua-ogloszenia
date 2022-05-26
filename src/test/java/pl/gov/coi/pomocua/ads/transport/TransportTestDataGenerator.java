package pl.gov.coi.pomocua.ads.transport;

import lombok.Builder;
import pl.gov.coi.pomocua.ads.Language;
import pl.gov.coi.pomocua.ads.BaseOffer;
import pl.gov.coi.pomocua.ads.Location;

import java.time.LocalDate;
import java.util.Optional;

public class TransportTestDataGenerator {

    public static final LocalDate TRANSPORT_DATE = LocalDate.now().plusDays(2L);

    public static TransportOfferVMBuilder aTransportOffer() {
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
    private static TransportOfferVM transportOfferVMBuilder(
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
        TransportOfferVM result = new TransportOfferVM();
        result.setTitle(Optional.ofNullable(title).orElse("some title"));
        result.setDescription(Optional.ofNullable(description).orElse("some description"));
        result.setOrigin(origin);
        result.setDestination(destination);
        result.setCapacity(capacity);
        result.setTransportDate(transportDate);
        result.setPhoneNumber(phoneNumber);
        result.setPhoneCountryCode(phoneCountryCode);
        result.setStatus(Optional.ofNullable(status).orElse(BaseOffer.Status.ACTIVE));
        return result;
    }
}
