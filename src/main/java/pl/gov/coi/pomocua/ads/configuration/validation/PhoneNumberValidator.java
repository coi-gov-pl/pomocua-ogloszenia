package pl.gov.coi.pomocua.ads.configuration.validation;

import pl.gov.coi.pomocua.ads.Phone;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return Phone.isValid(value);
    }
}
