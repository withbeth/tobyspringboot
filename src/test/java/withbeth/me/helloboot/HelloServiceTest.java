package withbeth.me.helloboot;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class HelloServiceTest {

    @Test
    void simpleHelloService() {
         HelloService helloService = new SimpleHelloService();
         assertThat(helloService.sayHello("withbeth")).isEqualTo("Hello, withbeth");
    }

    @Test
    void helloDecorator() {
        HelloService helloService = new HelloDecorator(name -> name);
        assertThat(helloService.sayHello("withbeth")).isEqualTo("*withbeth*");
    }

}
