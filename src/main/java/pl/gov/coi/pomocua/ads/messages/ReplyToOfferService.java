package pl.gov.coi.pomocua.ads.messages;

import lombok.AllArgsConstructor;
import org.apache.commons.codec.CharEncoding;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import pl.gov.coi.pomocua.ads.users.User;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@AllArgsConstructor
public class ReplyToOfferService {

    private final JavaMailSender mailSender;


    public void sendMessage(User user, String replyTo, String messageBody) {
        //TODO: generate html content from template with message body

        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, CharEncoding.UTF_8);
            helper.setTo(user.email());
            helper.setReplyTo(replyTo);
            //TODO set from email
            helper.setText(messageBody, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
