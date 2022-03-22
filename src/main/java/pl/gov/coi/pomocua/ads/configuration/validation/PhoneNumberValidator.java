package pl.gov.coi.pomocua.ads.configuration.validation;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {
    private static final String DEFAULT_REGION = "PL";

    private PhoneNumberUtil phoneNumberUtil;

    @Override
    public void initialize(PhoneNumber constraintAnnotation) {
        phoneNumberUtil = PhoneNumberUtil.getInstance();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        try {
            Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse(value, DEFAULT_REGION);
            return phoneNumberUtil.isValidNumber(phoneNumber);
        } catch (NumberParseException e) {
            return false;
        }
    }
}
