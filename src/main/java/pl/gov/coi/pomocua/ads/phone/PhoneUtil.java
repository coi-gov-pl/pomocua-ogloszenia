package pl.gov.coi.pomocua.ads.phone;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public class PhoneUtil {
    private static final String PLUS = "+";
    private static final String DEFAULT_REGION = "PL";
    private static final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    private static PhoneNumber parse(String value) throws NumberParseException {
        String phoneNumber = StringUtils.prependIfMissing(value, PLUS);
        return phoneNumberUtil.parse(phoneNumber, DEFAULT_REGION);
    }

    public static boolean isValid(String value) {
        try {
            PhoneNumber phoneNumber = parse(value);
            return phoneNumberUtil.isValidNumber(phoneNumber);
        } catch (NumberParseException e) {
            return false;
        }
    }

    public static Optional<PhoneDetails> getPhoneDetails(String value) {
        if (StringUtils.isEmpty(value)) return Optional.empty();

        try {
            PhoneNumber phoneNumber = parse(value);
            return Optional.of(new PhoneDetails(String.valueOf(phoneNumber.getCountryCode()),
                    String.valueOf(phoneNumber.getNationalNumber())));
        } catch (NumberParseException e) {
            throw new IllegalArgumentException("invalid phone number: %s".formatted(value), e);
        }
    }
}
