package pl.gov.coi.pomocua.ads.health;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.gov.coi.pomocua.ads.BaseOffer;
import pl.gov.coi.pomocua.ads.OfferNotFoundException;
import pl.gov.coi.pomocua.ads.Offers;
import pl.gov.coi.pomocua.ads.authentication.CurrentUser;
import pl.gov.coi.pomocua.ads.users.User;
import pl.gov.coi.pomocua.ads.users.UsersService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/", produces = MediaType.APPLICATION_JSON_VALUE)
public class HealthResource {

    private final HealthOfferRepository repository;
    private final CurrentUser currentUser;
    private final UsersService usersService;
    private final HealthOfferSpecifications specifications;

    @PostMapping("secure/health-care")
    @ResponseStatus(HttpStatus.CREATED)
    public HealthOffer create(@Valid @RequestBody HealthOfferDefinitionDTO offerDefinition) {
        HealthOffer offer = new HealthOffer();
        offerDefinition.applyTo(offer);

        User currentUser = usersService.getCurrentUser();
        offer.attachTo(currentUser);

        return repository.save(offer);
    }

    @DeleteMapping("secure/health-care/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        HealthOffer offer = repository.findByIdAndUserId(id, currentUser.getCurrentUserId())
                .orElseThrow(OfferNotFoundException::new);

        if (!offer.isActive()) return;

        offer.status = BaseOffer.Status.INACTIVE;
        repository.save(offer);
    }

    @GetMapping("health-care")
    public Offers<HealthOffer> list(Pageable pageRequest, HealthOfferSearchCriteria searchCriteria) {
        return Offers.page(repository.findAll(specifications.from(searchCriteria), pageRequest));
    }

    @GetMapping("health-care/{id}")
    public HealthOffer get(@PathVariable Long id) {
        return repository.findById(id).filter(BaseOffer::isActive).orElseThrow(OfferNotFoundException::new);
    }

    @PutMapping("secure/health-care/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id, @Valid @RequestBody HealthOfferDefinitionDTO update) {
        HealthOffer offer = repository.findByIdAndUserId(id, currentUser.getCurrentUserId())
                .filter(BaseOffer::isActive)
                .orElseThrow(OfferNotFoundException::new);

        update.applyTo(offer);

        repository.save(offer);
    }
}
