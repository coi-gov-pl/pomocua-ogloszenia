package pl.gov.coi.pomocua.ads.messages;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import org.apache.commons.mail.util.MimeMessageParser;
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
import pl.gov.coi.pomocua.ads.Location;
import pl.gov.coi.pomocua.ads.TestConfiguration;
import pl.gov.coi.pomocua.ads.UserId;
import pl.gov.coi.pomocua.ads.authentication.TestCurrentUser;
import pl.gov.coi.pomocua.ads.materialaid.MaterialAidCategory;
import pl.gov.coi.pomocua.ads.materialaid.MaterialAidOffer;
import pl.gov.coi.pomocua.ads.materialaid.MaterialAidOfferRepository;
import pl.gov.coi.pomocua.ads.users.TestUsersRepository;
import pl.gov.coi.pomocua.ads.users.User;

import javax.mail.Message;
import javax.mail.internet.MimeMessage;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestConfiguration.class)
class MessageResourceTest  {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TestUsersRepository usersRepository;

    @Autowired
    private TestCurrentUser currentUser;

    @Autowired
    private MaterialAidOfferRepository materialAidOfferRepository;

    @Autowired
    private Collection<CrudRepository<?, ?>> repositories;

    @Value("${spring.mail.port}")
    private int smtpPort;


    private GreenMail smtpServer;


    @BeforeEach
    void setUp() {
        smtpServer = new GreenMail(new ServerSetup(smtpPort, null, ServerSetup.PROTOCOL_SMTP));
        smtpServer.setUser("user", "password");
        smtpServer.start();
    }

    @AfterEach
    void clearDatabase() {
        repositories.forEach(CrudRepository::deleteAll);
        smtpServer.stop();
    }

    @Test
    void shouldSendEmailToOfferCreator() throws Exception {
        User recipient = new User(new UserId("recipient"), "recipient@email.invalid", "jan");
        usersRepository.saveUser(recipient);
        MaterialAidOffer offer = anOfferOf(recipient.id());

        ResponseEntity<Void> response = restTemplate.postForEntity("/api/message", new SendMessageDTO(
                offer.id,
                "message body",
                "reply@email.invalid",
                true
        ), Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        MimeMessage[] sentMails = smtpServer.getReceivedMessages();
        assertThat(sentMails.length).isEqualTo(1);
        MimeMessage sentMail = sentMails[0];
        assertThat(sentMail.getReplyTo().length).isEqualTo(1);
        assertThat(sentMail.getReplyTo()[0].toString()).isEqualTo("reply@email.invalid");
        assertThat(sentMail.getRecipients(Message.RecipientType.TO).length).isEqualTo(1);
        assertThat(sentMail.getRecipients(Message.RecipientType.TO)[0].toString()).isEqualTo("recipient@email.invalid");

        MimeMessageParser mimeMessageParser = new MimeMessageParser(sentMail);
        mimeMessageParser.parse();
        assertThat(mimeMessageParser.getHtmlContent()).contains("message body");
    }

    @Test
    void shouldFailWhenEmailPrefixIsIncorrect() {
        MaterialAidOffer offer = anOffer();

        ResponseEntity<Void> response = restTemplate.postForEntity("/api/message", new SendMessageDTO(
            offer.id,
            "message body",
            ".email@message@text.test",
            true
        ), Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldFailWhenEmailSuffixIsIncorrect() {
        MaterialAidOffer offer = anOffer();

        ResponseEntity<Void> response = restTemplate.postForEntity("/api/message", new SendMessageDTO(
            offer.id,
            "message body",
            "email@invalid",
            true
        ), Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldFailWithoutAcceptingToS() {
        MaterialAidOffer offer = anOffer();

        ResponseEntity<Void> response = restTemplate.postForEntity("/api/message", new SendMessageDTO(
                offer.id,
                "abc",
                "reply@email.invalid",
                false
        ), Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    @Test
    void shouldFailWithoutOfferId() {
        ResponseEntity<Void> response = restTemplate.postForEntity("/api/message", new SendMessageDTO(
                null,
                "abc",
                "reply@email.invalid",
                true
        ), Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldFailWithoutEmail() {
        MaterialAidOffer offer = materialAidOfferRepository.save(anOffer());

        ResponseEntity<Void> response = restTemplate.postForEntity("/api/message", new SendMessageDTO(
                offer.id,
                "text",
                "",
                true
        ), Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    @Test
    void shouldFailWhenOfferAuthorDoesNotExist() {
        MaterialAidOffer offer = anOfferOf(new UserId("not_existing_user"));

        ResponseEntity<Void> response = restTemplate.postForEntity("/api/message", new SendMessageDTO(
                offer.id,
                "text",
                "reply@email.invalid",
                true
        ), Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldFailForNotExistingOffer() {

        ResponseEntity<Void> response = restTemplate.postForEntity("/api/message", new SendMessageDTO(
                1L,
                "text",
                "reply@email.invalid",
                true
        ), Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    //TODO implement after disabling offer functionality is implemented
    @Test
    void shouldFailForDisabledOffer() {
    }

    private MaterialAidOffer anOffer() {
        return anOfferOf(currentUser.getCurrentUserId());
    }

    private MaterialAidOffer anOfferOf(UserId userId) {
        MaterialAidOffer offer = new MaterialAidOffer();
        offer.title = "title";
        offer.category = MaterialAidCategory.CLOTHING;
        offer.description = "description";
        offer.location = new Location("Mazowieckie", "Warszawa");
        offer.userId = userId;
        offer.userFirstName= "Jan";
        return materialAidOfferRepository.save(offer);
    }
}
