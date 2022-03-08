package pl.gov.coi.pomocua.ads;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import pl.gov.coi.pomocua.ads.authentication.TestCurrentUser;
import pl.gov.coi.pomocua.ads.users.TestUsersRepository;

@org.springframework.boot.test.context.TestConfiguration
public class TestConfiguration {
    @Bean
    @Primary
    public TestCurrentUser testCurrentUser() {
        return new TestCurrentUser();
    }

    @Bean
    public TestUsersRepository usersRepository() {
        return new TestUsersRepository();
    }

    @Bean
    @Primary
    public TestTimeProvider testTimeProvider() {
        return new TestTimeProvider();
    }

    @Bean
    @Primary
    public JavaMailSender mailSender() {
        return new JavaMailSenderImpl();
    }
}
