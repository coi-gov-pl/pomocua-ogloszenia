package pl.gov.coi.pomocua.ads.health;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.gov.coi.pomocua.ads.Language;
import pl.gov.coi.pomocua.ads.OffersVM;
import pl.gov.coi.pomocua.ads.authentication.CurrentUser;
import pl.gov.coi.pomocua.ads.BaseOfferResource;
import pl.gov.coi.pomocua.ads.OffersTranslationUtil;
import pl.gov.coi.pomocua.ads.users.UsersService;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/", produces = MediaType.APPLICATION_JSON_VALUE)
public class HealthResource extends BaseOfferResource<HealthOffer, HealthOfferDefinitionDTO, HealthOfferRepository> {

    private final HealthOfferSpecifications specifications;

    public HealthResource(HealthOfferRepository repository,
                          CurrentUser currentUser,
                          UsersService usersService,
                          OffersTranslationUtil translationUtil,
                          HealthOfferSpecifications specifications) {
        super(repository, currentUser, usersService, translationUtil);
        this.specifications = specifications;
    }

    @PostMapping("secure/health-care")
    @ResponseStatus(HttpStatus.CREATED)
    public HealthOfferVM create(@Valid @RequestBody HealthOfferDefinitionDTO offerDefinition) {
        HealthOffer offer = new HealthOffer();
        return HealthOfferVM.from(createOffer(offer, offerDefinition), Language.PL);
    }

    @DeleteMapping("secure/health-care/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        deleteOffer(id);
    }

    @GetMapping("health-care")
    public OffersVM<HealthOfferVM> list(Pageable pageRequest, HealthOfferSearchCriteria searchCriteria) {
        return OffersVM.page(repository.findAll(specifications.from(searchCriteria), pageRequest)
                .map(offer -> HealthOfferVM.from(offer, searchCriteria.getLang())));
    }

    @GetMapping("health-care/{id}")
    public HealthOfferVM get(@PathVariable Long id, @RequestParam(required = false, defaultValue = "PL") Language lang) {
        return HealthOfferVM.from(getOffer(id), lang);
    }

    @PutMapping("secure/health-care/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id, @Valid @RequestBody HealthOfferDefinitionDTO update) {
        updateOffer(id, update);
    }
}
