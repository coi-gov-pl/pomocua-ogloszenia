package pl.gov.coi.pomocua.ads.users;

import pl.gov.coi.pomocua.ads.UserId;

import java.util.HashMap;
import java.util.Map;

public class TestUsersRepository implements UsersRepository {
    private final Map<UserId, User> users = new HashMap<>();

    public void saveUser(User user) {
        users.put(user.id(), user);
    }

    public void clear() {
        users.clear();
    }
    @Override
    public User getById(UserId userId) {
        return users.get(userId);
    }
}
