package withbeth.me.helloboot;

import java.util.Objects;

public class HelloController {

    public String hello(String name) {
        Objects.requireNonNull(name);
        SimpleHelloService helloService = new SimpleHelloService();
        return helloService.sayHello(name);
    }
}
