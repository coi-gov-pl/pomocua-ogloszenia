package pl.gov.coi.pomocua.ads.materialaid;

import io.swagger.v3.oas.annotations.Operation;
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
public class MaterialAidResource
        extends BaseOfferResource<MaterialAidOffer, MaterialAidOfferDefinitionDTO, MaterialAidOfferRepository> {

    private final MaterialAidOfferSpecifications specifications;

    public MaterialAidResource(MaterialAidOfferRepository repository,
                               CurrentUser currentUser,
                               UsersService usersService,
                               OffersTranslationUtil translationUtil,
                               MaterialAidOfferSpecifications specifications) {
        super(repository, currentUser, usersService, translationUtil);
        this.specifications = specifications;
    }

    @Operation(description = "Creates material aid offer")
    @PostMapping(value = "secure/material-aid")
    @ResponseStatus(HttpStatus.CREATED)
    public MaterialAidOfferVM postMaterialAidOffer(@Valid @RequestBody MaterialAidOfferDefinitionDTO offerDefinition) {
        MaterialAidOffer materialAidOffer = new MaterialAidOffer();
        return MaterialAidOfferVM.from(createOffer(materialAidOffer, offerDefinition), Language.PL);
    }

    @DeleteMapping("secure/material-aid/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        deleteOffer(id);
    }

    @Operation(description = "Allows to search for material aid offers using different criteria (passes as query params). Each criteria is optional.")
    @GetMapping("material-aid")
    public OffersVM<MaterialAidOfferVM> list(Pageable pageRequest, MaterialAidOfferSearchCriteria searchCriteria) {
        return OffersVM.page(repository.findAll(specifications.from(searchCriteria), pageRequest)
                .map(offer -> MaterialAidOfferVM.from(offer, searchCriteria.getLang())));
    }

    @GetMapping("material-aid/{id}")
    public MaterialAidOfferVM get(@PathVariable Long id, @RequestParam(required = false, defaultValue = "PL") Language lang) {
        return MaterialAidOfferVM.from(getOffer(id), lang);
    }

    @PutMapping("secure/material-aid/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id, @Valid @RequestBody MaterialAidOfferDefinitionDTO update) {
        updateOffer(id, update);
    }
}
