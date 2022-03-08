package pl.gov.coi.pomocua.ads.users;

import pl.gov.coi.pomocua.ads.UserId;
import pl.gov.coi.pomocua.ads.authentication.CurrentUser;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TestUsersRepository implements UsersRepository {
    private final Map<UserId, User> users = new HashMap<>();
    private final CurrentUser currentUser;

    public TestUsersRepository(CurrentUser currentUser) {
        this.currentUser = currentUser;
        setDefault();
    }

    public void saveUser(User user) {
        users.put(user.id(), user);
    }

    public void clear() {
        users.clear();
    }

    public void setDefault() {
        clear();
        UserId currentUserId = currentUser.getCurrentUserId();
        users.put(currentUserId, new User(
                currentUserId,
                "some@email.com",
                "John"
        ));
    }

    @Override
    public Optional<User> getById(UserId userId) {
        return Optional.ofNullable(users.get(userId));
    }
}
