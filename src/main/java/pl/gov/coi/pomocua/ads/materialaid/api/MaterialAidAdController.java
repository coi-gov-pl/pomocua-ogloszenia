package pl.gov.coi.pomocua.ads.materialaid.api;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.gov.coi.pomocua.ads.materialaid.domain.dto.MaterialAidOfferDto;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/secure/material-aid")
public class MaterialAidAdController {

    @Operation(description = "Saves material aid offer")
    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<Object> postMaterialAidOffer(@Valid @RequestBody final MaterialAidOfferDto materialAidOfferDto) {

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
