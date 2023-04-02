# 자동구성기반 애플리케이션 

### Goal
SpringBoot의 AutoConfiguration이, 어떻게 구성되어있고, 어떻게 작동하는지에 대한 이해

### TOC

[x] 들어가며

- 자동구성이 뭐 대단한 것은 아니고, 기존 스프링에 있는 기술을 적절히 조합 및 활용하여 제공해주고 있는 것뿐이다.
- 다만, 자동구성을 이해하기 위해서는 스프링부트가 어노테이션을 활용할때 사용하는 기법을 잘 이해하여 응용이 필요하다.

[x] Meta-Annotation and Composed-Annotation

- Goal : 메타어노테이션과 합성어노테이션의 설명

- Q.Meta-Annotation?
  - @Controller, @Service와 같은 스테레오 타입 어노테이션들은, @Component을 자신의 어노테이션으로 다시 부여하여 @Component과 동일한 효과를 내도록 만들었다.
  - 즉, @Component를 메타어노테이션으로서 갖음으로써 해당 어노테이션이 적용된 효과를 내도록 하고 있는것.

- Q.Meta-Annotation 활용시 이점이 있을까?
  - 메타어노테이션이 붙은 클래스 사용시, 메타어노테이션이 적용된 것과 기능면에서는 동일하다.
    - 예) @Component를 직접 사용하는 것과, @Controller를 사용하는 것
  - 그렇다면 어떠한 장점이 있을까?
    - 다른 어노테이션 이름을 가짐으로써, 목적과 역할을 명시적으로 표현 가능.
    - 다른 어노테이션이므로, 기존 메타어노테이션 + 부가기능(속성) 추가 가능.

- Q. Meta-Annotation은 상속과 다른 개념이다?
  - 당연히 어노테이션 자체에 상속이라는 개념은 존재하지 않는다.
  - @Target이 AnnotationType으로 명시되어있을 경우만, 메타어노테이션으로 활용 가능.

- HandsOn. JUnit5에 @UnitTest라는 어노테이션 만들어보기
  ```java
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
  @Test
  @interface UnitTest {}
  ```
- 스프링에서는 Meta-Annotation말고도 합성어노테이션(Component-Annotation)을 활용하고 있다.
  - Q.Composed-Annotation?
    - 메타어노테이션을 하나이상 적용해서 만드는 어노테이션
    - 예) @RestController = @ReponseBody + @Controller(which has @Component)
    - 메타어노테이션이 중복되서 사용되는경우, 또는 덕지덕지 붙어 가독성을 해칠경우, 해당 메타어노테이션을 하나의 합성어노테이션으로 만들어 간결하게 사용가능하다.
 
[ ] Composed Annotation 적용

- Goal : 지금까지 작성한 코드에 메타어노테이션과 합성어노테이션을 이용하면 코드가 얼마나 간결해질수 있는지 확인

[ ] Bean Object의 역할과 구분

[ ] 인프라 빈 구성정보 분리

[ ] 동적인 자동구성정보 등록

[ ] 자동구성정보 파일 분리

[ ] 자동구성 어노테이션 적용

[ ] @Configuration and proxyBeanMethods

### Note



### QnA
- Q. 스프링에서는 어노테이션을 어떻게 활용하고 있나?
