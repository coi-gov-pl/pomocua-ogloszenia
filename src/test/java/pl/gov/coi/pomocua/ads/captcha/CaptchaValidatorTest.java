package pl.gov.coi.pomocua.ads.captcha;

import com.google.cloud.recaptchaenterprise.v1beta1.RecaptchaEnterpriseServiceV1Beta1Client;
import com.google.recaptchaenterprise.v1beta1.Assessment;
import com.google.recaptchaenterprise.v1beta1.CreateAssessmentRequest;
import com.google.recaptchaenterprise.v1beta1.TokenProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CaptchaValidatorTest {

    private static final String CAPTCHA_PROJECT_ID = "xxx";
    private static final String CAPTCHA_SITE = "xxx";
    private static final String CAPTCHA_API_KEY = "xxx";
    private static final Float CAPTCHA_ACCEPT_LEVEL = 0.4F;

    private CaptchaValidator captchaValidator;

    private CaptchaProperties captchaProperties;

    private RecaptchaEnterpriseServiceV1Beta1Client recaptchaClient;

    @BeforeEach
    void setUp() {
        captchaProperties = defaultCaptchaProperties();
        recaptchaClient = mock(RecaptchaEnterpriseServiceV1Beta1Client.class);
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

    @Test
    void validate_whenCaptchaResponseIsNull_expectFalse() {

        //given
        String recaptcha = "anytext";

        when(recaptchaClient.createAssessment(any(CreateAssessmentRequest.class))).thenReturn(null);

        //when
        boolean result = captchaValidator.validate(recaptcha);

        //then
        assertAll(
                () -> assertThat(result).isFalse(),
                () -> verify(recaptchaClient, times(1)).createAssessment(any(CreateAssessmentRequest.class))
        );
    }

    @Test
    void validate_whenCaptchaStatusNotSuccess_expectFalse() {

        //given
        String recaptcha = "anytext";

        Assessment response = createAssessmentResponse(false, 0);

        when(recaptchaClient.createAssessment(any(CreateAssessmentRequest.class))).thenReturn(response);

        //when
        boolean result = captchaValidator.validate(recaptcha);

        //then
        assertAll(
                () -> assertThat(result).isFalse(),
                () -> verify(recaptchaClient, times(1)).createAssessment(any(CreateAssessmentRequest.class))
        );
    }

    @Test
    void validate_whenCaptchaStatusIsSuccessAndScoreAccepted_expectTrue() {

        //given
        String recaptcha = "anytext";

        Assessment response = createAssessmentResponse(true, 111);

        when(recaptchaClient.createAssessment(any(CreateAssessmentRequest.class))).thenReturn(response);

        //when
        boolean result = captchaValidator.validate(recaptcha);

        //then
        assertAll(
                () -> assertThat(result).isTrue(),
                () -> verify(recaptchaClient, times(1)).createAssessment(any(CreateAssessmentRequest.class))
        );
    }

    @Test
    void validate_whenCaptchaStatusIsSuccessAndScoreNotAccepted_expectFalse() {

        //given
        String recaptcha = "anytext";

        Assessment response = createAssessmentResponse(true, 0.3F);

        when(recaptchaClient.createAssessment(any(CreateAssessmentRequest.class))).thenReturn(response);

        //when
        boolean result = captchaValidator.validate(recaptcha);

        //then
        assertAll(
                () -> assertThat(result).isFalse(),
                () -> verify(recaptchaClient, times(1)).createAssessment(any(CreateAssessmentRequest.class))
        );
    }

    private CaptchaProperties defaultCaptchaProperties() {
        CaptchaProperties properties = new CaptchaProperties();
        properties.setGoogleCloudProjectId(CAPTCHA_PROJECT_ID);
        properties.setSiteKey(CAPTCHA_SITE);
        properties.setGoogleCloudApiKey(CAPTCHA_API_KEY);
        properties.setAcceptLevel(CAPTCHA_ACCEPT_LEVEL);
        properties.setEnabled(true);

        return properties;
    }

    private Assessment createAssessmentResponse(boolean valid, float score) {
        return Assessment.newBuilder()
                .setTokenProperties(TokenProperties.newBuilder().setValid(valid).build())
                .setScore(score)
                .build();
    }
}