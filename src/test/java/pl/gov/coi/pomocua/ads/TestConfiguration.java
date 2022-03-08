package pl.gov.coi.pomocua.ads;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import pl.gov.coi.pomocua.ads.authentication.CurrentUser;
import pl.gov.coi.pomocua.ads.authentication.TestCurrentUser;
import pl.gov.coi.pomocua.ads.users.TestUsersRepository;
import pl.gov.coi.pomocua.ads.users.TestUser;

@org.springframework.boot.test.context.TestConfiguration
public class TestConfiguration {
    @Bean
    @Primary
    public TestCurrentUser testCurrentUser() {
        return new TestCurrentUser();
    }

    @Bean
    @Primary
    public TestUsersRepository testUsersRepository(CurrentUser currentUser) {
        return new TestUsersRepository(currentUser);
    }

    @Bean
    @Primary
    public TestTimeProvider testTimeProvider() {
        return new TestTimeProvider();
    }

    @Bean
    public TestUser testUser(TestCurrentUser testCurrentUser, TestUsersRepository testUsersRepository) {
        return new TestUser(testCurrentUser, testUsersRepository);
    }
}
