package pl.gov.coi.pomocua.ads.dev;

import pl.gov.coi.pomocua.ads.authentication.CurrentUser;

import java.util.Optional;

public class FakeCurrentUser implements CurrentUser {
    @Override
    public Optional<String> findCurrentUserId() {
        return Optional.of("fake-current-user-id");
    }
}
