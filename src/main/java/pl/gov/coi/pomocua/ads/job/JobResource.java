package pl.gov.coi.pomocua.ads.job;

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
public class JobResource extends BaseOfferResource<JobOffer, JobOfferDefinitionDTO, JobOfferRepository> {

    private final JobOfferSpecifications specifications;

    public JobResource(JobOfferRepository repository,
                       CurrentUser currentUser,
                       UsersService usersService,
                       OffersTranslationUtil translationUtil,
                       JobOfferSpecifications specifications) {
        super(repository, currentUser, usersService, translationUtil);
        this.specifications = specifications;
    }

    @PostMapping("secure/job")
    @ResponseStatus(HttpStatus.CREATED)
    public JobOfferVM create(@Valid @RequestBody JobOfferDefinitionDTO offerDefinition) {
        JobOffer offer = new JobOffer();
        return JobOfferVM.from(createOffer(offer, offerDefinition), Language.PL);
    }

    @DeleteMapping("secure/job/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        deleteOffer(id);
    }

    @GetMapping("job")
    public OffersVM<JobOfferVM> list(Pageable pageRequest, JobOfferSearchCriteria searchCriteria) {
        return OffersVM.page(repository.findAll(specifications.from(searchCriteria), pageRequest)
                .map(offer -> JobOfferVM.from(offer, searchCriteria.getLang())));
    }

    @GetMapping("job/{id}")
    public JobOfferVM get(@PathVariable Long id, @RequestParam(required = false, defaultValue = "PL") Language lang) {
        return JobOfferVM.from(getOffer(id), lang);
    }

    @PutMapping("secure/job/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id, @Valid @RequestBody JobOfferDefinitionDTO update) {
        updateOffer(id, update);
    }
}
