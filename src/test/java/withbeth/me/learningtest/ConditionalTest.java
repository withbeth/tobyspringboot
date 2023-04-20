package withbeth.me.learningtest;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.AnnotatedTypeMetadata;

class ConditionalTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner();

    @DisplayName("Condition=true일경우, 해당 구성정보 클래스의 빈을 등록한다")
    @Test
    void whenConditionIsTrue() {
        /*
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext();
        ac.registerBean(Config1.class);
        ac.refresh();
        ac.getBean(MyBean.class);
         */
        contextRunner.withUserConfiguration(ConfigWithTrueCondition.class)
            .run(context -> {
                assertThat(context).hasSingleBean(MyBean.class);
                assertThat(context).hasSingleBean(ConfigWithTrueCondition.class);
            });
    }

    @DisplayName("Condition=false일경우, 해당 구성정보 클래스의 빈을 등록하지 않는다")
    @Test
    void whenConditionIsFalse() {
        contextRunner.withUserConfiguration(ConfigWithFalseCondition.class)
            .run(context -> {
                assertThat(context).doesNotHaveBean(MyBean.class);
                assertThat(context).doesNotHaveBean(ConfigWithFalseCondition.class);
            });
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Conditional(BooleanCondition.class)
    @interface BooleanConditional {
        boolean value();
    }

    @Configuration
    @BooleanConditional(true)
    static class ConfigWithTrueCondition {
        @Bean
        MyBean myBean() {
            return new MyBean();
        }
    }

    @Configuration
    @BooleanConditional(false)
    static class ConfigWithFalseCondition {
        @Bean
        MyBean myBean() {
            return new MyBean();
        }
    }

    static class MyBean {}

    static class BooleanCondition implements Condition {
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(
                BooleanConditional.class.getName());
            if (annotationAttributes == null) {
                return false;
            }
            try {
                return (Boolean) annotationAttributes.get("value");
            } catch (ClassCastException e) {
                return false;
            }
        }
    }

}
