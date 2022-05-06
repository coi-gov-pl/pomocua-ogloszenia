package pl.gov.coi.pomocua.ads.users;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.gov.coi.pomocua.ads.TestConfiguration;
import pl.gov.coi.pomocua.ads.UserId;
import pl.gov.coi.pomocua.ads.authentication.TestCurrentUser;
import pl.gov.coi.pomocua.ads.users.UsersResource.UserInfo;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestConfiguration.class)
public class UserResourceTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TestCurrentUser testCurrentUser;

    @Autowired
    private TestUsersRepository testUsersRepository;

    @Value("${spring.mail.port}")
    private int smtpPort;

    private GreenMail smtpServer;

    @BeforeEach
    void setUp() {
        smtpServer = new GreenMail(new ServerSetup(smtpPort, null, ServerSetup.PROTOCOL_SMTP));
        smtpServer.setUser("user", "password");
        smtpServer.start();
        testUsersRepository.setDefault();
    }

    @AfterEach
    void after() {
        smtpServer.stop();
    }

    @Test
    void shouldReturnCurrentUserData() {
        User user = new User(new UserId("some-current-id"), "some@email.invalid", "John");
        testUsersRepository.saveUser(user);
        testCurrentUser.setCurrentUserId(user.id());

        ResponseEntity<UserInfo> response = restTemplate.getForEntity("/api/secure/me", UserInfo.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().email).isEqualTo("some@email.invalid");
        assertThat(response.getBody().firstName).isEqualTo("John");
    }

    @Test
    void handleMissingUser() {
        testUsersRepository.clear();
        testCurrentUser.setCurrentUserId(new UserId("some-current-id"));

        ResponseEntity<UserInfo> response = restTemplate.getForEntity("/api/secure/me", UserInfo.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testRemoveAccount() throws InterruptedException, MessagingException {

        //given
        String email = "some@email.invalid";
        User user = new User(new UserId("some-current-id"), email, "John");
        testUsersRepository.saveUser(user);
        testCurrentUser.setCurrentUserId(user.id());

        //when
        ResponseEntity<Void> response = restTemplate.postForEntity("/api/secure/remove-account", null, Void.class);

        //then
        Thread.sleep(1000);

        MimeMessage[] sentMails = smtpServer.getReceivedMessages();
        assertThat(sentMails).hasSize(1);

        Address[] allRecipients = sentMails[0].getAllRecipients();
        assertThat(allRecipients[0].toString()).isEqualTo(email);

        Optional<User> userOpt = testUsersRepository.getById(testCurrentUser.getCurrentUserId());
        assertThat(userOpt).isPresent().get().extracting(User::email).isEqualTo("obfuscate@email.com");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
