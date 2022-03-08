package pl.gov.coi.pomocua.ads.users;

import pl.gov.coi.pomocua.ads.UserId;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TestUsersRepository implements UsersRepository {
    private final Map<UserId, User> users = new HashMap<>();

    public void saveUser(User user) {
        users.put(user.id(), user);
    }

    public void clear() {
        users.clear();
    }

    @Override
    public Optional<User> getById(UserId userId) {
        return Optional.ofNullable(users.get(userId));
    }
}
