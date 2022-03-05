package pl.gov.coi.pomocua.ads.authentication;

import pl.gov.coi.pomocua.ads.UserId;

import java.util.Optional;

public class TestCurrentUser implements CurrentUser {
    private UserId currentUserId;

    @Override
    public Optional<UserId> findCurrentUserId() {
        return Optional.ofNullable(currentUserId);
    }

    public void setCurrentUserId(UserId currentUserId) {
        this.currentUserId = currentUserId;
    }

    public void clear() {
        this.currentUserId = null;
    }
}
