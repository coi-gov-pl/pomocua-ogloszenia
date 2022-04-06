package pl.gov.coi.pomocua.ads.messages;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.gov.coi.pomocua.ads.BaseOffer;
import pl.gov.coi.pomocua.ads.OfferNotFoundException;
import pl.gov.coi.pomocua.ads.captcha.CaptchaException;
import pl.gov.coi.pomocua.ads.captcha.CaptchaValidator;
import pl.gov.coi.pomocua.ads.error.CodeValidationError;
import pl.gov.coi.pomocua.ads.myoffers.MyOffersRepository;
import pl.gov.coi.pomocua.ads.users.User;
import pl.gov.coi.pomocua.ads.users.UserNotFoundException;
import pl.gov.coi.pomocua.ads.users.UsersRepository;

import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/message", produces = MediaType.APPLICATION_JSON_VALUE)
public class MessageResource {

    private final MyOffersRepository offersRepository;

    private final UsersRepository usersRepository;

    private final ReplyToOfferService replyToOfferService;

    private final CaptchaValidator captchaValidator;

    @PostMapping
    public ResponseEntity<Void> sendMessage(@Valid @RequestBody SendMessageDTO messageDefinition) {

        if (!captchaValidator.validate(messageDefinition.recaptchaResponse)) {
            throw new CaptchaException();
        }

        BaseOffer offer = offersRepository.findById(messageDefinition.offerId).orElseThrow(OfferNotFoundException::new);
        User user = usersRepository.getById(offer.userId).orElseThrow(UserNotFoundException::new);

        replyToOfferService.sendMessageToAdvertiser(user.email(), messageDefinition.replyEmail, messageDefinition.text,
                offer.title);
        replyToOfferService.sendOrderConfirmationMessage(messageDefinition.replyEmail, user.email(), offer.title, messageDefinition.text);

        return ResponseEntity.ok().build();
    }
}
