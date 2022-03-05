package pl.gov.coi.pomocua.ads.authentication;

import pl.gov.coi.pomocua.ads.UserId;

import java.util.Optional;

public class TestCurrentUser implements CurrentUser {
    private static final UserId DEFAULT_USER_ID = new UserId("some default user");
    private UserId currentUserId = DEFAULT_USER_ID;

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
