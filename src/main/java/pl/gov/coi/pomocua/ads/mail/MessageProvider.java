package pl.gov.coi.pomocua.ads.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MessageProvider {

    private final ResourceBundleMessageSource messageSource;

    public String getMessageByCode(String code, Object... args) {
        return getMessageByCodeAndLocale(code, Locale.getDefault(), args);
    }

    public String getMessageByCodeAndLocale(String code, Locale locale, Object... args) {
        return messageSource.getMessage(code, args, locale);
    }
}
