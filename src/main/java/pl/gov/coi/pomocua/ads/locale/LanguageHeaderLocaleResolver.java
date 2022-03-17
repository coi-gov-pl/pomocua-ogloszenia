package pl.gov.coi.pomocua.ads.locale;

import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@RequiredArgsConstructor
public class LanguageHeaderLocaleResolver extends AcceptHeaderLocaleResolver {

    private final String header;

    @Override
    public Locale resolveLocale(HttpServletRequest request) {

        String locale = request.getHeader(header);
        if (StringUtils.hasText(locale)) {
            return StringUtils.parseLocale(locale);
        }

        return super.resolveLocale(request);
    }
}
