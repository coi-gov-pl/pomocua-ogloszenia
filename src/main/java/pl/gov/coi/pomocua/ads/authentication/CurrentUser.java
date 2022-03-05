package pl.gov.coi.pomocua.ads.authentication;

import pl.gov.coi.pomocua.ads.UserId;

import java.util.Optional;

public interface CurrentUser {
    Optional<UserId> findCurrentUserId();

    default UserId getCurrentUserId() {
        return findCurrentUserId().orElseThrow(UnauthorizedException::new);
    }
}
