package pl.gov.coi.pomocua.ads.users;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.gov.coi.pomocua.ads.UserId;
import pl.gov.coi.pomocua.ads.authentication.CurrentUser;
import pl.gov.coi.pomocua.ads.mail.HtmlGenerator;
import pl.gov.coi.pomocua.ads.mail.MailService;
import pl.gov.coi.pomocua.ads.mail.MessageProvider;
import pl.gov.coi.pomocua.ads.myoffers.MyOffersRepository;

import javax.transaction.Transactional;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final CurrentUser currentUser;
    private final UsersRepository usersRepository;
    private final MyOffersRepository myOffersRepository;
    private final MailService mailService;
    private final HtmlGenerator htmlGenerator;
    private final MessageProvider messageProvider;

    public User getCurrentUser() {
        UserId currentUserId = currentUser.getCurrentUserId();
        return usersRepository.getById(currentUserId)
                .orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    public void removeCurrentUser() {
        UserId userId = currentUser.getCurrentUserId();
        myOffersRepository.setUserOffersAsInactive(userId);
        User user = usersRepository.obfuscateUser(userId);
        sendRemoveAccountEmail(user.email());
    }

    private void sendRemoveAccountEmail(String email) {
        String subject = messageProvider.getMessageByCode("REMOVE_ACCOUNT_SUBJECT");
        String html = htmlGenerator.generateHtml("mail/remove-account.ftlh", new HashMap<>());

        mailService.sendMailAsync(email, null, subject, html);
    }
}
