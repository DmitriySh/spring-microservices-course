Quiz for students
=======
Use `Spring Boot 2.x` and `Spring Shell 2.x`.<br/>
Not all applications need graphical or web user interface! 
Sometimes an application could use an interactive `terminal` and it is the most appropriate way to get things done 
and `Spring Shell` delivers this functionality.

## Description
 * The program stores questions and answers into a bundle of properties files (rus and eng)
 * The program runs in a terminal and asks a user:
   * name and surname,
   * questions from the bundle
   * gets a result at the end of the quiz
 * Language configures into config file `application.yml`
 * Services covered by unit tests

## Documentation
 * [Spring Shell 2.x](https://docs.spring.io/spring-shell/docs/2.0.x/reference/htmlsingle/) uses for a regular 
 `Spring Boot` application but isn't part of `spring-boot-starter-parent`
 * The [Spring Shell 2.x](https://docs.spring.io/spring-shell/docs/2.0.x/reference/htmlsingle) project provides 
 the infrastructure to create such a REPL applications, where the user will enter textual commands that will get executed.
 * Advanced features:
     * text parsing and validation,
     * TAB completion,
     * colorization of output, 
     * fancy ascii-art table display, 
 * The `Spring Shell 2.x` project builds on top of the [JLine library](https://github.com/jline/jline3) for handling console input

Main site of `Spring Shell`: https://docs.spring.io/spring-shell/docs/2.0.x/reference/htmlsingle/

## Run
 *  Build project
```sh
spring-microservices-course$ ./gradlew clean build
                             
> Configure project :
Version 0.1
                             
BUILD SUCCESSFUL in 13s
24 actionable tasks: 20 executed, 4 up-to-date
```  

  *  Run server: 
```sh
spring-microservices-course$ ./04-spring-shell/build/libs/04-spring-shell-all-0.1.jar
________        .__
\_____  \  __ __|__|_______
 /  / \  \|  |  \  \___   /
/   \_/.  \  |  /  |/    /
\_____\ \_/____/|__/_____ \
       \__>              \/
Dmitriy Shishmakov | Spring Boot

19:47:38.183 [main] INFO  r.s.Main - Starting Main on shishmakov.local with PID 10564 (/Users/dima/programming/git/otus/spring-course/spring-microservices-course/04-spring-shell/build/libs/04-spring-shell-all-0.1.jar started by dima in /Users/dima/programming/git/otus/spring-course/spring-microservices-course/04-spring-shell/build/libs)
19:47:38.188 [main] DEBUG r.s.Main - Running with Spring Boot v2.0.3.RELEASE, Spring v5.0.7.RELEASE
19:47:38.189 [main] INFO  r.s.Main - No active profile set, falling back to default profiles: default
19:47:38.246 [main] INFO  o.s.c.a.AnnotationConfigApplicationContext - Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@449b2d27: startup date [Fri Jul 20 19:47:38 MSK 2018]; root of context hierarchy
19:47:38.814 [main] INFO  r.s.s.Reader - init questions...
19:47:38.821 [main] INFO  r.s.s.Reader - read questions: 6

Hello quiz!
Please type your name and surname

19:47:39.353 [main] INFO  o.s.j.e.a.AnnotationMBeanExporter - Registering beans for JMX exposure on startup
19:47:39.365 [main] INFO  r.s.Main - Started Main in 1.521 seconds (JVM running for 2.103)
input> name <name>
Please type your name and surname
input> surname <surname>
Good luck <name> <surname>!
Start quiz...

Question 1: The capital of Russia
Answers:
	1: Moscow
	2: St.Petersburg
	3: Kirov
	4: Astana
	5: Abakan
Please type number your answer
input> number <number>
...
```

## Stop

 * The program is terminated at the end of quiz, by command `exit` or response to a user interrupt, such as typing `^C` (Ctrl + C), or a system-wide event of a shutdown.
```sh
...
Please type number your answer
input> exit

Result: 5/6
Goodbye! =)
19:52:15.086 [Thread-2] INFO  o.s.c.a.AnnotationConfigApplicationContext - Closing org.springframework.context.annotation.AnnotationConfigApplicationContext@7d4793a8: startup date [Fri Jul 20 19:51:43 MSK 2018]; root of context hierarchy
19:52:15.090 [Thread-2] INFO  o.s.j.e.a.AnnotationMBeanExporter - Unregistering JMX-exposed beans on shutdown
```
