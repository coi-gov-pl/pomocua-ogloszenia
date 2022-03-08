package pl.gov.coi.pomocua.ads.materialaid;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.gov.coi.pomocua.ads.OfferNotFoundException;
import pl.gov.coi.pomocua.ads.Offers;
import pl.gov.coi.pomocua.ads.authentication.CurrentUser;

import javax.validation.Valid;

import static pl.gov.coi.pomocua.ads.Offers.page;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/", produces = MediaType.APPLICATION_JSON_VALUE)
public class MaterialAidResource {
    private final MaterialAidOfferRepository repository;
    private final CurrentUser currentUser;

    @Operation(description = "Creates material aid offer")
    @PostMapping(value = "secure/material-aid")
    @ResponseStatus(HttpStatus.CREATED)
    public MaterialAidOffer postMaterialAidOffer(
            @Valid @RequestBody final MaterialAidOfferDefinitionDTO materialAidOfferDefinition) {
        MaterialAidOffer materialAidOffer = new MaterialAidOffer();
        materialAidOfferDefinition.applyTo(materialAidOffer);
        materialAidOffer.userId = currentUser.getCurrentUserId();
        return repository.save(materialAidOffer);
    }

    @Operation(description = "Allows to search for material aid offers using different criteria (passes as query params). Each criteria is optional.")
    @GetMapping("material-aid")
    public Offers<MaterialAidOffer> list(Pageable pageRequest, MaterialAidOfferSearchCriteria searchCriteria) {
        return page(repository.findAll(MaterialAidOfferSpecifications.from(searchCriteria), pageRequest));
    }

    @GetMapping("material-aid/{id}")
    public ResponseEntity<MaterialAidOffer> get(@PathVariable Long id) {
        return ResponseEntity.of(repository.findById(id));
    }

    @PutMapping("secure/material-aid/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id, @Valid @RequestBody MaterialAidOfferDefinitionDTO update) {
        MaterialAidOffer offer = repository.findByIdAndUserId(id, currentUser.getCurrentUserId())
                .orElseThrow(OfferNotFoundException::new);

        update.applyTo(offer);

        repository.save(offer);
    }
}
