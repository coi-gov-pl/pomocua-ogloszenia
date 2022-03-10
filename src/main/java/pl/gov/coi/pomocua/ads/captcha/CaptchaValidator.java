package pl.gov.coi.pomocua.ads.captcha;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestOperations;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CaptchaValidator {

    public static final Pattern RESPONSE_PATTERN = Pattern.compile("[A-Za-z0-9_-]+");
    public static final String RECAPTCHA_URL_TEMPLATE = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s&remoteip=%s";

    private final CaptchaProperties captchaProperties;
    private final RestOperations restOperations;
    private final HttpServletRequest request;

    public boolean validate(String recaptchaResponse) {

        if (!captchaProperties.isEnabled()) {
            log.debug("Skip captcha validation.");
            return true;
        }

        if (!responseSanityCheck(recaptchaResponse)) {
            log.warn("Response contains invalid characters");
            return false;
        }

        URI verifyUri = URI.create(String.format(
                RECAPTCHA_URL_TEMPLATE, getCaptchaSecret(), recaptchaResponse, getClientIP()));
        log.debug("Check reCaptcha: " + verifyUri);

        CaptchaResponse captchaResponse = restOperations.getForObject(verifyUri, CaptchaResponse.class);

        if (captchaResponse == null ) {
            log.warn("Empty response from reCaptcha");
            return false;
        }

        if (!captchaResponse.isSuccess()) {
            String errors = "";
            CaptchaResponse.ErrorCode[] errorCodes = captchaResponse.getErrorCodes();
            if (errorCodes != null && errorCodes.length > 0) {
                errors = Arrays.stream(errorCodes)
                        .map(Enum::name)
                        .collect(Collectors.joining(", "));
            }
            log.warn("reCaptcha validation errors: " +  errors);
            return false;
        }

        return true;
    }

    private String getCaptchaSecret() {
        return captchaProperties.getSecret();
    }

    private String getClientIP() {
        return request.getRemoteAddr();
    }

    private boolean responseSanityCheck(String response) {
        return StringUtils.hasLength(response) && RESPONSE_PATTERN.matcher(response).matches();
    }
}
