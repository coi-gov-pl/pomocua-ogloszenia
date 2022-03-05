package pl.gov.coi.pomocua.ads.materialaid;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
public class MaterialAidResource {

    private final MaterialAidRepository repository;

    @Operation(description = "Saves material aid offer")
    @PostMapping(value = "secure/material-aid")
    @ResponseStatus(HttpStatus.CREATED)
    public MaterialAidOffer create(@Valid @RequestBody MaterialAidOffer materialAidOffer) {
        materialAidOffer.id = null;
        return repository.save(materialAidOffer);
    }

    @GetMapping("material-aid")
    public Page<MaterialAidOffer> list(Pageable pageRequest) {
        return repository.findAll(pageRequest);
    }

    @GetMapping("material-aid/{id}")
    public Optional<MaterialAidOffer> list(@PathVariable Long id) {
        return repository.findById(id);
    }
}
