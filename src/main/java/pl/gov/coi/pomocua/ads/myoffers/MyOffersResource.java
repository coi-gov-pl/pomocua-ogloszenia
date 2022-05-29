package pl.gov.coi.pomocua.ads.myoffers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.gov.coi.pomocua.ads.BaseOffer;
import pl.gov.coi.pomocua.ads.BaseOfferVM;
import pl.gov.coi.pomocua.ads.BaseOfferVisitor;
import pl.gov.coi.pomocua.ads.Language;
import pl.gov.coi.pomocua.ads.OfferNotFoundException;
import pl.gov.coi.pomocua.ads.OffersVM;
import pl.gov.coi.pomocua.ads.UserId;
import pl.gov.coi.pomocua.ads.authentication.CurrentUser;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/secure/", produces = MediaType.APPLICATION_JSON_VALUE)
public class MyOffersResource {
    private final MyOffersRepository repository;
    private final CurrentUser currentUser;
    private final BaseOfferVisitor baseOfferVisitor;

    @GetMapping("my-offers")
    public OffersVM<BaseOfferVM> list(Pageable pageRequest,
                                      @RequestParam(required = false, defaultValue = "PL") Language lang) {
        UserId userId = currentUser.getCurrentUserId();
        return OffersVM.page(repository.findAllByUserIdAndStatus(userId, BaseOffer.Status.ACTIVE, pageRequest)
                .map(offer -> offer.accept(baseOfferVisitor, lang)));
    }

    @GetMapping("my-offers/{id}")
    public BaseOfferVM get(@PathVariable Long id, @RequestParam(required = false, defaultValue = "PL") Language lang) {
        UserId userId = currentUser.getCurrentUserId();
        return repository.findByIdAndUserId(id, userId)
                .filter(BaseOffer::isActive)
                .orElseThrow(OfferNotFoundException::new)
                .accept(baseOfferVisitor, lang);
    }
}
