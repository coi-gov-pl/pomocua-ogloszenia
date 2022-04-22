package pl.gov.coi.pomocua.ads.users;

import pl.gov.coi.pomocua.ads.UserId;

import java.util.Optional;

public interface UsersRepository {

    Optional<User> getById(UserId userId);

    User obfuscateUser(UserId userId);
}
