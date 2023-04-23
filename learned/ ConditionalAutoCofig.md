# 조건부 자동구성

## [x] Jetty 스타터와 자동구성정보 Config 클래스 추가 

### Goal 

스프링부트가 제공하는 자동구성Config클래스에는 대표적으로 다음이 존재한다.
- `@AutoConfiguration` in `spring-boot-autoconfigure` lib.

또한, 해당 어노테이션을 이용하는 자동구성Config 클래스들은 다음에서 확인 가능
- `org.springframework.boot.autoconfigure.AutoConfiguration.imports`

그런데, 해당 파일에 등록된 클래스는 144개나 존재하고, 해당 Config클래스들은 또한 여러개의 @Bean을 등록하는 Config클래스이기에, 아무 조건 없이 해당 클래스를 모두 읽어 들이면 빈은 수백개이상 등록이 될것이다.

따라서, 분명, `특정 조건이 만족해야만`, 해당 Config을 읽어들이는 프로세스가 존재할 것.

- 그것이 바로 `조건부 자동구성`.

- > 예: `Thymeleaf`를 사용하지 않을 경우 해당 자동구성Config을 사용하고 싶지 않을 것.

그럼, 그걸 어떻게 조건을 명시할수 있나?

바로 그 HOW를 알아보자.

### What we want to do 

Tomcat은, 자바의 ServletContainer기술을 구현한 구현라이브러리 중 하나.

따라서, 특정 조건을 걸어, 스프링 부트가 stand-alone을 지원하는 특정 ServletContainer 을 선택하게 하고 싶다.

이걸, 어떻게 선택하게 할 수 있을까?

참고로 jetty는, [spring-boot-starter-jetty](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#using.build-systems.starters)를 이용해야 한다.

### Q. Jetty?

자바 기반 경량 웹 서버.

HTTP 클라이언트 및 서버를 지원하며, 비동기식 및 이벤트 기반 처리를 통해 높은 처리량과 낮은 지연 시간을 제공. 

또한 Servlet, WebSocket 및 HTTP/2 지원.

### Task

- [x] `spring-boot-start-jetty` 의존성 추가
- [x] `JettyWebServerConfig`자동구성클래스 추가
  - [x] `TomCatWbeServerConfig`과 Bean Name이 겹치지 않도록 다른 Bean Name부여
- [x] `@MyAutoConfiguration`imports 파일에 `JettyWebServerConfig` 클래스 추가

### Remaining Task

이대로 실행하면 SameType이 여러개 존재 하므로 예외 발생 할 것이다.

- `org.springframework.context.ApplicationContextException: Unable to start ServletWebServerApplicationContext due to multiple ServletWebServerFactory beans : servletWebServerFactory,jettyWebServer`

따라서, 특정 조건시, 특정 빈을 선택하도록 하는 프로세스를 구현해야 한다.

## [x] @Conditional and Condition 

### What we want to do

조건을 나타내는 `Condition` 클래스를 구현하고,
해당 조건을 이용해 자동구성 Config Class Level에 `@Conditional`적용.

`Condition`클래스의 `matches`결과 여부를 통해, 
해당 자동설정 Config클래스의 적용 여부를 결정할수 있다.

일단, DeepDive하지 말고, 간단히 움직이는 것을 테스트해보자.

`JettyWebServerConfig`클래스의 `Condition`은 true를 반환하도록 하드코딩하자.

### Task

- [x] `TomcatWebServerConfig`클래스에 `@Conditional`과 Condition적용(match result = false)
- [x] `JettyWebServerConfig`클래스에 `@Conditional`과 Condition적용(match result = true)

### Remaining Task

어떻게 `Condition`을 구현할 것인가.

## [x] @Conditional LearningTest

### What we want to do

학습테스트를 통해, 어떻게 `Conditional`이 작동하는지 확인.

### @Conditional and Condition

![IMG_11B183ED2C1A-1.jpeg](..%2Fimage%2FIMG_11B183ED2C1A-1.jpeg)

@Conditional은, Config과 Bean에 Annotate가능.

`@Config` (Class lv) : if Condition == true, Configuation Bean등록 + 포함된 모든 @Bean 팩토리 메서드 실행하여 Bean 등록.

`@Bean` (Method lv) : if Condition == true, @Bean 팩토리 메서드 실행하여 Bean등록.

Note : 애초에 `@Config`의 Condition이 false일경우, `@Bean` Condition 체크 X.

### `@Conditional`에 적용된 attribute 값을, `Condition`클래스에서 이용하는 방법

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Conditional(BooleanCondition.class)
@interface BooleanConditional {
    boolean value();
}

@Configuration
@BooleanConditional(true) // Conditional 어노테이션의 value 설정
static class ConfigWithTrueCondition {
}

static class BooleanCondition implements Condition {
  @Override
  public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    // AnnotatedTypeMetadata 객체로부터, Conditional의 설정된 value값 획득
    Map<String, Object> annotationAttributes = 
            metadata.getAnnotationAttributes(BooleanConditional.class.getName());
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
```

### Tip : Test목적으로 만들어진 ApplicationContext

매번 ApplicationContext만들고, bean register -> refersh -> getBean()하지 않아도 된다.

테스트 목적으로 만들어진 `ApplicationContextRunner`이용하면,

Assert library와 조합하여 손쉽게 컨텍스트 assertion 가능.

> Utility design to run an ApplicationContext and provide AssertJ style assertions. 

```java
class MyContextTests {
  private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
    .withPropertyValues("spring.foo=bar")
    .withUserConfiguration(MyConfiguration.class);

  // Run method takes a ContextConsumer that can apply assertions to the context. 
  // Upon completion, the context is automatically closed.
  @Test
  someTest() {
    this.contextRunner.withPropertyValues("spring.foo=biz").run((context) -> {
        assertThat(context).containsSingleBean(MyBean.class);
        // other assertions
    });
  }

  // If the application context fails to start the #run(ContextConsumer) method is called with a "failed" application context. 
  // Calls to the context will throw an IllegalStateException and assertions that expect a running context will fail. 
  // The getFailure() assertion can be used if further checks are required on the cause of the failure:
  @Test
  someTest() {
    this.context.withPropertyValues("spring.foo=fails").run((loaded) -> {
         assertThat(loaded).getFailure().hasCauseInstanceOf(BadPropertyException.class);
        // other assertions
    });
  }
}
```

## [ ] Custom @Conditional 

### What we want to do 
`@Conditional`과 `Condition`을 활용해, 조건에 따라 Tomcat or Jetty를 사용하고 싶다.

### Q. 그렇다면, 무엇을 기준으로 할 것인가?

자동구성에서 가장 대표적으로 쓰이는 방법은, `특정 라이브러리(특정 클래스)가 프로젝트에 포함되어 있는가` 이다.
- 예: embedded Tomcat 라이브러리가 포함되어 있을 경우에는 Tomcat을 이용하는 방식.

그렇다면, 
- `org.apache.catalina.startup.Tomcat` 가 현 프로젝트에 포함된 경우, `TomcatServletWebServerFactory` 을 이용.
- `org.eclipse.jetty.server.Server` 가 현 프로젝트에 포함된 경우, `JettyServletWebServerFactory`를 이용하도록 해보자.

### Q. 그렇다면, 어떤 라이브러리(클래스)가 현재 프로젝트에 포함되어 있는지는 어떻게 알 수 있을까?

`org.springframework.util.ClassUtils#isPresent()` 이용.
- className : FQCN
- classLoader : `ConditionalContext.getClassLoader()`


## [ ] 자동 구성 정보 대체하기 

## [ ] 스프링 부트의 @Conditional 
