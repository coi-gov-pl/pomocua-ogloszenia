package pl.gov.coi.pomocua.ads;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

public class PhoneUtil {
    private static final String DEFAULT_REGION = "PL";
    private static final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    public static boolean isValid(String value) {
        try {
            PhoneNumber phoneNumber = phoneNumberUtil.parse(value, DEFAULT_REGION);
            return phoneNumberUtil.isValidNumber(phoneNumber);
        } catch (NumberParseException e) {
            return false;
        }
    }

    public static String normalize(String value) {
        try {
            PhoneNumber phoneNumber = phoneNumberUtil.parse(value, DEFAULT_REGION);
            return phoneNumberUtil.format(phoneNumber, PhoneNumberFormat.E164);
        } catch (NumberParseException e) {
            throw new IllegalArgumentException("invalid phone number: %s".formatted(value), e);
        }
    }
}
