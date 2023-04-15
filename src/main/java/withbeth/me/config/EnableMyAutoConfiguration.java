package withbeth.me.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;
import withbeth.me.config.autoconfig.DispatcherServletConfig;
import withbeth.me.config.autoconfig.TomCatWebServerConfig;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({DispatcherServletConfig.class, TomCatWebServerConfig.class})
public @interface EnableMyAutoConfiguration {
}
