package pl.gov.coi.pomocua.ads.materialaid;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
public class MaterialAidResource {

    private final MaterialAidOfferManager manager;

    @Operation(description = "Saves material aid offer")
    @PostMapping(value = "secure/material-aid", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Object> postMaterialAidOffer(@Valid @RequestBody final MaterialAidOffer materialAidOffer) {
        manager.saveMaterialAidOffer(materialAidOffer);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
