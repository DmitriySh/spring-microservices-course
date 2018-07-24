Quiz for students
=======
Use `Spring Core 5.x`.

## Description
 - The program stores questions and answers into a bundle of properties files (rus and eng)
 - The program runs in a terminal and asks a user:
   - name and surname,
   - questions from the bundle
   - gets a result at the end of the quiz
 - Language configures into config file `conf.properties`
 - Services covered by unit tests

## Documentation
 * Use class [AnnotationConfigApplicationContext](https://docs.spring.io/spring/docs/5.0.x/javadoc-api/org/springframework/context/annotation/AnnotationConfigApplicationContext.html) to build the container of [Inversion of Control (IoC)](https://docs.spring.io/spring/docs/5.0.x/spring-framework-reference/core.html#spring-core).
 * The container then injects those dependencies when it creates the bean. 
 * This process is fundamentally the inverse, hence the name IoC, of the bean itself controlling the instantiation or location of its dependencies by using direct construction of classes, or a mechanism such as the Service Locator pattern.
 * Java-configuration support are [@Configuration](https://docs.spring.io/spring/docs/5.0.x/javadoc-api/org/springframework/context/annotation/Configuration.html)-annotated classes and [@Bean](https://docs.spring.io/spring/docs/5.0.x/javadoc-api/org/springframework/context/annotation/Bean.html)-annotated methods, separated classes with any Spring [@Component](https://docs.spring.io/spring/docs/5.0.x/javadoc-api/org/springframework/stereotype/Component.html) annotations.

Annotation-based container configuration: [reference documentation](https://docs.spring.io/spring/docs/5.0.x/spring-framework-reference/core.html#beans-annotation-config)


## Run
 *  Build project
```sh
spring-microservices-course$ ./gradlew clean build

> Configure project :
Version 0.1


BUILD SUCCESSFUL in 3s
12 actionable tasks: 12 executed
```  

  *  Run server: 
```sh
spring-microservices-course$ java -jar ./02-spring-annotation/build/libs/02-spring-annotation-all-0.1.jar
21:49:45.526 [main] INFO  o.s.c.a.AnnotationConfigApplicationContext - Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@7a5d012c: startup date [Wed Jul 04 21:49:45 MSK 2018]; root of context hierarchy
21:49:45.765 [main] INFO  r.s.s.Reader - init questions...
21:49:45.777 [main] INFO  r.s.s.Reader - read questions: 6
21:49:45.804 [main] INFO  r.s.s.Quiz - Hello quiz!

Please type your first name: <name>
Please type your surname: <surname>
Good luck <name> <surname>!
Start quiz...

Question 1: The capital of Russia
Answers:
	1: Abakan
	2: Kirov
	3: Astana
	4: Moscow
	5: St.Petersburg
Please type number your answer:
...
```

## Stop

 * The program is terminated at the end of quiz or response to a user interrupt, such as typing `^C` (Ctrl + C), or a system-wide event of a shutdown.
```sh
Result: 5/6
Goodbye! =)
21:52:01.799 [main] INFO  o.s.c.a.AnnotationConfigApplicationContext - Closing org.springframework.context.annotation.AnnotationConfigApplicationContext@7a5d012c: startup date [Wed Jul 04 21:49:45 MSK 2018]; root of context hierarchy
```
