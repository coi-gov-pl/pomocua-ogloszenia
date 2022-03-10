package pl.gov.coi.pomocua.ads.messages;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.gov.coi.pomocua.ads.mail.HtmlGenerator;
import pl.gov.coi.pomocua.ads.mail.MailService;
import pl.gov.coi.pomocua.ads.mail.MessageProvider;
import pl.gov.coi.pomocua.ads.users.User;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class ReplyToOfferService {

    private final MailService mailService;
    private final HtmlGenerator htmlGenerator;
    private final MessageProvider messageProvider;

    public void sendMessage(User user, String replyTo, String messageBody) {

        String to = user.email();
        String subject = messageProvider.getMessageByCode("REPLY_TO_OFFER_SUBJECT");

        Map<String, Object> variables = new HashMap<>();
        variables.put("MESSAGE", messageBody);
        String html = htmlGenerator.generateHtml("mail/reply-to-offer.ftlh", variables);

        mailService.sendMail(to, replyTo, subject, html);
    }
}
