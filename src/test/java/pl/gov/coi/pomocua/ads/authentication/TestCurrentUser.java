package pl.gov.coi.pomocua.ads.authentication;

import java.util.Optional;

public class TestCurrentUser implements CurrentUser {
    private String currentUserId;

    @Override
    public Optional<String> findCurrentUserId() {
        return Optional.ofNullable(currentUserId);
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    public void clear() {
        this.currentUserId = null;
    }
}
