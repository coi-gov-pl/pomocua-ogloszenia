package pl.gov.coi.pomocua.ads.dev;

import pl.gov.coi.pomocua.ads.UserId;
import pl.gov.coi.pomocua.ads.authentication.CurrentUser;
import pl.gov.coi.pomocua.ads.users.User;
import pl.gov.coi.pomocua.ads.users.UsersRepository;

import java.util.Optional;

public class FakeUsersRepository implements UsersRepository {

    private final CurrentUser currentUser;

    public FakeUsersRepository(CurrentUser currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public Optional<User> getById(UserId userId) {
        return Optional.of(new User(currentUser.getCurrentUserId(), "fake@email.invalid", "John"));
    }

    @Override
    public void removeUser(UserId userId) {

    }
}
