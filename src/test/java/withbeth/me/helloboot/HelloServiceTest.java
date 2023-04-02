package withbeth.me.helloboot;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.Test;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Test
@interface UnitTest {
}

class HelloServiceTest {

    @UnitTest
    void simpleHelloService() {
         HelloService helloService = new SimpleHelloService();
         assertThat(helloService.sayHello("withbeth")).isEqualTo("Hello, withbeth");
    }

    @UnitTest
    void helloDecorator() {
        HelloService helloService = new HelloDecorator(name -> name);
        assertThat(helloService.sayHello("withbeth")).isEqualTo("*withbeth*");
    }

}
