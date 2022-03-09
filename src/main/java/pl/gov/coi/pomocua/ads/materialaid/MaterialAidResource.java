package pl.gov.coi.pomocua.ads.materialaid;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.gov.coi.pomocua.ads.BaseOffer;
import pl.gov.coi.pomocua.ads.OfferNotFoundException;
import pl.gov.coi.pomocua.ads.Offers;
import pl.gov.coi.pomocua.ads.authentication.CurrentUser;
import pl.gov.coi.pomocua.ads.users.User;
import pl.gov.coi.pomocua.ads.users.UsersService;

import javax.validation.Valid;

import static pl.gov.coi.pomocua.ads.Offers.page;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/", produces = MediaType.APPLICATION_JSON_VALUE)
public class MaterialAidResource {
    private final MaterialAidOfferRepository repository;
    private final CurrentUser currentUser;
    private final UsersService usersService;

    @Operation(description = "Creates material aid offer")
    @PostMapping(value = "secure/material-aid")
    @ResponseStatus(HttpStatus.CREATED)
    public MaterialAidOffer postMaterialAidOffer(@Valid @RequestBody MaterialAidOfferDefinitionDTO offerDefinition) {
        MaterialAidOffer materialAidOffer = new MaterialAidOffer();
        offerDefinition.applyTo(materialAidOffer);

        User currentUser = usersService.getCurrentUser();
        materialAidOffer.attachTo(currentUser);

        return repository.save(materialAidOffer);
    }

    @DeleteMapping("secure/material-aid/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        MaterialAidOffer offer = repository.findByIdAndUserId(id, currentUser.getCurrentUserId())
                .orElseThrow(OfferNotFoundException::new);

        if (!offer.isActive()) return;

        offer.status = BaseOffer.Status.INACTIVE;
        repository.save(offer);
    }

    @Operation(description = "Allows to search for material aid offers using different criteria (passes as query params). Each criteria is optional.")
    @GetMapping("material-aid")
    public Offers<MaterialAidOffer> list(Pageable pageRequest, MaterialAidOfferSearchCriteria searchCriteria) {
        return page(repository.findAll(MaterialAidOfferSpecifications.from(searchCriteria), pageRequest));
    }

    @GetMapping("material-aid/{id}")
    public ResponseEntity<MaterialAidOffer> get(@PathVariable Long id) {
        return ResponseEntity.of(repository.findById(id).filter(BaseOffer::isActive));
    }

    @PutMapping("secure/material-aid/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id, @Valid @RequestBody MaterialAidOfferDefinitionDTO update) {
        MaterialAidOffer offer = repository.findByIdAndUserId(id, currentUser.getCurrentUserId())
                .filter(BaseOffer::isActive)
                .orElseThrow(OfferNotFoundException::new);

        update.applyTo(offer);

        repository.save(offer);
    }
}
