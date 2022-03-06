package pl.gov.coi.pomocua.ads.accomodations;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.gov.coi.pomocua.ads.authentication.CurrentUser;

import javax.validation.Valid;

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
    public Page<AccommodationOffer> list(@RequestParam(required = false, defaultValue = "1") Integer capacity, Pageable pageRequest) {
        return repository.findAllByGuestsIsGreaterThanEqual(capacity, pageRequest);
    }

    @GetMapping("accommodations/{region}/{city}")
    public Page<AccommodationOffer> list(@PathVariable String region, @PathVariable String city, @RequestParam(defaultValue = "1") Integer capacity, Pageable pageRequest) {
        return repository.findAllByLocation_RegionIgnoreCaseAndLocation_CityIgnoreCaseAndGuestsIsGreaterThanEqual(region, city, capacity, pageRequest);
    }

    @GetMapping("accommodations/{id}")
    public ResponseEntity<AccommodationOffer> get(@PathVariable Long id) {
        return ResponseEntity.of(repository.findById(id));
    }
}
