package withbeth.me.helloboot;

import java.util.Objects;

public class HelloController {

    private final HelloService helloService;

    public HelloController(HelloService helloService) {
        this.helloService = helloService;
    }

    public String hello(String name) {
        Objects.requireNonNull(name);
        return helloService.sayHello(name);
    }
}
