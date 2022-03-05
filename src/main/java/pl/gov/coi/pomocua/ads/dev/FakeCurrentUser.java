package pl.gov.coi.pomocua.ads.dev;

import pl.gov.coi.pomocua.ads.UserId;
import pl.gov.coi.pomocua.ads.authentication.CurrentUser;

import java.util.Optional;

public class FakeCurrentUser implements CurrentUser {
    @Override
    public Optional<UserId> findCurrentUserId() {
        return Optional.of(new UserId("fake-current-user-id"));
    }
}
