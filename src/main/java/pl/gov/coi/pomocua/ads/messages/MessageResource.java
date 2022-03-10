package pl.gov.coi.pomocua.ads.messages;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.gov.coi.pomocua.ads.BaseOffer;
import pl.gov.coi.pomocua.ads.myoffers.MyOffersRepository;
import pl.gov.coi.pomocua.ads.users.User;
import pl.gov.coi.pomocua.ads.users.UsersRepository;

import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/message", produces = MediaType.APPLICATION_JSON_VALUE)
public class MessageResource {

    private final MyOffersRepository offersRepository;

    private final UsersRepository usersRepository;

    private final ReplyToOfferService replyToOfferService;

    @PostMapping
    public ResponseEntity<Void> sendMessage(@Valid @RequestBody SendMessageDTO messageDefinition) {
        if (!messageDefinition.tosApproved) {
            throw new ValidationException();
        }
        BaseOffer offer = offersRepository.findById(messageDefinition.offerId).orElseThrow(ValidationException::new);
        User user = usersRepository.getById(offer.userId).orElseThrow(ValidationException::new);

        replyToOfferService.sendMessageToAdvertiser(user.email(), messageDefinition.replyEmail, messageDefinition.text);
        replyToOfferService.sendOrderConfirmationMessage(messageDefinition.replyEmail, user.email(), offer.title, messageDefinition.text);

        return ResponseEntity.ok().build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class ValidationException extends IllegalArgumentException {}
}
