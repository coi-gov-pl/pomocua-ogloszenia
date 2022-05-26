package pl.gov.coi.pomocua.ads;

import lombok.RequiredArgsConstructor;
import pl.gov.coi.pomocua.ads.authentication.CurrentUser;
import pl.gov.coi.pomocua.ads.users.User;
import pl.gov.coi.pomocua.ads.users.UsersService;

@RequiredArgsConstructor
public abstract class BaseOfferResource<T extends BaseOffer, U extends BaseOfferDefinitionDTO,
        V extends BaseOfferRepository<T>> {

    protected final V repository;
    private final CurrentUser currentUser;
    private final UsersService usersService;
    private final OffersTranslationUtil translationUtil;

    protected T createOffer(T offer, U offerDefinition) {
        offerDefinition.applyTo(offer);
        translationUtil.translateOffer(offer);

        User currentUser = usersService.getCurrentUser();
        offer.attachTo(currentUser);

        return repository.save(offer);
    }

    protected void deleteOffer(Long id) {
        T offer = repository.findByIdAndUserId(id, currentUser.getCurrentUserId())
                .orElseThrow(OfferNotFoundException::new);

        if (!offer.isActive()) return;

        offer.status = BaseOffer.Status.INACTIVE;
        repository.save(offer);
    }

    protected T getOffer(Long id) {
        return repository.findById(id).filter(BaseOffer::isActive).orElseThrow(OfferNotFoundException::new);
    }

    protected void updateOffer(Long id, U update) {
        T offer = repository.findByIdAndUserId(id, currentUser.getCurrentUserId())
                .filter(BaseOffer::isActive)
                .orElseThrow(OfferNotFoundException::new);

        update.applyTo(offer);
        translationUtil.translateOffer(offer);

        repository.save(offer);
    }
}
