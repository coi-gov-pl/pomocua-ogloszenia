package pl.gov.coi.pomocua.ads.other;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.gov.coi.pomocua.ads.BaseOfferResource;
import pl.gov.coi.pomocua.ads.Language;
import pl.gov.coi.pomocua.ads.OffersTranslationUtil;
import pl.gov.coi.pomocua.ads.OffersVM;
import pl.gov.coi.pomocua.ads.authentication.CurrentUser;
import pl.gov.coi.pomocua.ads.users.UsersService;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/", produces = MediaType.APPLICATION_JSON_VALUE)
public class OtherResource extends BaseOfferResource<OtherOffer, OtherOfferDefinitionDTO, OtherOfferRepository> {

    private final OtherOfferSpecifications specifications;

    public OtherResource(OtherOfferRepository repository,
                         CurrentUser currentUser,
                         UsersService usersService,
                         OffersTranslationUtil translationUtil,
                         OtherOfferSpecifications specifications) {
        super(repository, currentUser, usersService, translationUtil);
        this.specifications = specifications;
    }

    @PostMapping("secure/other")
    @ResponseStatus(HttpStatus.CREATED)
    public OtherOfferVM create(@Valid @RequestBody OtherOfferDefinitionDTO offerDefinition) {
        OtherOffer offer = new OtherOffer();
        return OtherOfferVM.from(createOffer(offer, offerDefinition), Language.PL);
    }

    @DeleteMapping("secure/other/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        deleteOffer(id);
    }

    @GetMapping("other")
    public OffersVM<OtherOfferVM> list(Pageable pageRequest, OtherOfferSearchCriteria searchCriteria) {
        return OffersVM.page(repository.findAll(specifications.from(searchCriteria), pageRequest)
                .map(offer -> OtherOfferVM.from(offer, searchCriteria.getLang())));
    }

    @GetMapping("other/{id}")
    public OtherOfferVM get(@PathVariable Long id, @RequestParam(required = false, defaultValue = "PL") Language lang) {
        return OtherOfferVM.from(getOffer(id), lang);
    }

    @PutMapping("secure/other/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id, @Valid @RequestBody OtherOfferDefinitionDTO update) {
        updateOffer(id, update);
    }
}
