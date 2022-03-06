package pl.gov.coi.pomocua.ads.dev;

import pl.gov.coi.pomocua.ads.UserId;
import pl.gov.coi.pomocua.ads.users.User;
import pl.gov.coi.pomocua.ads.users.UsersRepository;

public class FakeUserRepository implements UsersRepository {

    @Override
    public User getById(UserId userId) {
        return new User(new FakeCurrentUser().getCurrentUserId(), "fake@email.invalid");
    }
}
