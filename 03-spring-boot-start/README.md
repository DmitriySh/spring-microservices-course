Quiz for students
=======
Use Spring Boot 2.0.x.<br/>
Spring Boot makes it easy to create stand-alone, production-grade Spring-based applications that you can run quickly. 
They will need minimal or zero configuration.

## Description
 * The program stores questions and answers into a bundle of properties files (rus and eng)
 * The program runs in a terminal and asks a user:
   * name and surname,
   * questions from the bundle
   * gets a result at the end of the quiz
 * Language configures into config file `application.yml`
 * Services covered by unit tests

## Documentation
 * Spring Boot 2.x could start with one of embedded servlet container (Servlet 3.1/4.0) for instance: 
 	* Tomcat 8.5(9.0) (spring-boot-starter-tomcat, default), 
 	* Jetty 9.4 (spring-boot-starter-jetty) 
 	* Undertow 1.4(2.0) (spring-boot-starter-undertow)
    * or embedded reactive HTTP server (spring-boot-starter-reactor-netty)

 * Spring Boot defines `starters` used to limit the amount of manual dependency configuration that you have to do 
 to get a project up and run it quickly (https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#using-boot-starter)
 * Spring Boot supports `auto-configuration` attempts to automatically configure your Spring application 
 with "Opinionated Defaults Configuration":
   * app will use `H2` in-memory database if you have JAR file `h2.jar` in classpath and have configured no other `DataSource` beans,
   * app will automatically configure JPA if you annotated your JPA beans with `@Entity` and don't have a `persistence.xml` file
   * and others ...
 * Power of annotations that could replace set of separated annotations ([@SpringBootApplication](https://docs.spring.io/spring-boot/docs/2.0.x/reference/html/using-boot-using-springbootapplication-annotation.html) = [@Configuration](https://docs.spring.io/spring/docs/5.0.x/javadoc-api/org/springframework/context/annotation/Configuration.html) + [@ComponentScan](https://docs.spring.io/spring/docs/5.0.x/javadoc-api/org/springframework/context/annotation/ComponentScan.html) + [@EnableAutoConfiguration](https://docs.spring.io/spring-boot/docs/2.0.x/api/org/springframework/boot/autoconfigure/EnableAutoConfiguration.html))
 * You can start to define your own configuration to replace specific parts of the [auto-configuration](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#using-boot-auto-configuration),   

 * Spring Boot could package app and dependencies into a single, executable JAR as an [init.d service](https://docs.spring.io/spring-boot/docs/current/reference/html/deployment-install.html)
 * Plugin for Spring Boot defines a special JAR file layout where app classes locates into `BOOT-INF/classes` directory and Jar files of libraries into `BOOT-INF/lib`
 * [spring-boot-starter-test](https://docs.spring.io/spring-boot/docs/2.0.x/reference/html/boot-features-testing.html) provides a number of utilities and annotations to help when testing your application

Main site of Spring Boot: https://spring.io.<br/>
Spring Initializr for Spring Boot: https://start.spring.io

## Run
 *  Build project
```sh
spring-microservices-course$ ./gradlew clean build

> Configure project :
Version 0.1

BUILD SUCCESSFUL in 7s
18 actionable tasks: 18 executed
```  

  *  Run server: 
```sh
spring-microservices-course$ ./03-spring-boot-start/build/libs/03-spring-boot-start-all-0.1.jar
________        .__
\_____  \  __ __|__|_______
 /  / \  \|  |  \  \___   /
/   \_/.  \  |  /  |/    /
\_____\ \_/____/|__/_____ \
       \__>              \/

13:56:18.062 [main] INFO  r.s.Main - Starting Main on shishmakov.local with PID 43209 (/Users/dima/programming/git/otus/spring-course/spring-microservices-course/03-spring-boot-start/build/libs/03-spring-boot-start-all-0.1.jar started by dima in /Users/dima/programming/git/otus/spring-course/spring-microservices-course/03-spring-boot-start/build/libs)
13:56:18.066 [main] DEBUG r.s.Main - Running with Spring Boot v2.0.3.RELEASE, Spring v5.0.7.RELEASE
13:56:18.067 [main] INFO  r.s.Main - No active profile set, falling back to default profiles: default
13:56:18.126 [main] INFO  o.s.c.a.AnnotationConfigApplicationContext - Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@61baa894: startup date [Tue Jul 17 13:56:18 MSK 2018]; root of context hierarchy
13:56:18.685 [main] INFO  o.s.j.e.a.AnnotationMBeanExporter - Registering beans for JMX exposure on startup
13:56:18.740 [main] INFO  r.s.Main - Started Main in 1.013 seconds (JVM running for 1.597)
13:56:18.742 [main] INFO  r.s.s.Reader - init questions...
13:56:18.749 [main] INFO  r.s.s.Reader - read questions: 6
13:56:18.749 [main] INFO  r.s.s.Quiz - Hello quiz!

Please type your first name: <name>
Please type your surname: <surname>
Good luck <name> <surname>!
Start quiz...

Question 1: The capital of Russia
Answers:
	1: Astana
	2: Abakan
	3: St.Petersburg
	4: Moscow
	5: Kirov
Please type number your answer:
...
```

## Stop

 * The program is terminated at the end of quiz or response to a user interrupt, such as typing `^C` (Ctrl + C), or a system-wide event of a shutdown.
```sh
Result: 5/6
Goodbye! =)
13:58:10.931 [main] INFO  o.s.c.a.AnnotationConfigApplicationContext - Closing org.springframework.context.annotation.AnnotationConfigApplicationContext@61baa894: startup date [Tue Jul 17 13:56:18 MSK 2018]; root of context hierarchy
13:58:10.933 [main] INFO  o.s.j.e.a.AnnotationMBeanExporter - Unregistering JMX-exposed beans on shutdown
```
