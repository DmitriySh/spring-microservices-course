Quiz for students
=======
Use Spring Core 5.x.

## Description
 - The program stores questions and answers into a csv file
 - The program runs in a terminal and asks a user:
   - name and surname,
   - questions from a csv file
   - gets a result at the end of the quiz
 - Services covered by unit tests

## Documentation
 * Use class [ClassPathXmlApplicationContext](https://docs.spring.io/spring/docs/5.0.x/javadoc-api/org/springframework/context/support/ClassPathXmlApplicationContext.html) to build the container of [Inversion of Control (IoC)](https://docs.spring.io/spring/docs/5.0.x/spring-framework-reference/core.html#spring-core).
 * The container then injects those dependencies when it creates the bean. 
 * This process is fundamentally the inverse, hence the name IoC, of the bean itself controlling the instantiation or location of its dependencies by using direct construction of classes, or a mechanism such as the Service Locator pattern.
 * The configuration metadata is represented in XML file.

Spring Framework Core Technologies: [reference documentation](https://docs.spring.io/spring/docs/5.0.x/spring-framework-reference/core.html#spring-core)


## Run
 *  Build project
```sh
spring-microservices-course$ ./gradlew clean build

> Configure project :
Version 0.1


BUILD SUCCESSFUL in 3s
6 actionable tasks: 6 executed

```  

  *  Run server: 
```sh
spring-microservices-course$ java -jar ./01-spring-xml/build/libs/01-spring-xml-all-0.1.jar
22:26:30.416 [main] INFO  o.s.c.s.ClassPathXmlApplicationContext - Refreshing org.springframework.context.support.ClassPathXmlApplicationContext@2ed94a8b: startup date [Tue Jul 03 22:26:30 MSK 2018]; root of context hierarchy
22:26:30.458 [main] INFO  o.s.b.f.x.XmlBeanDefinitionReader - Loading XML bean definitions from class path resource [context.xml]
22:26:30.659 [main] INFO  r.s.s.Reader - init questions...
22:26:30.801 [main] INFO  r.s.s.Reader - read csv file: questions.csv, lines: 6
22:26:30.818 [main] INFO  r.s.s.Quiz - Hello quiz!

Please type your first name: <name>
Please type your surname: <surname>
Good luck <name> <surname>!
Start quiz...

Question 1: Столица России
Answers:
	1: Москва
	2: Санкт-Петербург
	3: Абакан
	4: Киров
	5: Астана
Please type number your answer: <answer>
...
```

## Stop

 * The program is terminated at the end of quiz or response to a user interrupt, such as typing `^C` (Ctrl + C), or a system-wide event of a shutdown.
```sh
Result: 5/6
Byu! =)
22:27:12.072 [main] INFO  o.s.c.s.ClassPathXmlApplicationContext - Closing org.springframework.context.support.ClassPathXmlApplicationContext@2ed94a8b: startup date [Tue Jul 03 22:26:30 MSK 2018]; root of context hierarchy
```
