package pl.gov.coi.pomocua.ads.messages;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public final class SendMessageDTO {
    @NotNull
    public final Long offerId;
    public final String text;
    @NotEmpty
    public final String replyEmail;
    @NotNull
    public final boolean tosApproved;

    public SendMessageDTO(Long offerId, String text, String replyEmail,
                          boolean tosApproved) {
        this.offerId = offerId;
        this.text = text;
        this.replyEmail = replyEmail;
        this.tosApproved = tosApproved;
    }


}
