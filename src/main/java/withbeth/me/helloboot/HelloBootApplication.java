package withbeth.me.helloboot;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

// @SpringBootApplication
@Configuration
@ComponentScan
public class HelloBootApplication {

    public static void main(String[] args) {
        // SpringApplication.run(HelloBootApplication.class, args);

        // Spring Container 생성
        // DispatcherServlet은 WebAppContext를 인자로 받기에, GenericWebAppContext생성.
        AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext() {

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
        // 구성정보 메타데이터 설정
        appContext.register(HelloBootApplication.class);
        // Bean class 지정
        // appContext.registerBean(HelloController.class);
        // 스프링이 구성정보를 만들때는, 정확하게 어떤 클래스를 가지고 빈을 만들건지 명시적으로 지정해야 한다.
        // appContext.registerBean(SimpleHelloService.class);
        // 주어진 구성정보 이용해 초기화
        appContext.refresh();
        // 클래스 타입만으로 빈 취득
        HelloController helloController = appContext.getBean(HelloController.class);
    }

}
