package withbeth.me.helloboot.factorybean;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.MessageDigest;
import java.util.Arrays;

class FactoryBeanTest {


    @Test
    void getMessageDigestInstanceFromBean() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
        System.out.println(Arrays.toString(ac.getBeanDefinitionNames()));
        MessageDigestClient messageDigestClient = ac.getBean(MessageDigestClient.class);
        messageDigestClient.computeHash("withbeth");
    }


    @Configuration
    static class TestConfig {

        @Bean
        public MessageDigestFactoryBean messageDigest() {
            return new MessageDigestFactoryBean();
        }

        // Autowiring by type from bean name 'messageDigestClient' via factory method to bean named 'messageDigest'
        // 따라서, 직접 factoryBean의 getObject()를 부르면 안된다. postConstruct와 같은 추가 설정들도 해주기에.
        @Bean
        public MessageDigestClient messageDigestClient(MessageDigest messageDigest) {
            return new MessageDigestClient(messageDigest);
        }

    }
}
