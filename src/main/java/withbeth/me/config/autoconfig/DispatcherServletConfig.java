package withbeth.me.config.autoconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.DispatcherServlet;
import withbeth.me.config.MyAutoConfiguration;

@MyAutoConfiguration
public class DispatcherServletConfig {
    @Bean
    DispatcherServlet dispatcherServlet() {
        return new DispatcherServlet();
    }
}
