package pl.gov.coi.pomocua.ads.users;

import pl.gov.coi.pomocua.ads.UserId;

public interface UsersRepository {

    User getById(UserId userId);
}
