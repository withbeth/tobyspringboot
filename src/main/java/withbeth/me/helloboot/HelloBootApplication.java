package withbeth.me.helloboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// @SpringBootApplication
public class HelloBootApplication {

    public static void main(String[] args) {
        // SpringApplication.run(HelloBootApplication.class, args);

        // Spring Container 생성
        // DispatcherServlet은 WebAppContext를 인자로 받기에, GenericWebAppContext생성.
        GenericWebApplicationContext appContext = new GenericWebApplicationContext() {

            @Override
            protected void onRefresh() {
                super.onRefresh();

                // 스프링 컨테이너 생성중에 서블릿 컨테이너 생성 및 프론트 컨트롤러 등록
                ServletWebServerFactory serverFactory = new TomcatServletWebServerFactory();

                WebServer webServer = serverFactory.getWebServer(servletContext -> {
                    servletContext.addServlet("dispatcherServlet", new DispatcherServlet(this))
                            .addMapping("/*"); // 모든 요청은 front controller로 위임
                });

                webServer.start();

            }
        };
        // Bean class 지정
        appContext.registerBean(HelloController.class);
        // 스프링이 구성정보를 만들때는, 정확하게 어떤 클래스를 가지고 빈을 만들건지 명시적으로 지정해야 한다.
        appContext.registerBean(SimpleHelloService.class);
        // 주어진 구성정보 이용해 초기화
        appContext.refresh();
        // 클래스 타입만으로 빈 취득
        HelloController helloController = appContext.getBean(HelloController.class);
    }

}
