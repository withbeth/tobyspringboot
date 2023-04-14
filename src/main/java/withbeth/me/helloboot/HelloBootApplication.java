package withbeth.me.helloboot;

import org.springframework.boot.SpringApplication;

// @SpringBootApplication
@MySpringBootAnnotation
public class HelloBootApplication {

    public static void main(String[] args) {
        // MySpringApplication.run(HelloBootApplication.class, args);
        SpringApplication.run(HelloBootApplication.class, args);
    }

}
