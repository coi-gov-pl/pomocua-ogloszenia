package pl.gov.coi.pomocua.ads.mail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;
import pl.gov.coi.pomocua.ads.TestConfiguration;

import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.concurrent.Future;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@Import(TestConfiguration.class)
class MailServiceTest {

    @Value("${app.mail.from}")
    private String sendFrom;

    @Autowired
    private MailService mailService;

    private JavaMailSender mailSender;

    @BeforeEach
    void setUp() {
        mailSender = mock(JavaMailSender.class);
        ReflectionTestUtils.setField(mailService, "mailSender", mailSender);
    }

    @Test
    void sendMail_whenRecipientAndSenderAndSubjectIsProvided_thenExpectRecipientAndSenderAndSubjectSameAsProvided() {

        //given
        final String to = "email@example.com";
        final String subject = "Hello";
        final String htmlContent = "<html></html>";
        MimeMessage message = new MimeMessage((Session) null);

        when(mailSender.createMimeMessage()).thenReturn(message);

        //when
        boolean result = mailService.sendMail(to, subject, htmlContent);

        //then
        assertAll(
                () -> assertThat(result).isTrue(),
                () -> assertThat(message.getAllRecipients()).contains(new InternetAddress(to)),
                () -> assertThat(message.getFrom()).contains(new InternetAddress(sendFrom)),
                () -> assertThat(message.getSubject()).isEqualTo(subject),
                () -> verify(mailSender, times(1)).createMimeMessage(),
                () -> verify(mailSender, times(1)).send(message)
        );
    }

    @Test
    void sendMail_whenRecipientIsInvalid_thenExpectException() {

        //given
        final String invalidTo = "ssssss;";
        final String subject = "Hello";
        final String htmlContent = "<html></html>";
        MimeMessage message = new MimeMessage((Session) null);

        when(mailSender.createMimeMessage()).thenReturn(message);

        //then
        assertAll(
                () -> assertThatThrownBy(() -> mailService.sendMail(invalidTo, subject, htmlContent))
                        .isInstanceOf(MailSendingException.class),
                () -> verify(mailSender, times(1)).createMimeMessage()
        );
    }

    @Test
    void sendMailAsync_verify() {

        //given
        final String to = "email@example.com";
        final String subject = "Hello";
        final String htmlContent = "<html></html>";
        MimeMessage message = new MimeMessage((Session) null);

        when(mailSender.createMimeMessage()).thenReturn(message);

        //when
        Future<Boolean> result = mailService.sendMailAsync(to, subject, htmlContent);

        //then
        assertAll(
                () -> assertThat(result.isDone()).isFalse(),
                () -> verify(mailSender, timeout(100)).createMimeMessage(),
                () -> verify(mailSender, timeout(100)).send(message)
        );
    }
}