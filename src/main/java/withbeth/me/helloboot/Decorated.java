package withbeth.me.helloboot;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.springframework.beans.factory.annotation.Qualifier;

@Qualifier("decorated")
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({
    java.lang.annotation.ElementType.TYPE,
    java.lang.annotation.ElementType.PARAMETER
})
public @interface Decorated {
}
