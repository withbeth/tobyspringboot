package withbeth.me.helloboot;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class HelloController implements ApplicationContextAware {

    private final HelloService helloService;
    // AppContext도 스프링 빈으로 등록되어 생성자 주입 받을 수 있다.
    // AwareInterface쓰는 것도 생성자 주입받는게 인터페이스 구현 필요도 없어지고, final지정도 가능해진다.
    private final ApplicationContext applicationContext;

    public HelloController(HelloService helloService, ApplicationContext applicationContext) {
        this.helloService = helloService;
        this.applicationContext = applicationContext;
    }

    @GetMapping("/hello")
    public String hello(String name) {
        Objects.requireNonNull(name);
        return helloService.sayHello(name);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("Set application context before init");
        System.out.println(applicationContext);
    }
}
