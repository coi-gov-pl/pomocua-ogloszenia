package pl.gov.coi.pomocua.ads;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import pl.gov.coi.pomocua.ads.authentication.TestCurrentUser;
import pl.gov.coi.pomocua.ads.users.TestUsersRepository;
import pl.gov.coi.pomocua.ads.users.UsersRepository;

@org.springframework.boot.test.context.TestConfiguration
public class TestConfiguration {
    @Bean
    @Primary
    public TestCurrentUser testCurrentUser() {
        return new TestCurrentUser();
    }

    @Bean
    public TestUsersRepository usersRepository() { return new TestUsersRepository(); }
}
