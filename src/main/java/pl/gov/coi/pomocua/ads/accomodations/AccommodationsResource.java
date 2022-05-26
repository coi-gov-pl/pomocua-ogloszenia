package pl.gov.coi.pomocua.ads.accomodations;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.gov.coi.pomocua.ads.Language;
import pl.gov.coi.pomocua.ads.OffersVM;
import pl.gov.coi.pomocua.ads.BaseOffer;
import pl.gov.coi.pomocua.ads.authentication.CurrentUser;
import pl.gov.coi.pomocua.ads.BaseOfferResource;
import pl.gov.coi.pomocua.ads.OffersTranslationUtil;
import pl.gov.coi.pomocua.ads.users.UsersService;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/", produces = MediaType.APPLICATION_JSON_VALUE)
public class AccommodationsResource
        extends BaseOfferResource<AccommodationOffer, AccommodationOfferDefinitionDTO, AccommodationsRepository> {

    public AccommodationsResource(AccommodationsRepository repository,
                                  CurrentUser currentUser,
                                  UsersService usersService,
                                  OffersTranslationUtil translationUtil) {
        super(repository, currentUser, usersService, translationUtil);
    }

    @PostMapping("secure/accommodations")
    @ResponseStatus(HttpStatus.CREATED)
    public AccommodationOfferVM create(@Valid @RequestBody AccommodationOfferDefinitionDTO offerDefinition) {
        AccommodationOffer accommodationOffer = new AccommodationOffer();
        return AccommodationOfferVM.from(createOffer(accommodationOffer, offerDefinition), Language.PL);
    }

    @DeleteMapping("secure/accommodations/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        deleteOffer(id);
    }

    @GetMapping("accommodations")
    public OffersVM<AccommodationOfferVM> list(AccommodationOfferSearchCriteria searchCriteria, Pageable pageRequest) {
        return OffersVM.page(repository.findAllByGuestsIsGreaterThanEqualAndStatus(
                searchCriteria.getCapacity(),
                BaseOffer.Status.ACTIVE,
                pageRequest
        ).map(offer -> AccommodationOfferVM.from(offer, searchCriteria.getLang())));
    }

    @GetMapping("accommodations/{region}/{city}")
    public OffersVM<AccommodationOfferVM> listByLocation(
            @PathVariable String region,
            @PathVariable String city,
            AccommodationOfferSearchCriteria searchCriteria,
            Pageable pageRequest
    ) {
        return OffersVM.page(repository.findAllByLocation_RegionIgnoreCaseAndLocation_CityIgnoreCaseAndGuestsIsGreaterThanEqualAndStatus(
                region,
                city,
                searchCriteria.getCapacity(),
                BaseOffer.Status.ACTIVE,
                pageRequest
        ).map(offer -> AccommodationOfferVM.from(offer, searchCriteria.getLang())));
    }

    @GetMapping("accommodations/{id}")
    public AccommodationOfferVM get(@PathVariable Long id, @RequestParam(required = false, defaultValue = "PL") Language lang) {
        return AccommodationOfferVM.from(getOffer(id), lang);
    }

    @PutMapping("secure/accommodations/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id, @Valid @RequestBody AccommodationOfferDefinitionDTO update) {
        updateOffer(id, update);
    }
}
