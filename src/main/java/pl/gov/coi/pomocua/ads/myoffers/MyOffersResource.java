package pl.gov.coi.pomocua.ads.myoffers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.gov.coi.pomocua.ads.BaseOffer;
import pl.gov.coi.pomocua.ads.Offers;
import pl.gov.coi.pomocua.ads.UserId;
import pl.gov.coi.pomocua.ads.authentication.CurrentUser;

import static pl.gov.coi.pomocua.ads.Offers.page;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/secure/")
public class MyOffersResource {
    private final MyOffersRepository repository;
    private final CurrentUser currentUser;

    @GetMapping("my-offers")
    public Offers<BaseOffer> list(Pageable pageRequest) {
        UserId userId = currentUser.getCurrentUserId();
        return page(repository.findAllByUserIdAndStatus(userId, BaseOffer.Status.ACTIVE, pageRequest));
    }

    @GetMapping("my-offers/{id}")
    public ResponseEntity<BaseOffer> get(@PathVariable Long id) {
        UserId userId = currentUser.getCurrentUserId();
        return ResponseEntity.of(repository.findByIdAndUserId(id, userId).filter(BaseOffer::isActive));
    }
}
