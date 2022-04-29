package pl.gov.coi.pomocua.ads.phone;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PhoneUtilTest {

    @Test
    void getPhoneDetails_expectSameResultWithOrWithoutPlus() {

        //when
        Optional<PhoneDetails> phoneDetailsWithPlus = PhoneUtil.getPhoneDetails("+380671232233");

        Optional<PhoneDetails> phoneDetailsWithoutPlus = PhoneUtil.getPhoneDetails("380671232233");

        //then

        assertThat(phoneDetailsWithPlus).isPresent();
        assertThat(phoneDetailsWithoutPlus).isPresent();

        assertAll(
                () -> assertThat(phoneDetailsWithPlus).isEqualTo(phoneDetailsWithoutPlus),
                () -> assertThat(phoneDetailsWithPlus.get().countryCode()).isEqualTo("380")
        );
    }
}