package pl.gov.coi.pomocua.ads.captcha;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "success",
        "challenge_ts",
        "hostname",
        "error-codes"
})
@Data
public class CaptchaResponse {

    @JsonProperty("success")
    private boolean success;

    @JsonProperty("challenge_ts")
    private String challengeTs;

    @JsonProperty("hostname")
    private String hostname;

    @JsonProperty("error-codes")
    private ErrorCode[] errorCodes;

    @JsonIgnore
    public boolean hasClientError() {
        ErrorCode[] errors = getErrorCodes();
        if(errors == null) {
            return false;
        }
        for(ErrorCode error : errors) {
            switch(error) {
                case InvalidResponse:
                case MissingResponse:
                    return true;
            }
        }
        return false;
    }

    enum ErrorCode {
        MissingSecret,
        InvalidSecret,
        MissingResponse,
        InvalidResponse;

        private static Map<String, ErrorCode> errorsMap = new HashMap<>(4);

        static {
            errorsMap.put("missing-input-secret",   MissingSecret);
            errorsMap.put("invalid-input-secret",   InvalidSecret);
            errorsMap.put("missing-input-response", MissingResponse);
            errorsMap.put("invalid-input-response", InvalidResponse);
        }

        @JsonCreator
        public static ErrorCode forValue(String value) {
            return errorsMap.get(value.toLowerCase());
        }
    }
}
