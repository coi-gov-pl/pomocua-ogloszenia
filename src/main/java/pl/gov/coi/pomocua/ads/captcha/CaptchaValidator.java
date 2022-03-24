package pl.gov.coi.pomocua.ads.captcha;

import com.google.cloud.recaptchaenterprise.v1beta1.RecaptchaEnterpriseServiceV1Beta1Client;
import com.google.recaptchaenterprise.v1beta1.Assessment;
import com.google.recaptchaenterprise.v1beta1.CreateAssessmentRequest;
import com.google.recaptchaenterprise.v1beta1.Event;
import com.google.recaptchaenterprise.v1beta1.ProjectName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CaptchaValidator {

    private final CaptchaProperties properties;
    private final RecaptchaEnterpriseServiceV1Beta1Client recaptchaClient;

    public static final Pattern RESPONSE_PATTERN = Pattern.compile("[A-Za-z0-9_-]+");

    public boolean validate(String recaptchaResponse) {

        if (!properties.isEnabled()) {
            log.debug("Skip captcha validation. To enable change 'pl.gov.coi.captcha.enterprise.enabled' property");
            return true;
        }

        if (!responseSanityCheck(recaptchaResponse)) {
            log.warn("Response contains invalid characters");
            return false;
        }

        ProjectName projectName = ProjectName.of(properties.getGoogleCloudProjectId());
        Event event = Event.newBuilder()
                .setSiteKey(properties.getSiteKey())
                .setToken(recaptchaResponse)
                .build();

        CreateAssessmentRequest createAssessmentRequest =
                CreateAssessmentRequest.newBuilder()
                        .setParent(projectName.toString())
                        .setAssessment(Assessment.newBuilder().setEvent(event).build())
                        .build();

        Assessment response = recaptchaClient.createAssessment(createAssessmentRequest);

        // Check if the token is valid.
        if (!response.getTokenProperties().getValid()) {
            String invalidTokenReason = response.getTokenProperties().getInvalidReason().name();
            log.debug("The CreateAssessment call failed because the token was: " + invalidTokenReason);
            return false;
        }

        float score = response.getScore();
        if (score < properties.getAcceptLevel()) {
            List<String> reasons = response.getReasonsList()
                    .stream()
                    .map(classificationReason -> classificationReason.getDescriptorForType().getFullName())
                    .collect(Collectors.toList());
            log.debug("Validation failed. Score: " + score + ". Reasons: " + String.join(", ", reasons));
            return false;
        }

        log.debug("Validation OK - score: " + score);
        return true;
    }

    private boolean responseSanityCheck(String response) {
        return StringUtils.hasLength(response) && RESPONSE_PATTERN.matcher(response).matches();
    }
}
