package pl.gov.coi.pomocua.ads.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import pl.gov.coi.pomocua.ads.authentication.CurrentUser;
import pl.gov.coi.pomocua.ads.dev.FakeCurrentUser;
import pl.gov.coi.pomocua.ads.dev.FakeUsersRepository;
import pl.gov.coi.pomocua.ads.users.UsersRepository;

@Profile("dev")
@Configuration
public class DevAuthenticationConfig {
    @Bean
    public CurrentUser fakeCurrentUser() {
        return new FakeCurrentUser();
    }

    @Bean
    public UsersRepository fakeUsersRepository(CurrentUser currentUser) {
        return new FakeUsersRepository(currentUser);
    }
}
