package pl.gov.coi.pomocua.ads.captcha;

import com.google.cloud.recaptchaenterprise.v1beta1.RecaptchaEnterpriseServiceV1Beta1Client;
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

class CaptchaValidatorTest {

    private static final String CAPTCHA_PROJECT_ID = "xxx";
    private static final String CAPTCHA_SECRET = "xxx";
    private static final String CAPTCHA_SITE = "xxx";
    private static final String CAPTCHA_API_KEY = "xxx";

    private CaptchaValidator captchaValidator;

    private CaptchaProperties captchaProperties;
    private RestOperations restOperations;
    private MockHttpServletRequest request;

    private RecaptchaEnterpriseServiceV1Beta1Client recaptchaClient;

    @BeforeEach
    void setUp() {
        captchaProperties = defaultCaptchaProperties();
        request = new MockHttpServletRequest();
        restOperations = mock(RestOperations.class);

        captchaValidator = new CaptchaValidator(captchaProperties, recaptchaClient);
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

//    @Test
//    void validate_whenCaptchaResponseIsNull_expectFalse() {
//
//        //given
//        String recaptcha = "anytext";
//        String clientIP = "127.0.0.1";
//        request.setRemoteAddr(clientIP);
//
//        URI verifyUri = URI.create(String.format(RECAPTCHA_URL_TEMPLATE, CAPTCHA_SECRET, recaptcha, clientIP));
//        when(restOperations.getForObject(verifyUri, CaptchaResponse.class)).thenReturn(null);
//
//        //when
//        boolean result = captchaValidator.validate(recaptcha);
//
//        //then
//        assertAll(
//                () -> assertThat(result).isFalse(),
//                () -> verify(restOperations, times(1)).getForObject(verifyUri, CaptchaResponse.class)
//        );
//    }

//    @Test
//    void validate_whenCaptchaStatusNotSuccess_expectFalse() {
//
//        //given
//        String recaptcha = "anytext";
//        String clientIP = "127.0.0.1";
//        request.setRemoteAddr(clientIP);
//
//        CaptchaResponse response = new CaptchaResponse();
//        response.setSuccess(false);
//
//        URI verifyUri = URI.create(String.format(RECAPTCHA_URL_TEMPLATE, CAPTCHA_SECRET, recaptcha, clientIP));
//        when(restOperations.getForObject(verifyUri, CaptchaResponse.class)).thenReturn(response);
//
//        //when
//        boolean result = captchaValidator.validate(recaptcha);
//
//        //then
//        assertAll(
//                () -> assertThat(result).isFalse(),
//                () -> verify(restOperations, times(1)).getForObject(verifyUri, CaptchaResponse.class)
//        );
//    }

//    @Test
//    void validate_whenCaptchaStatusIsSuccess_expectTrue() {
//
//        //given
//        String recaptcha = "anytext";
//        String clientIP = "127.0.0.1";
//        request.setRemoteAddr(clientIP);
//
//        CaptchaResponse response = new CaptchaResponse();
//        response.setSuccess(true);
//
//        URI verifyUri = URI.create(String.format(RECAPTCHA_URL_TEMPLATE, CAPTCHA_SECRET, recaptcha, clientIP));
//        when(restOperations.getForObject(verifyUri, CaptchaResponse.class)).thenReturn(response);
//
//        //when
//        boolean result = captchaValidator.validate(recaptcha);
//
//        //then
//        assertAll(
//                () -> assertThat(result).isTrue(),
//                () -> verify(restOperations, times(1)).getForObject(verifyUri, CaptchaResponse.class)
//        );
//    }

    private CaptchaProperties defaultCaptchaProperties() {
        CaptchaProperties properties = new CaptchaProperties();
        properties.setGoogleCloudProjectId(CAPTCHA_PROJECT_ID);
        properties.setSiteKey(CAPTCHA_SITE);
        properties.setGoogleCloudApiKey(CAPTCHA_API_KEY);
        properties.setEnabled(true);

        return properties;
    }
}