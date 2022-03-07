package pl.gov.coi.pomocua.ads.accomodations;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.gov.coi.pomocua.ads.Offers;
import pl.gov.coi.pomocua.ads.authentication.CurrentUser;

import javax.validation.Valid;

import static pl.gov.coi.pomocua.ads.Offers.page;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/", produces = MediaType.APPLICATION_JSON_VALUE)
public class AccommodationsResource {
    private final AccommodationsRepository repository;
    private final CurrentUser currentUser;

    @PostMapping("secure/accommodations")
    @ResponseStatus(HttpStatus.CREATED)
    public AccommodationOffer create(@Valid @RequestBody AccommodationOffer offer) {
        offer.id = null;
        offer.userId = currentUser.getCurrentUserId();
        return repository.save(offer);
    }

    @GetMapping("accommodations")
    public Offers<AccommodationOffer> list(@RequestParam(required = false, defaultValue = "1") Integer capacity, Pageable pageRequest) {
        return page(repository.findAllByGuestsIsGreaterThanEqual(capacity, pageRequest));
    }

    @GetMapping("accommodations/{region}/{city}")
    public Offers<AccommodationOffer> listByLocation(@PathVariable String region, @PathVariable String city, @RequestParam(defaultValue = "1") Integer capacity, Pageable pageRequest) {
        return page(repository.findAllByLocation_RegionIgnoreCaseAndLocation_CityIgnoreCaseAndGuestsIsGreaterThanEqual(region, city, capacity, pageRequest));
    }

    @GetMapping("accommodations/{id}")
    public ResponseEntity<AccommodationOffer> get(@PathVariable Long id) {
        return ResponseEntity.of(repository.findById(id));
    }
}
