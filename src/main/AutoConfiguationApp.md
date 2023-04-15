# 자동구성기반 애플리케이션 

## Goal

SpringBoot의 `@AutoConfiguration`이, 어떻게 구성되어있고, 어떻게 작동하는지에 대한 이해

## TOC

### [x] 들어가며

- 자동구성이 뭐 대단한 것은 아니고, 기존 스프링에 있는 기술을 적절히 조합 및 활용하여 제공해주고 있는 것뿐이다.
- 다만, 자동구성을 이해하기 위해서는 `스프링부트가 어노테이션을 활용할때 사용하는 기법`을 잘 이해하여 응용이 필요하다.

### [x] Meta-Annotation and Composed-Annotation

Goal : 
- 메타어노테이션과 합성어노테이션의 설명

Q.Meta-Annotation?
- @Controller, @Service와 같은 스테레오 타입 어노테이션들은, `@Component을 자신의 어노테이션으로 다시 부여`하여 @Component과 동일한 효과를 내도록 만들었다.
- 즉, @Component를 메타어노테이션으로서 갖음으로써 해당 어노테이션이 적용된 효과를 내도록 하고 있는것.

Q.Meta-Annotation 활용시 이점이 있을까?
- 메타어노테이션이 붙은 클래스 사용시, 메타어노테이션이 적용된 것과 기능면에서는 동일하다.
  - 예) @Component를 직접 사용하는 것과, @Controller를 사용하는 것
- 그렇다면 어떠한 장점이 있을까?
  - 다른 어노테이션 이름을 가짐으로써, 목적과 역할을 명시적으로 표현 가능.
  - 다른 어노테이션이므로, 기존 메타어노테이션 + 부가기능(속성) 추가 가능.

Q. Meta-Annotation은 상속과 다른 개념이다?
- 당연히 어노테이션 자체에 상속이라는 개념은 존재하지 않는다.
- @Target이 ANNOTATION_TYPE으로 명시되어있을 경우만, 메타어노테이션으로 활용 가능.

HandsOn : JUnit5에 @UnitTest라는 어노테이션 만들어보기
```java
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Test
@interface UnitTest {}
```

스프링에서는 Meta-Annotation말고도 합성어노테이션(Component-Annotation)을 활용하고 있다.

Q.Composed-Annotation?

- 메타어노테이션을 하나이상 적용해서 만드는 어노테이션
- 예) @RestController = @ReponseBody + @Controller(which has @Component)
- 메타어노테이션이 중복되서 사용되는경우, 또는 덕지덕지 붙어 가독성을 해칠경우, 해당 메타어노테이션을 하나의 합성어노테이션으로 만들어 간결하게 사용가능하다.
 
### [x] Composed Annotation 적용

Goal : 
- 지금까지 작성한 코드에 메타어노테이션과 합성어노테이션을 이용하면 코드가 얼마나 간결해질수 있는지 확인.

`@SpringBootApplication`을 적용하지 않은 `HelloBootApplication`은 다음과 같이 구현되어 있다.
```java
@Configuration
@ComponentScan
public class HelloBootApplication {

  @Bean
  ServletWebServerFactory servletWebServerFactory() {
    return new TomcatServletWebServerFactory();
  }

  @Bean
  DispatcherServlet dispatcherServlet() {
    return new DispatcherServlet();
  }

  public static void main(String[] args) {
    SpringApplication.run(HelloBootApplication.class, args);
  }
}
```

What we want to do: 
- `@SpringBootApplication`을 이용해서 메인엔트리 작동시킬 때처럼, main메서드 이외의 정보는 별도로 분리하고 싶다.
  
Task : 
- [x] `@Configuration`과 `@ComponentScan`을 Composed Annoation으로 합치기 (`@MySpringBootApplication`)
- [x] `ServletWebServerFactory`와 `DispatcherServlet` Bean 등록 하는 Config을 별도클래스로  분리(`Config`)

Remaining Task :
- 단순히 어노테이션과 설정정보를 별도 분리한 것뿐이라, 유연하고 편리하게 다양한 기술을 적용할수 있도록, `@AutoConfiguration`구조로 확장해보기

### [x] Bean Object의 역할과 구분

Goal :
- 스프링에 등록되는 빈들은 성격이 조금씩 다른 것들이 존재.
- 이에 따라, 구성정보를 작성하는 방법도 달라질수 있다.
- 따라서, 스프링이 어떤 종류의 빈에, 어떤 스타일의 구성정보를 사용하는가를 파악해보자.


Q. 스프링 컨테이너에 등록되는 빈들은, 어떤 종류로 구분할수 있을까?
- **Application bean** :
  - `Controller, DataSource, JdbcTransactionManager`, ...
  - 개발자가 어떤 빈을 사용할지 `명시적으로 구성정보를 제공`한 빈.
  - **Application Logic bean**과, **Application Infra bean**으로 구분할수 있다.
  - **Application Logic Bean**의 구성 정보는, 사용자가 명시적으로  `ComponenScan`으로 구성정보 제공.
  - **Application Infra Bean**의 구성 정보는 `AutoConfiguration`을 이용해 **자동**으로 구성정보 제공.

- **Container Infra bean** :
  - `ApplicationContext`, `BeanFactoryPostProcessor`,`BeanPostProcessor`, `DefaultAdvisorAutoProxyCreator`,...
  - 스프링 컨테이너 자신이거나, 컨테이너 내부에서 사용되거나, 특정 기술 구현을 위해 자동으로 등록한 빈.

Q. 스프링부트가 container-less 달성을 위해, 내장형 서블릿 컨테이너를 이용하는 standalone방식으로 동작시키기 위해 이용하는 다음과 같은 빈들이 존재하는데, 이 빈들은 어떤 빈에 속하는 것일까?
- `ServletWebServerFactory` bean
- `DispatcherServlet` bean 
- A. 명시적으로 구성 정보를 제공해야 하므로, **Appliction Infra Bean**.
- A. **Appliction Infra Bean**이기에, `자동 구성정보(AutoConfiguration)`를 이용해 구성 정보가 만들어진다.

Q. `자동 구성정보(AutoConfiguration)`는 어떻게 구성되어 있나?
Simply put,
- Application에서 사용될수 있는 각 인프라 빈들을 담은 `@Configuration` class들을 `각 기능별`로 구분하여 작성.
- SpringBoot가 Application의 필요에 따라, 필요한 설정정보들을 골라 필요한 방식으로 구성하여 자동으로 적용.

### [ ] 인프라 빈 구성정보 분리

What we want to do :
- `ServletWebServerFactory`, `DispatcherServlet` bean을, **Application Infra Bean**으로 등록하고 싶다.
- 따라서, 단순 @ComponentScan이 아닌, @AutoConfiguration 이용해 `자동 구성정보` 만들기.

Task :
- [] @ComponentScan의 basepackage대상으로부터 제외하기(다른 패키지로 이동)
- 

### [ ] 동적인 자동구성정보 등록

### [ ] 자동구성정보 파일 분리

### [ ] 자동구성 어노테이션 적용

### [ ] @Configuration and proxyBeanMethods

### Note


