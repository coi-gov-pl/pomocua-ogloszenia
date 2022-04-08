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

    public void sendMessageToAdvertiser(String email, String replyTo, String messageBody, String offerTitle) {
        String subject = messageProvider.getMessageByCode("REPLY_TO_OFFER_SUBJECT");

        Map<String, Object> variables = new HashMap<>();
        variables.put("MESSAGE", messageBody);
        variables.put("REPLY_TO_EMAIL", replyTo);
        variables.put("OFFER_TITLE", offerTitle);
        String html = htmlGenerator.generateHtml("mail/message-to-advertiser.ftlh", variables);

        mailService.sendMailAsync(email, replyTo, subject, html);
    }

    public void sendOrderConfirmationMessage(String email, String replyTo, String offerTitle, String messageBody) {
        String subject = messageProvider.getMessageByCode("ORDER_CONFIRMATION_SUBJECT");

        Map<String, Object> variables = new HashMap<>();
        variables.put("MESSAGE", messageBody);
        variables.put("OFFER_TITLE", offerTitle);
        String html = htmlGenerator.generateHtml("mail/offer-confirmation.ftlh", variables);

        mailService.sendMailAsync(email, replyTo, subject, html);
    }
}
