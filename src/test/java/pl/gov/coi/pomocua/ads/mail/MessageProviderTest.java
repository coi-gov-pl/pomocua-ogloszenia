package pl.gov.coi.pomocua.ads.mail;

import org.apache.commons.codec.CharEncoding;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ResourceBundleMessageSource;
import pl.gov.coi.pomocua.ads.TestConfiguration;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@Import(TestConfiguration.class)
class MessageProviderTest {

    @org.springframework.boot.test.context.TestConfiguration
    static class MessageProviderTestConfiguration {

        @Primary
        @Bean
        public ResourceBundleMessageSource testMessageSource() {
            ResourceBundleMessageSource source = new ResourceBundleMessageSource();
            source.setBasenames("messages/messages");
            source.setDefaultEncoding(CharEncoding.UTF_8);
            source.setUseCodeAsDefaultMessage(true);
            return source;
        }
    }

    @Autowired
    private MessageProvider messageProvider;

    @Test
    void getMessageByCode_withoutArgs_UTF8EncodingIsWorking() {

        //when
        String replyToOfferSubject = messageProvider.getMessageByCode("POLISH_SIGNS_TEST");

        //then
        assertThat(replyToOfferSubject).isEqualTo("Test polskich znaków ążźół");
    }

    @Test
    void testGetMessageByCode_withArgs_messageContainValue() {

        //given
        Object[] args = {"World"};

        //when
        String replyToOfferSubject = messageProvider.getMessageByCode("MESSAGE_WITH_ARGS", args);

        //then
        assertThat(replyToOfferSubject).isEqualTo("Hello World");
    }
}