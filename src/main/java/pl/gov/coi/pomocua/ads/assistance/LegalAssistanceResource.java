package pl.gov.coi.pomocua.ads.assistance;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;


@RestController
@RequestMapping(path = "/api/legal-assistances", produces = "application/json")
public class LegalAssistanceResource {

    private final LegalAssistanceRepository legalAssistanceRepository;

    public LegalAssistanceResource(LegalAssistanceRepository legalAssistanceRepository){this.legalAssistanceRepository = legalAssistanceRepository;}

    @GetMapping(value = "/")
    public Page<LegalAssistance> getLegalAssistances(Pageable pageRequest){
        return legalAssistanceRepository.findAll(pageRequest);
    }

    @GetMapping(value = "/{id}")
    public Optional<LegalAssistance> getLegalAssistance(@PathVariable Long id){
        return legalAssistanceRepository.findById(id);
    }

    @PostMapping(value = "/secure")
    @ResponseStatus(HttpStatus.CREATED)
    public LegalAssistance create(@Valid @RequestBody LegalAssistance legalAssistance){
        legalAssistance.id = null;
        return legalAssistanceRepository.save(legalAssistance);
    }
}
