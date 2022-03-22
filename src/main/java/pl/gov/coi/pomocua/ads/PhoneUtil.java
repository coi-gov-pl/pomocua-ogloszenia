package pl.gov.coi.pomocua.ads;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

public class PhoneUtil {
    private static final String DEFAULT_REGION = "PL";
    private static final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    public static boolean isValid(String value) {
        return phoneNumberUtil.isValidNumber(parse(value));
    }

    public static String normalize(String value) {
        return phoneNumberUtil.format(parse(value), PhoneNumberFormat.E164);
    }

    private static PhoneNumber parse(String value) {
        try {
            return phoneNumberUtil.parse(value, DEFAULT_REGION);
        } catch (NumberParseException e) {
            throw new IllegalArgumentException("invalid phone number: %s".formatted(value), e);
        }
    }
}
