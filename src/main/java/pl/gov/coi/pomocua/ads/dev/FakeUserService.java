package pl.gov.coi.pomocua.ads.dev;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pl.gov.coi.pomocua.ads.UserId;
import pl.gov.coi.pomocua.ads.iam.UserDetails;
import pl.gov.coi.pomocua.ads.iam.service.UserService;

@Profile("dev")
@Service
public class FakeUserService implements UserService {

    public final static String FAKE_EMAIL = "fake@email.com";

    @Override
    public UserDetails getUserDetails(UserId userId) {
        return new UserDetails(FAKE_EMAIL);
    }
}
