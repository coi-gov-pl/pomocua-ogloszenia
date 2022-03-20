package pl.gov.coi.pomocua.ads.authentication;

import pl.gov.coi.pomocua.ads.UserId;
import pl.gov.coi.pomocua.ads.users.UserNotFoundException;

import java.util.Optional;

public interface CurrentUser {
    Optional<UserId> findCurrentUserId();

    default UserId getCurrentUserId() {
        return findCurrentUserId().orElseThrow(UserNotFoundException::new);
    }
}
