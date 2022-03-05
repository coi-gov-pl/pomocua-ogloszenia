package pl.gov.coi.pomocua.ads.authentication;

import java.util.Optional;

public interface CurrentUser {
    Optional<String> findCurrentUserId();

    default String getCurrentUserId() {
        return findCurrentUserId().orElseThrow(UnauthorizedException::new);
    }
}
