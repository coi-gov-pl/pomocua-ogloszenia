package pl.gov.coi.pomocua.ads.users;

import lombok.RequiredArgsConstructor;
import pl.gov.coi.pomocua.ads.UserId;
import pl.gov.coi.pomocua.ads.authentication.TestCurrentUser;

@RequiredArgsConstructor
public class TestUser {
    private final TestCurrentUser testCurrentUser;
    private final TestUsersRepository testUsersRepository;

    public void setCurrentUserWithId(UserId userId) {
        setCurrentUser(new User(
                userId,
                "some@email.com",
                "John"
        ));
    }

    public void setCurrentUser(User user) {
        testCurrentUser.setCurrentUserId(user.id());
        testUsersRepository.saveUser(user);
    }

    public void setDefault() {
        testCurrentUser.setDefault();
        testUsersRepository.setDefault();
    }
}
