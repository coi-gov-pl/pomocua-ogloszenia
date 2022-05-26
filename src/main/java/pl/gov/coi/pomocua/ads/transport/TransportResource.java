package pl.gov.coi.pomocua.ads.transport;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.PageRequest;
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
public class TransportResource
        extends BaseOfferResource<TransportOffer, TransportOfferDefinitionDTO, TransportOfferRepository> {

    private final TransportOfferSpecifications specifications;

    public TransportResource(TransportOfferRepository repository,
                             CurrentUser currentUser,
                             UsersService usersService,
                             OffersTranslationUtil translationUtil,
                             TransportOfferSpecifications specifications) {
        super(repository, currentUser, usersService, translationUtil);
        this.specifications = specifications;
    }

    @PostMapping("secure/transport")
    @ResponseStatus(HttpStatus.CREATED)
    public TransportOfferVM create(@Valid @RequestBody TransportOfferDefinitionDTO offerDefinition) {
        TransportOffer offer = new TransportOffer();
        return TransportOfferVM.from(createOffer(offer, offerDefinition), Language.PL);
    }

    @DeleteMapping("secure/transport/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        deleteOffer(id);
    }

    @Operation(description = "Allows to search for transport offers using different criterias (passes as query params). Each criteria is optional.")
    @GetMapping("transport")
    public OffersVM<TransportOfferVM> list(Pageable pageRequest, TransportOfferSearchCriteria searchCriteria) {

        Pageable pageable = TransportSort.modifySort((PageRequest) pageRequest, searchCriteria);
        return OffersVM.page(repository.findAll(specifications.from(searchCriteria), pageable)
                .map(offer -> TransportOfferVM.from(offer, searchCriteria.getLang())));
    }

    @GetMapping("transport/{id}")
    public TransportOfferVM get(@PathVariable Long id, @RequestParam(required = false, defaultValue = "PL") Language lang) {
        return TransportOfferVM.from(getOffer(id), lang);
    }

    @PutMapping("secure/transport/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id, @Valid @RequestBody TransportOfferDefinitionDTO update) {
        updateOffer(id, update);
    }
}
