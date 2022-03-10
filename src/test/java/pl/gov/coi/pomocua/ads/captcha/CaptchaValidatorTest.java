package pl.gov.coi.pomocua.ads.captcha;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.client.RestOperations;

import java.net.URI;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static pl.gov.coi.pomocua.ads.captcha.CaptchaValidator.RECAPTCHA_URL_TEMPLATE;

class CaptchaValidatorTest {

    private static final String CAPTCHA_SECRET = "xxx";
    private static final String CAPTCHA_SITE = "xxx";

    private CaptchaValidator captchaValidator;

    private CaptchaProperties captchaProperties;
    private RestOperations restOperations;
    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        captchaProperties = defaultCaptchaProperties();
        request = new MockHttpServletRequest();
        restOperations = mock(RestOperations.class);

        captchaValidator = new CaptchaValidator(captchaProperties, restOperations, request);
    }

    @Test
    void validate_whenValidationIsDisabled_expectTrue() {

        //given
        String recaptcha = "anytext";
        captchaProperties.setEnabled(false);

        //when
        boolean result = captchaValidator.validate(recaptcha);

        //then
        assertThat(result).isTrue();
    }

    @Test
    void validate_whenCaptchaContainsInvalidCharacters_expectFalse() {

        //given
        String recaptcha = "invalid characters $%&";

        //when
        boolean result = captchaValidator.validate(recaptcha);

        //then
        assertThat(result).isFalse();
    }

    @Test
    void validate_whenCaptchaResponseIsNull_expectFalse() {

        //given
        String recaptcha = "anytext";
        String clientIP = "127.0.0.1";
        request.setRemoteAddr(clientIP);

        URI verifyUri = URI.create(String.format(RECAPTCHA_URL_TEMPLATE, CAPTCHA_SECRET, recaptcha, clientIP));
        when(restOperations.getForObject(verifyUri, CaptchaResponse.class)).thenReturn(null);

        //when
        boolean result = captchaValidator.validate(recaptcha);

        //then
        assertAll(
                () -> assertThat(result).isFalse(),
                () -> verify(restOperations, times(1)).getForObject(verifyUri, CaptchaResponse.class)
        );
    }

    @Test
    void validate_whenCaptchaStatusNotSuccess_expectFalse() {

        //given
        String recaptcha = "anytext";
        String clientIP = "127.0.0.1";
        request.setRemoteAddr(clientIP);

        CaptchaResponse response = new CaptchaResponse();
        response.setSuccess(false);

        URI verifyUri = URI.create(String.format(RECAPTCHA_URL_TEMPLATE, CAPTCHA_SECRET, recaptcha, clientIP));
        when(restOperations.getForObject(verifyUri, CaptchaResponse.class)).thenReturn(response);

        //when
        boolean result = captchaValidator.validate(recaptcha);

        //then
        assertAll(
                () -> assertThat(result).isFalse(),
                () -> verify(restOperations, times(1)).getForObject(verifyUri, CaptchaResponse.class)
        );
    }

    @Test
    void validate_whenCaptchaStatusIsSuccess_expectTrue() {

        //given
        String recaptcha = "anytext";
        String clientIP = "127.0.0.1";
        request.setRemoteAddr(clientIP);

        CaptchaResponse response = new CaptchaResponse();
        response.setSuccess(true);

        URI verifyUri = URI.create(String.format(RECAPTCHA_URL_TEMPLATE, CAPTCHA_SECRET, recaptcha, clientIP));
        when(restOperations.getForObject(verifyUri, CaptchaResponse.class)).thenReturn(response);

        //when
        boolean result = captchaValidator.validate(recaptcha);

        //then
        assertAll(
                () -> assertThat(result).isTrue(),
                () -> verify(restOperations, times(1)).getForObject(verifyUri, CaptchaResponse.class)
        );
    }

    private CaptchaProperties defaultCaptchaProperties() {
        CaptchaProperties properties = new CaptchaProperties();
        properties.setSecret(CAPTCHA_SECRET);
        properties.setSite(CAPTCHA_SITE);
        properties.setEnabled(true);

        return properties;
    }
}