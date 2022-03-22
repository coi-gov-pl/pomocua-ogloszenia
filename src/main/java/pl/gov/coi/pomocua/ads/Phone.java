package pl.gov.coi.pomocua.ads;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

import javax.persistence.AttributeConverter;
import java.io.IOException;
import java.util.Objects;

public class Phone {
    private static final String DEFAULT_REGION = "PL";
    private static final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    private final PhoneNumber phoneNumber;

    private Phone(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public static Phone from(String value) {
        try {
            return new Phone(phoneNumberUtil.parse(value, DEFAULT_REGION));
        } catch (NumberParseException e) {
            throw new IllegalArgumentException("invalid phone number: %s".formatted(value), e);
        }
    }

    public String normalized() {
        return phoneNumberUtil.format(phoneNumber, PhoneNumberFormat.E164);
    }

    public static boolean isValid(String value) {
        try {
            PhoneNumber phoneNumber = phoneNumberUtil.parse(value, DEFAULT_REGION);
            return phoneNumberUtil.isValidNumber(phoneNumber);
        } catch (NumberParseException e) {
            return false;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Phone phone = (Phone) o;
        return Objects.equals(phoneNumber, phone.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phoneNumber);
    }

    public static class Converter implements AttributeConverter<Phone, String> {
        @Override
        public String convertToDatabaseColumn(Phone phone) {
            return phone != null ? phone.normalized() : null;
        }

        @Override
        public Phone convertToEntityAttribute(String phoneStr) {
            return phoneStr != null ? Phone.from(phoneStr) : null;
        }
    }

    public static class Serializer extends StdSerializer<Phone> {
        public Serializer() {
            super(Phone.class);
        }

        @Override
        public void serialize(Phone phone, JsonGenerator json, SerializerProvider provider) throws IOException {
            json.writeString(phone.normalized());
        }
    }

    public static class Deserializer extends StdDeserializer<Phone> {
        public Deserializer() {
            super(Phone.class);
        }

        @Override
        public Phone deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            TreeNode json = parser.getCodec().readTree(parser);
            TreeNode phoneNumberNode = json.get("phoneNumber");
            if (phoneNumberNode != null) {
                String phoneNumber = phoneNumberNode.asToken().asString();
                return Phone.from(phoneNumber);
            } else {
                return null;
            }
        }
    }
}
