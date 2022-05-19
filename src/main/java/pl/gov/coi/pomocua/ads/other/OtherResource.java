package pl.gov.coi.pomocua.ads.other;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
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
public class OtherResource {

    private final OtherOfferRepository repository;
    private final CurrentUser currentUser;
    private final UsersService usersService;

    @PostMapping("secure/other")
    @ResponseStatus(HttpStatus.CREATED)
    public OtherOffer create(@Valid @RequestBody OtherOfferDefinitionDTO offerDefinition) {
        OtherOffer offer = new OtherOffer();
        offerDefinition.applyTo(offer);

        User currentUser = usersService.getCurrentUser();
        offer.attachTo(currentUser);

        return repository.save(offer);
    }

    @DeleteMapping("secure/other/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        OtherOffer offer = repository.findByIdAndUserId(id, currentUser.getCurrentUserId())
                .orElseThrow(OfferNotFoundException::new);

        if (!offer.isActive()) return;

        offer.status = BaseOffer.Status.INACTIVE;
        repository.save(offer);
    }

    //TODO searchCriteria
    @GetMapping("other")
    public Offers<OtherOffer> list(Pageable pageRequest) {
        return Offers.page(repository.findAll(pageRequest));
    }

    @GetMapping("other/{id}")
    public OtherOffer get(@PathVariable Long id) {
        return repository.findById(id).filter(BaseOffer::isActive).orElseThrow(OfferNotFoundException::new);
    }

    @PutMapping("secure/other/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id, @Valid @RequestBody OtherOfferDefinitionDTO update) {
        OtherOffer offer = repository.findByIdAndUserId(id, currentUser.getCurrentUserId())
                .filter(BaseOffer::isActive)
                .orElseThrow(OfferNotFoundException::new);

        update.applyTo(offer);

        repository.save(offer);
    }
}
