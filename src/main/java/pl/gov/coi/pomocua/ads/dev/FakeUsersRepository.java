package pl.gov.coi.pomocua.ads.dev;

import pl.gov.coi.pomocua.ads.UserId;
import pl.gov.coi.pomocua.ads.authentication.CurrentUser;
import pl.gov.coi.pomocua.ads.users.User;
import pl.gov.coi.pomocua.ads.users.UsersRepository;

public class FakeUsersRepository implements UsersRepository {

    private final CurrentUser currentUser;

    public FakeUsersRepository(CurrentUser currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public User getById(UserId userId) {
        return new User(currentUser.getCurrentUserId(), "fake@email.invalid");
    }
}
