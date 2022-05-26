package pl.gov.coi.pomocua.ads.accomodations;

import pl.gov.coi.pomocua.ads.Language;
import pl.gov.coi.pomocua.ads.Location;

import java.util.List;

public class AccommodationsTestDataGenerator {
    public static AccommodationOfferVM sampleOffer() {
        AccommodationOfferVM request = new AccommodationOfferVM();
        request.setTitle("sample work");
        request.setLocation(new Location("Mazowieckie", "Warszawa"));
        request.setHostLanguage(List.of(Language.PL, Language.UA));
        request.setDescription("description");
        request.setLengthOfStay(AccommodationOffer.LengthOfStay.MONTH_2);
        request.setGuests(5);
        request.setPhoneNumber("+48123456789");
        return request;
    }

    public static AccommodationOfferDefinitionDTO sampleUpdateJson() {
        var updateJson = new AccommodationOfferDefinitionDTO();
        updateJson.title = "new title";
        updateJson.description = "new description";
        updateJson.location = new Location("Pomorskie", "Gdańsk");
        updateJson.guests = 14;
        updateJson.lengthOfStay = AccommodationOffer.LengthOfStay.MONTH_3;
        updateJson.hostLanguage = List.of(Language.UA);
        updateJson.phoneNumber = "+48123456780";
        return updateJson;
    }
}
