package pl.gov.coi.pomocua.ads.configuration.validation;

import pl.gov.coi.pomocua.ads.PhoneUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return PhoneUtil.isValid(value);
    }
}
