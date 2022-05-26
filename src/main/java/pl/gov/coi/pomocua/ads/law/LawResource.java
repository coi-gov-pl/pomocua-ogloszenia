package pl.gov.coi.pomocua.ads.law;

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
public class LawResource extends BaseOfferResource<LawOffer, LawOfferDefinitionDTO, LawOfferRepository> {

    private final LawOfferSpecifications specifications;

    public LawResource(LawOfferRepository repository,
                       CurrentUser currentUser,
                       UsersService usersService,
                       OffersTranslationUtil translationUtil,
                       LawOfferSpecifications specifications) {
        super(repository, currentUser, usersService, translationUtil);
        this.specifications = specifications;
    }

    @PostMapping("secure/law")
    @ResponseStatus(HttpStatus.CREATED)
    public LawOfferVM create(@Valid @RequestBody LawOfferDefinitionDTO offerDefinition) {
        LawOffer offer = new LawOffer();
        return LawOfferVM.from(createOffer(offer, offerDefinition), Language.PL);
    }

    @DeleteMapping("secure/law/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        deleteOffer(id);
    }

    @GetMapping("law")
    public OffersVM<LawOfferVM> list(Pageable pageRequest, LawOfferSearchCriteria searchCriteria) {
        return OffersVM.page(repository.findAll(specifications.from(searchCriteria), pageRequest)
                .map(offer -> LawOfferVM.from(offer, searchCriteria.getLang())));
    }

    @GetMapping("law/{id}")
    public LawOfferVM get(@PathVariable Long id, @RequestParam(required = false, defaultValue = "PL") Language lang) {
        return LawOfferVM.from(getOffer(id), lang);
    }

    @PutMapping("secure/law/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id, @Valid @RequestBody LawOfferDefinitionDTO update) {
        updateOffer(id, update);
    }
}
