package pl.gov.coi.pomocua.ads.translation;

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
public class TranslationResource
        extends BaseOfferResource<TranslationOffer, TranslationOfferDefinitionDTO, TranslationOfferRepository> {

    private final TranslationOfferSpecifications specifications;

    public TranslationResource(TranslationOfferRepository repository,
                               CurrentUser currentUser,
                               UsersService usersService,
                               OffersTranslationUtil translationUtil,
                               TranslationOfferSpecifications specifications) {
        super(repository, currentUser, usersService, translationUtil);
        this.specifications = specifications;
    }

    @PostMapping("secure/translation")
    @ResponseStatus(HttpStatus.CREATED)
    public TranslationOfferVM create(@Valid @RequestBody TranslationOfferDefinitionDTO offerDefinition) {
        TranslationOffer offer = new TranslationOffer();
        return TranslationOfferVM.from(createOffer(offer, offerDefinition), Language.PL);
    }

    @DeleteMapping("secure/translation/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        deleteOffer(id);
    }

    @GetMapping("translation")
    public OffersVM<TranslationOfferVM> list(Pageable pageRequest, TranslationOfferSearchCriteria searchCriteria) {
        return OffersVM.page(repository.findAll(specifications.from(searchCriteria), pageRequest)
                .map(offer -> TranslationOfferVM.from(offer, searchCriteria.getLang())));
    }

    @GetMapping("translation/{id}")
    public TranslationOfferVM get(@PathVariable Long id, @RequestParam(required = false, defaultValue = "PL") Language lang) {
        return TranslationOfferVM.from(getOffer(id), lang);
    }

    @PutMapping("secure/translation/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id, @Valid @RequestBody TranslationOfferDefinitionDTO update) {
        updateOffer(id, update);
    }
}
