package pl.gov.coi.pomocua.ads.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MessageProvider {

    private final ResourceBundleMessageSource messageSource;

    public String getMessageByCode(String code) {
        return getMessageByCode(code, null);
    }

    public String getMessageByCode(String code, Object[] args) {
        return messageSource.getMessage(code, args, Locale.getDefault());
    }
}
