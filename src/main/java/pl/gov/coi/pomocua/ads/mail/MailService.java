package pl.gov.coi.pomocua.ads.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.CharEncoding;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.concurrent.Future;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    @Value("${app.mail.from}")
    private String sendFrom;

    private final JavaMailSender mailSender;

    public void sendMail(String to, String replyTo, String subject, String htmlContent) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, CharEncoding.UTF_8);
            helper.setFrom(sendFrom);
            helper.setTo(to);
            helper.setReplyTo(replyTo);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (MailException | MessagingException e) {
            log.error("Error during sending mail", e);
            throw new MailSendingException();
        }
    }

    @Async
    public Future<Void> sendMailAsync(String to, String replyTo, String subject, String htmlContent) {
        sendMail(to, replyTo, subject, htmlContent);
        return new AsyncResult<>(null);
    }
}
