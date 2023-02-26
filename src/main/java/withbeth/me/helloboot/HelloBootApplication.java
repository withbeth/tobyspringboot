package withbeth.me.helloboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;

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
        ServletWebServerFactory serverFactory = new TomcatServletWebServerFactory();
        WebServer webServer = serverFactory.getWebServer(servletContext ->
                servletContext.addServlet("myServlet", new HttpServlet() {
                    @Override
                    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
                        resp.setStatus(200);
                        resp.setHeader("Content-Type", "text/plain");
                        // 간단히 body 부분 설정
                        resp.getWriter().println("hello from servlet");
                    }
                }).addMapping("/hello"));
        webServer.start();
    }

}
